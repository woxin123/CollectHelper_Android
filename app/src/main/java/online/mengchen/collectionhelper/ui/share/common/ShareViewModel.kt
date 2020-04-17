package online.mengchen.collectionhelper.ui.share.common

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.Event
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.bookmark.CategoryInfo
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.file.CloudStore
import online.mengchen.collectionhelper.domain.entity.AliyunConfig
import online.mengchen.collectionhelper.domain.entity.FileInfo
import online.mengchen.collectionhelper.repository.AliyunConfigRepository
import online.mengchen.collectionhelper.repository.FileInfoRepository
import online.mengchen.collectionhelper.ui.image.share.ImageShareViewModel
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import retrofit2.HttpException

abstract class ShareViewModel(application: Application): AndroidViewModel(application) {

    lateinit var cloudStore: CloudStore
    private val aliyunConfigRepository: AliyunConfigRepository
    private val fileInfoRepository: FileInfoRepository
    val aliyunConfig: LiveData<AliyunConfig>

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

    protected val _uploadProgress = MutableLiveData<Event<Int>>()
    val uploadProgressEvent: LiveData<Event<Int>>
        get() = _uploadProgress

    private val _selectCategory = MutableLiveData<Event<Pair<Boolean, CategoryInfo>>>()
    val selectCategory: LiveData<Event<Pair<Boolean, CategoryInfo>>>
        get() = _selectCategory

    protected val _startUpload = MutableLiveData<Event<Int>>()
    val startUpload: LiveData<Event<Int>>
        get() = _startUpload


    /**
     * true 上传成功
     * false 上传失败
     */
    protected val _uploadCompleted = MutableLiveData<Event<Boolean>>()
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
                val categoryRes = getCategories()
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
        saveCategory(categoryName)
    }

    internal fun addCategoryEvent(category: CategoryInfo) {
        _addCategory.value = Event(category)
    }

    fun sendMessage(string: Int) {
        _toastText.value = Event(string)
    }

    abstract fun saveCategory(categoryName: String)
    abstract suspend fun getCategories(): ApiResult<List<CategoryInfo>>



    fun checkedCategory(checked: Boolean, category: CategoryInfo) {
        Log.d(ImageShareViewModel.TAG, "选择了分类 ${category.categoryName}")
        _selectCategory.value = Event(Pair(checked, category))
    }

    protected fun saveFileInfo(fileInfo: FileInfo) = viewModelScope.launch {
        fileInfoRepository.insert(fileInfo)
    }
}