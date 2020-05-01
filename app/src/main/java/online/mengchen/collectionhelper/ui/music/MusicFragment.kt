package online.mengchen.collectionhelper.ui.music

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.music_fragment.*

import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.data.file.CloudStoreInstance
import online.mengchen.collectionhelper.data.file.aliyun.AliyunConfiguration
import online.mengchen.collectionhelper.databinding.MusicFragmentBinding

class MusicFragment : Fragment() {

    companion object {
        const val TAG = "MusicFragment"
        fun newInstance() = MusicFragment()
    }

    private lateinit var mViewModel: MusicViewModel
    private lateinit var mBinding: MusicFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.music_fragment, container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(MusicViewModel::class.java)
        mBinding.viewModel = mViewModel
        lifecycle.addObserver(mViewModel)
        initView()
        initObserver()
        initListener()
        mViewModel.start()
    }


    private fun initView() {
        recyclerView.adapter = MusicAdapter(mViewModel)
        recyclerView.layoutManager =
            LinearLayoutManager(this.activity, RecyclerView.VERTICAL, false)
        playBar.elevation = 2F
        playBar.alpha = 1F
        musicName.text = ""
        musicCategory.text = ""
    }

    private fun initListener() {
        mViewModel.setListener(object : AudioController.AudioControlListener {
            override fun setCurPositionTime(position: Int, curPositionTime: Long) {
                musicProgress?.setProgress(curPositionTime)
            }

            override fun setDurationTime(position: Int, durationTime: Long) {
                musicProgress?.maxValue = durationTime
            }

            override fun setBufferedPositionTime(position: Int, bufferedPosition: Long) {
            }

            override fun setCurTimeString(position: Int, curTimeString: String?) {
                curTime?.text = curTimeString
            }

            override fun isPlay(position: Int, isPlay: Boolean) {
                if (isPlay) {
                    musicProgress?.isChecked = isPlay
                }
            }

            override fun setDurationTimeString(position: Int, durationTimeString: String?) {
            }

            override fun onComplete() {
                mViewModel.next()
            }

        })
        musicProgress.setOnClickListener {
            val checked = musicProgress.isChecked
            Log.d(TAG, "checked = $checked")
            if (checked) {
                mViewModel.startMusic()
            } else {
                mViewModel.pause()
            }
        }
    }

    private fun initObserver() {
//        mViewModel.aliyunConfig.observe(this.viewLifecycleOwner, Observer {
//            if (it == null) {
//                mViewModel.sendMessage(R.string.cloud_store_config_get_error)
//            } else {
//                CloudStoreInstance.getAliyunInstance(
//                    AliyunConfiguration(it.accessKey, it.secretKey, it.bucket),
//                    mViewModel.viewModelScope
//                ).observe(this.viewLifecycleOwner, Observer {
//                    if (it == null) {
//                        mViewModel.sendMessage(R.string.cloud_store_init_error)
//                    } else {
//                        mViewModel.cloudStore = it
//                        mViewModel.start()
//                    }
//                })
//            }
//        })
        mViewModel.musicInfoChangeEvent.observe(this.viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                val musicInfo = mViewModel.getMusicInfo(it)
                Log.d(TAG, musicInfo.toString())
                musicName.text = musicInfo.musicName
                musicCategory.text = musicInfo.categoryName
            }
        })
        mViewModel.toastMessage.observe(this.viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(this.activity, resources.getString(it), Toast.LENGTH_SHORT).show()
            }
        })
    }


//    override fun onResume() {
//        super.onResume()
//        Log.d(TAG, "onResume")
//        if (mViewModel.curMusicInfo != null) {
//            musicName.text = mViewModel.curMusicInfo?.musicName
//            musicCategory.text = mViewModel.curMusicInfo?.categoryName
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.unbind()
    }

}
