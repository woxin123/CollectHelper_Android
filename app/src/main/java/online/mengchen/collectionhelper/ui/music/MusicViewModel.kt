package online.mengchen.collectionhelper.ui.music

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.base.Event
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.FileType
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.file.CloudStore
import online.mengchen.collectionhelper.data.file.CloudStoreInstance
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.data.repository.AliyunConfigRepository
import online.mengchen.collectionhelper.data.repository.FileInfoRepository
import online.mengchen.collectionhelper.domain.entity.AliyunConfig
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.domain.model.MusicInfo
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import retrofit2.HttpException

class MusicViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    private val fileInfoRepository: FileInfoRepository
    private val categoryService = RetrofitClient.categoryService
    private val audioController: AudioController by lazy { AudioController(getApplication()) }
    val cloudStore: CloudStore = CloudStoreInstance.getCloudStore()
    val cloudStoreType = CloudStoreInstance.getCloudStoreType()

    private val _items = MutableLiveData<List<MusicInfo>>(emptyList())
    val items: LiveData<List<MusicInfo>>
        get() = _items

    private val _musicInfoChangeEvent = MutableLiveData<Event<Int>>()
    val musicInfoChangeEvent: LiveData<Event<Int>>
        get() = _musicInfoChangeEvent

    private val _toastText = MutableLiveData<Event<Int>>()
    val toastMessage: LiveData<Event<Int>>
        get() = _toastText

    private var position = 0
    var curMusicInfo: MusicInfo? = null

    val curMusicName = MutableLiveData<String>()
    val curMusicCategory = MutableLiveData<String>()


    init {
        val db = CollectHelpDatabase.getDatabase(getApplication(), viewModelScope)
        fileInfoRepository = FileInfoRepository(db.fileInfoDao())
//        aliyunConfigRepository = AliyunConfigRepository(db.aliyunConfigDao())
//        aliyunConfig = aliyunConfigRepository.aliyunConfig
    }

    fun start() {
        loadMusics()
    }

    private fun loadMusics() {
        viewModelScope.launch {
            try {
                val categoryRes = categoryService.getCategory(Category.MUSIC, cloudStoreType)
                val musicIfs = fileInfoRepository.getFileInfoByFileTypeAndStoreType(
                    FileType.MUSIC,
                    CloudStoreInstance.getCloudStoreType()
                )
                val categories = categoryRes.data!!
                val map = mutableMapOf<Long, String>()
                categories.forEach {
                    map[it.categoryId] = it.categoryName
                }
                _items.value = musicIfs.map { MusicInfo(it.key, map[it.categoryId] ?: "未分类", cloudStore.getFileUrl(it.key)) }
                if (_items.value!!.isNotEmpty()) {
                    curMusicInfo = _items.value!![0]
                    setCurMusic()
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                HttpExceptionProcess.process(e)
            }
        }
    }

    fun refresh() {
        loadMusics()
    }

    private fun setCurMusic() {
        curMusicName.value = curMusicInfo?.musicName
        curMusicCategory.value = curMusicInfo?.categoryName
    }

    fun changeMusic(position: Int) {
        this.position = position
        _musicInfoChangeEvent.value =
            Event(position)
        val musicUrl = getMusicInfo(position).musicUrl
        curMusicInfo = getMusicInfo(position)
        setCurMusic()
        if (musicUrl == null) {
            sendMessage(R.string.music_play_error)
            return
        }
        audioController.onPrepare(musicUrl)
        audioController.onStart(position)
    }

    fun startMusic() {
        audioController.onStart(position)
    }

    fun pause() {
        audioController.onPause()
    }

    fun next() {
        position++
        if (position == _items.value?.size) {
            position = 0
        }
        changeMusic(position)
    }

    fun setListener(listener: AudioController.AudioControlListener) {
        audioController.listener = listener
    }

    fun getMusicInfo(position: Int): MusicInfo {
        return _items.value?.get(position)!!
    }

    fun sendMessage(messageId: Int) {
        _toastText.value = Event(messageId)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        audioController.release()
    }

}
