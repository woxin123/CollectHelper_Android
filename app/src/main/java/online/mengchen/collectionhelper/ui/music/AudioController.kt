package online.mengchen.collectionhelper.ui.music

import android.content.Context
import android.net.Uri
import android.os.Handler
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioListener
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class AudioController(private val context: Context, private val handler: Handler = Handler()) {
    private val formatBuilder: StringBuilder by lazy { StringBuilder() }
    private val formatter: Formatter by lazy { Formatter(formatBuilder, Locale.getDefault()) }
    private var adGroupTimesMs: LongArray = LongArray(0)
    private var playedAdGroups: BooleanArray = BooleanArray(0)
    private val controlDispatcher: DefaultControlDispatcher by lazy { DefaultControlDispatcher() }
    private val window: Timeline.Window by lazy { Timeline.Window() }
    private val period: Timeline.Period by lazy { Timeline.Period() }
    private val simpleExoPlayer: SimpleExoPlayer by lazy {
        ExoPlayerFactory.newSimpleInstance(
            context,
            DefaultTrackSelector()
        )
    }
    private var sourceList: List<MediaSource> = ArrayList()
    private val defaultDataSourceFactory: DefaultDataSourceFactory by lazy {
        DefaultDataSourceFactory(
            context,
            "CollectHelp"
        )
    }
    var listener: AudioControlListener? = null
    private var curUri: String? = null
    private var position = 0

    init {
        // 创建一个媒体连接资源
        val concatenatingMediaSource = ConcatenatingMediaSource()
        concatenatingMediaSource.addMediaSources(sourceList)
        simpleExoPlayer.playWhenReady = false
        initListener()
    }

    private fun initListener() {
        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onLoadingChanged(isLoading: Boolean) {
                if (isLoading) {
                    handler.post(loadStatusRunnable)
                }
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    listener?.onComplete()
                }
            }
        })
    }

    fun onPrepare(uri: String) {
        if (uri == curUri) {
            return
        }
        curUri = uri
        val mediaSource = ExtractorMediaSource.Factory(
            defaultDataSourceFactory
        ) //创建一个播放数据源
            .createMediaSource(Uri.parse(uri))
        simpleExoPlayer.prepare(mediaSource)
    }

    fun onStart(position: Int) {
        this.position = position
        if (simpleExoPlayer.playbackState == Player.STATE_IDLE) {
            // ignore
        } else if (simpleExoPlayer.playbackState == Player.STATE_ENDED) {
            //重新播放
            controlDispatcher
                .dispatchSeekTo(
                    simpleExoPlayer, simpleExoPlayer.currentWindowIndex,
                    C.TIME_UNSET
                )
        }
        controlDispatcher.dispatchSetPlayWhenReady(simpleExoPlayer, true)
        if (listener != null) {
            listener!!.isPlay(position, true)
        }
    }

    fun onPause() {
        controlDispatcher.dispatchSetPlayWhenReady(simpleExoPlayer, false)
        if (listener != null) {
            listener!!.isPlay(position, false)
        }
    }


    fun seekToTimeBarPosition(positionMs: Long) {
        var positionMs = positionMs
        val timeline = simpleExoPlayer.currentTimeline
        var windowIndex: Int
        if (!timeline.isEmpty) {
            val windowCount = timeline.windowCount
            windowIndex = 0
            while (true) {
                val windowDurationMs =
                    timeline.getWindow(windowIndex, window).durationMs
                if (positionMs < windowDurationMs) {
                    break
                } else if (windowIndex == windowCount - 1) {
                    // Seeking past the end of the last window should seek to the end of the
                    // timeline.
                    positionMs = windowDurationMs
                    break
                }
                positionMs -= windowDurationMs
                windowIndex++
            }
        } else {
            windowIndex = simpleExoPlayer.currentWindowIndex
        }
        val dispatched = controlDispatcher
            .dispatchSeekTo(simpleExoPlayer, windowIndex, positionMs)
        if (!dispatched) {
            handler.post(loadStatusRunnable)
        }
    }

    fun release() {
        simpleExoPlayer.release()
    }

    private val loadStatusRunnable = object : Runnable {
        override fun run() {
            var durationUs = 0L
            var adGroupCount = 0
            var currentWindowTimeBarOffsetMs = 0L
            val currentTimeline = simpleExoPlayer.currentTimeline
            if (!currentTimeline.isEmpty) {
                val currentWindowIndex = simpleExoPlayer.currentWindowIndex
                val firstWindowIndex = currentWindowIndex
                val lastWindowIndex = currentWindowIndex
                for (i in firstWindowIndex..lastWindowIndex) {
                    if (i == currentWindowIndex) {
                        currentWindowTimeBarOffsetMs = C.usToMs(durationUs)
                    }
                    currentTimeline.getWindow(i, window)
                    if (window.durationMs == C.TIME_UNSET) {
                        break
                    }
                    for (j in window.firstPeriodIndex..window.lastPeriodIndex) {
                        currentTimeline.getPeriod(j, period)
                        val periodAdGroupCount = period.adGroupCount
                        for (adGroupIndex in 0 until periodAdGroupCount) {
                            var adGroupTimeInPeriodUs = period.getAdGroupTimeUs(adGroupIndex)
                            if (adGroupTimeInPeriodUs == C.TIME_END_OF_SOURCE) {
                                if (period.durationUs == C.TIME_UNSET) {
                                    continue
                                }
                                adGroupTimeInPeriodUs = period.durationUs
                            }
                            val adGroupTimeInWindowUs =
                                adGroupTimeInPeriodUs + period.positionInWindowUs
                            if (adGroupTimeInWindowUs >= 0 && adGroupTimeInWindowUs <= window.durationUs) {
                                if (adGroupCount == adGroupTimesMs.size) {
                                    val newLength =
                                        if (adGroupTimesMs.isEmpty()) 1 else adGroupTimesMs.size * 2
                                    adGroupTimesMs = adGroupTimesMs.copyOf(newLength)
                                    playedAdGroups = playedAdGroups.copyOf(newLength)
                                }
                                adGroupTimesMs[adGroupCount] =
                                    C.usToMs(durationUs + adGroupTimeInWindowUs)
                                playedAdGroups[adGroupCount] = period.hasPlayedAdGroup(adGroupIndex)
                                adGroupCount++
                            }
                        }
                    }
                    durationUs += window.durationUs
                }
            }

            durationUs = C.usToMs(window.durationUs)
            val curTime = currentWindowTimeBarOffsetMs + simpleExoPlayer.contentPosition
            val bufferedPosition =
                currentWindowTimeBarOffsetMs + simpleExoPlayer.contentBufferedPosition
            listener?.setCurTimeString(
                position,
                "" + Util.getStringForTime(formatBuilder, formatter, curTime)
            );
            listener?.setDurationTimeString(
                position,
                "" + Util.getStringForTime(formatBuilder, formatter, durationUs)
            );
            listener?.setBufferedPositionTime(position, bufferedPosition);
            listener?.setCurPositionTime(position, curTime);
            listener?.setDurationTime(position, durationUs);
            handler.removeCallbacks(this)
            val playbackState = simpleExoPlayer.playbackState
            //播放器未开始播放后者播放器播放结束
            if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
                var delayMs = 0L
                //当正在播放状态时
                if (simpleExoPlayer.playWhenReady && playbackState == Player.STATE_READY) {
                    val playBackSpeed = simpleExoPlayer.playbackParameters.speed
                    if (playBackSpeed <= 0.1f) {
                        delayMs = 1000;
                    } else if (playBackSpeed <= 5f) {
                        //中间更新周期时间
                        val mediaTimeUpdatePeriodMs = 1000 / Math.max(
                            1, (1 / playBackSpeed).roundToInt()
                        );
                        //当前进度时间与中间更新周期之间的多出的不足一个中间更新周期时长的时间
                        val surplusTimeMs = curTime % mediaTimeUpdatePeriodMs
                        //播放延迟时间
                        var mediaTimeDelayMs = mediaTimeUpdatePeriodMs - surplusTimeMs
                        if (mediaTimeDelayMs < (mediaTimeUpdatePeriodMs / 5)) {
                            mediaTimeDelayMs += mediaTimeUpdatePeriodMs;
                        }
                        delayMs =
                            if (playBackSpeed.toInt() == 1) mediaTimeDelayMs else (mediaTimeDelayMs / playBackSpeed).toLong()
                        Log.e("AUDIO_CONTROL", "playBackSpeed<=5:$delayMs")
                    } else {
                        delayMs = 200;
                    }
                } else {
                    //当暂停状态时
                    delayMs = 1000;
                }
                handler.postDelayed(this, delayMs);
            } else {
                listener?.isPlay(position, false)
            }
        }
    }


    interface AudioControlListener {
        fun setCurPositionTime(position: Int, curPositionTime: Long)
        fun setDurationTime(position: Int, durationTime: Long)
        fun setBufferedPositionTime(position: Int, bufferedPosition: Long)
        fun setCurTimeString(position: Int, curTimeString: String?)
        fun isPlay(position: Int, isPlay: Boolean)
        fun setDurationTimeString(
            position: Int,
            durationTimeString: String?
        )

        fun onComplete()
    }

}