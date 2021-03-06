package online.mengchen.collectionhelper.ui.image.list

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.CollectHelperApplication
import online.mengchen.collectionhelper.base.SingleLiveEvent
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.file.CloudStore
import online.mengchen.collectionhelper.data.file.CloudStoreInstance
import online.mengchen.collectionhelper.domain.entity.AliyunConfig
import online.mengchen.collectionhelper.data.repository.AliyunConfigRepository
import online.mengchen.collectionhelper.data.repository.FileInfoRepository
import online.mengchen.collectionhelper.ui.image.ImageCategory

class ImageListViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "ImageListViewModel"
    }

    private val fileInfoRepository: FileInfoRepository
    lateinit var imageCategory: ImageCategory
    val cloudStore: CloudStore = CloudStoreInstance.getCloudStore()
//    private val aliyunConfigRepository: AliyunConfigRepository
//    val aliyunConfig: LiveData<AliyunConfig?>

    init {
        val db = CollectHelpDatabase.getDatabase(application, viewModelScope)
        fileInfoRepository = FileInfoRepository(db.fileInfoDao())
        val aliyunDao = db.aliyunConfigDao()
//        aliyunConfigRepository = AliyunConfigRepository(aliyunDao)
//        aliyunConfig = aliyunConfigRepository.aliyunConfig
    }

    private val _items = MutableLiveData<List<String>>(emptyList())
    val items: LiveData<List<String>>
        get() = _items

    /**
     * 点击 recyclerview
     */
    private val _clickItem = SingleLiveEvent<Int>()
    val clickItem: LiveData<Int>
        get() = _clickItem

    fun start() {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
            val fileIfs = fileInfoRepository.getFileInfoByCategoryId(imageCategory.categoryId, CloudStoreInstance.getCloudStoreType())
            Log.d(TAG, fileIfs.toString())
            val urlList = fileIfs.map { cloudStore.getFileUrl(it.key)!! }
            _items.value = urlList
        }
    }

    fun clickItem(position: Int) {
        _clickItem.setValue(position)
    }

}