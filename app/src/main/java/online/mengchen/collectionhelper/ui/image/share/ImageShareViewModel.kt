package online.mengchen.collectionhelper.ui.image.share

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.Event
import online.mengchen.collectionhelper.bookmark.AddOrUpdateCategory
import online.mengchen.collectionhelper.bookmark.CategoryInfo
import online.mengchen.collectionhelper.common.FileType
import online.mengchen.collectionhelper.common.HTTPStatus
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.file.CloudStore
import online.mengchen.collectionhelper.data.file.CloudStoreCallback
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.domain.entity.AliyunConfig
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.domain.entity.FileInfo
import online.mengchen.collectionhelper.repository.AliyunConfigRepository
import online.mengchen.collectionhelper.repository.FileInfoRepository
import online.mengchen.collectionhelper.utils.FileHelper
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import online.mengchen.collectionhelper.utils.UriHelper
import retrofit2.HttpException
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime

class ImageShareViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "ImageShareViewModel"
    }

    lateinit var cloudStore: CloudStore
    private val aliyunConfigRepository: AliyunConfigRepository
    private val fileInfoRepository: FileInfoRepository
    val aliyunConfig: LiveData<AliyunConfig>
    private val categoryService = RetrofitClient.categoryService

    private val _items = MutableLiveData<List<CategoryInfo>>(emptyList())
    val items: LiveData<List<CategoryInfo>>
        get() = _items

    private val _newCategoryEvent = MutableLiveData<Event<String>>()
    val newCategoryEvent: LiveData<Event<String>>
        get() = _newCategoryEvent

    val newCategory = MutableLiveData<String>()

    private val _showCreateCategory = MutableLiveData<Boolean>(false)
    val showCreateCategory: LiveData<Boolean>
        get() = _showCreateCategory

    private val _toastText = MutableLiveData<Event<Int>>()
    val toastTextMessage: LiveData<Event<Int>>
        get() = _toastText

    private val _addCategory = MutableLiveData<Event<CategoryInfo>>()
    val addCategoryEvent: LiveData<Event<CategoryInfo>>
        get() = _addCategory

    private val _uploadProgress = MutableLiveData<Event<Int>>()
    val uploadProgressEvent: LiveData<Event<Int>>
        get() = _uploadProgress

    private val _selectCategory = MutableLiveData<Event<Pair<Boolean, CategoryInfo>>>()
    val selectCategory: LiveData<Event<Pair<Boolean, CategoryInfo>>>
        get() = _selectCategory

    private val _startUploadImage = MutableLiveData<Event<Unit>>()
    val startUploadImage: LiveData<Event<Unit>>
        get() = _startUploadImage
    /**
     * true 上传成功
     * false 上传失败
     */
    private val _uploadCompleted = MutableLiveData<Event<Boolean>>()
    val uploadCompleted: LiveData<Event<Boolean>>
        get() = _uploadCompleted

    init {
        val db = CollectHelpDatabase.getDatabase(application, viewModelScope)
        val aliyunDao = db.aliyunConfigDao()
        aliyunConfigRepository = AliyunConfigRepository(aliyunDao)
        fileInfoRepository = FileInfoRepository(db.fileInfoDao())
        aliyunConfig = aliyunConfigRepository.aliyunConfig
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

    fun showCreateCategoryView() {
        _showCreateCategory.value = true
    }

    fun hideCreateCategoryView() {
        _showCreateCategory.value = false
    }

    fun addNewCategory() {
        val categoryName = newCategory.value
        if (categoryName == null || categoryName.trim().isEmpty()) {
            _toastText.value = Event(R.string.new_category_empty)
            return
        }
        val addCategory =
            AddOrUpdateCategory(categoryType = Category.IMAGE, categoryName = categoryName)
        addCategory(addCategory)
    }

    private fun addCategory(addCategory: AddOrUpdateCategory) = viewModelScope.launch {
        try {
            val addCategoryRes = categoryService.addCategory(addCategory)
            if (addCategoryRes.status == HTTPStatus.CREATED.code) {
                addCategoryEvent(addCategoryRes.data!!)
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            HttpExceptionProcess.process(e)
        }
    }

    private fun addCategoryEvent(category: CategoryInfo) {
        _addCategory.value = Event(category)
    }

    fun uploadImage(context: Context, imageUri: Uri, categories: List<CategoryInfo>) {
        if (categories.isEmpty()) {
            _toastText.value = Event(R.string.category_is_not_empty)
            return
        }
        val parcFd = context.contentResolver.openFileDescriptor(imageUri, "r")
        val fd = parcFd?.fileDescriptor
        val dir = context.filesDir
        val filePath = UriHelper.getImagePath(imageUri, context)
        if (filePath == null) {
            _toastText.value = Event(R.string.internal_error)
            return
        }
        val fileName = FileHelper.getFileName(filePath)
        if (fileName == null) {
            _toastText.value = Event(R.string.internal_error)
            return
        }
        val file = File(dir, fileName)
        FileHelper.saveFile(FileInputStream(fd!!), file)
        val imagePath = file.absolutePath
        // 开始上传
        _startUploadImage.value = Event(Unit)
        cloudStore.uploadFile("", fileName, imagePath, { progress, _, _ ->
            // java.lang.IllegalStateException: Cannot invoke setValue on a background thread
            viewModelScope.launch(Dispatchers.Main) {
                _uploadProgress.value = Event(progress)
            }
        }, object : CloudStoreCallback {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun <T> onSuccess(t: T) {
                Log.d(TAG, "上传成功")
                viewModelScope.launch(Dispatchers.Main) {
                    _toastText.value = Event(R.string.file_upload_success)
                    _uploadCompleted.value = Event(true)
                    FileHelper.deleteFile(file)
                    categories.forEach {
                        val fileInfo = FileInfo(
                            null,
                            fileName,
                            null,
                            FileType.IMAGE,
                            StoreType.ALIYUN,
                            it.categoryId,
                            1,
                            LocalDateTime.now()
                        )
                        saveImage(fileInfo)
                    }
                }
            }

            override fun onFailed() {
                _toastText.value = Event(R.string.file_upload_fail)
                _uploadCompleted.value = Event(false)
                FileHelper.deleteFile(file)
            }

        })

    }

    fun saveImage(fileInfo: FileInfo) {
        viewModelScope.launch {
            fileInfoRepository.insert(fileInfo)
            val fi = fileInfoRepository.getFileInfoByKey(fileInfo.key)
            if (fi != null) {
                Log.d(TAG, fi.toString())
            } else {
                Log.d(TAG, "卧槽")
            }
        }
    }

    fun checkedCategory(checked: Boolean, category: CategoryInfo) {
        Log.d(TAG, "选择了分类 ${category.categoryName}")
        _selectCategory.value = Event(Pair(checked, category))
    }
}