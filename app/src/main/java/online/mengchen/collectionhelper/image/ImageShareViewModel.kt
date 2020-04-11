package online.mengchen.collectionhelper.image

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.bookmark.CategoryInfo
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.file.CloudStore
import online.mengchen.collectionhelper.data.file.aliyun.AliyunCloudStore
import online.mengchen.collectionhelper.data.file.aliyun.AliyunConfiguration
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.domain.entity.AliyunConfig
import online.mengchen.collectionhelper.repository.AliyunConfigRepository
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import retrofit2.HttpException

class ImageShareViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "ImageShareViewModel"
    }

    lateinit var cloudStore: CloudStore
    private val aliyunConfigRepository: AliyunConfigRepository
    val aliyunConfig: LiveData<AliyunConfig>
    private val categoryService = RetrofitClient.categoryService

    private val _items = MutableLiveData<List<CategoryInfo>>(emptyList())
    val items: LiveData<List<CategoryInfo>>
        get() = _items
    init {
        val aliyunDao =
            CollectHelpDatabase.getDatabase(application, viewModelScope).aliyunConfigDao()
        aliyunConfigRepository = AliyunConfigRepository(aliyunDao)
        aliyunConfig = aliyunConfigRepository.aliyunConfig
    }

    fun addImage() {

    }

    fun loadCategories() {
        // 从网上获取
        viewModelScope.launch {
            try {
                val categoryRes = categoryService.getImageCategory()
                _items.value = categoryRes.data
            } catch (e: HttpException) {
                e.printStackTrace()
                HttpExceptionProcess.process(e)
            }
        }
    }

}