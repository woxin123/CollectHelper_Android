package online.mengchen.collectionhelper.ui.video

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.base.Event
import online.mengchen.collectionhelper.domain.model.CategoryInfo
import online.mengchen.collectionhelper.common.FileType
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.file.CloudStore
import online.mengchen.collectionhelper.data.file.CloudStoreInstance
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.data.repository.FileInfoRepository
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.domain.model.VideoInfo
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import retrofit2.HttpException
import java.time.format.DateTimeFormatter

class VideoViewModel(application: Application) : AndroidViewModel(application) {

    private var categories: List<CategoryInfo>? = null
    private val categoryService = RetrofitClient.categoryService
//    private val aliyunConfigRepository: AliyunConfigRepository
    private val fileInfoRepository: FileInfoRepository
    val cloudStore: CloudStore = CloudStoreInstance.getCloudStore()
    val cloudStoreType = CloudStoreInstance.getCloudStoreType()
//    val aliyunConfig: LiveData<AliyunConfig?>

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ")

    init {
        val db = CollectHelpDatabase.getDatabase(getApplication(), viewModelScope)
//        aliyunConfigRepository = AliyunConfigRepository(db.aliyunConfigDao())
        fileInfoRepository = FileInfoRepository(db.fileInfoDao())
//        aliyunConfig = aliyunConfigRepository.aliyunConfig
    }

    private val _item = MutableLiveData<List<VideoInfo>>(emptyList())
    val item: LiveData<List<VideoInfo>>
        get() = _item

    private val _toastText = MutableLiveData<Event<Int>>()
    val toastMessage: LiveData<Event<Int>>
        get() = _toastText

    fun sendMessage(messageId: Int) {
        _toastText.value = Event(messageId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun start() {
        loadData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refresh() {
        loadData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData() {
        viewModelScope.launch {
            val videos = fileInfoRepository.getFileInfoByFileTypeAndStoreType(
                FileType.VIDEO,
                CloudStoreInstance.getCloudStoreType()
            )
            try {
                val categoriesRes = categoryService.getCategory(Category.VIDEO, cloudStoreType)
                categories = categoriesRes.data
            } catch (e: HttpException) {
                e.printStackTrace()
                HttpExceptionProcess.process(e)
                return@launch
            }
            val map = mutableMapOf<Long, CategoryInfo>()
            for (categoryIf in categories!!) {
                map[categoryIf.categoryId] = categoryIf
            }
            val videoIfs = videos.map {
                VideoInfo(
                    it.key,
                    map[it.categoryId] ?: CategoryInfo.unCategorized(Category.DOCUMENT),
                    cloudStore.getFileUrl(it.key) ?: "",
                    formatter.format(it.createTime)

                )
            }
            _item.value = videoIfs
        }
    }
}