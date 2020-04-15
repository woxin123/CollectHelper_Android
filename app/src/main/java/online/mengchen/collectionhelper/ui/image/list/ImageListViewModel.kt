package online.mengchen.collectionhelper.ui.image.list

import android.app.Application
import android.telephony.mbms.FileInfo
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.file.CloudStore
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.domain.entity.AliyunConfig
import online.mengchen.collectionhelper.repository.AliyunConfigRepository
import online.mengchen.collectionhelper.repository.FileInfoRepository
import online.mengchen.collectionhelper.ui.image.ImageCategory

class ImageListViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "ImageListViewModel"
    }

    private val fileInfoRepository: FileInfoRepository
    lateinit var imageCategory: ImageCategory
    lateinit var cloudStore: CloudStore
    private val aliyunConfigRepository: AliyunConfigRepository
    val aliyunConfig: LiveData<AliyunConfig>

    init {
        val db = CollectHelpDatabase.getDatabase(application, viewModelScope)
        fileInfoRepository = FileInfoRepository(db.fileInfoDao())
        val aliyunDao = db.aliyunConfigDao()
        aliyunConfigRepository = AliyunConfigRepository(aliyunDao)
        aliyunConfig = aliyunConfigRepository.aliyunConfig
    }

    private val _items = MutableLiveData<List<String>>(emptyList())
    val items: LiveData<List<String>>
        get() = _items

    fun start() {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
            val fileIfs = fileInfoRepository.getFileInfoByCategoryId(imageCategory.categoryId, StoreType.ALIYUN)
            Log.d(TAG, fileIfs.toString())
            val urlList = fileIfs.map { cloudStore.getFileUrl(it.key)!! }
            _items.value = urlList
        }
    }

}