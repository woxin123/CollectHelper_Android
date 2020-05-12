package online.mengchen.collectionhelper.ui.document

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.CollectHelperApplication
import online.mengchen.collectionhelper.base.Event
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.domain.model.CategoryInfo
import online.mengchen.collectionhelper.common.FileType
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.file.*
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.data.repository.FileInfoRepository
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.domain.model.DocumentInfo
import online.mengchen.collectionhelper.utils.FileHelper
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import retrofit2.HttpException
import java.io.File
import java.time.format.DateTimeFormatter

class DocumentViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "DocumentViewModel"
    }

    private var categories: List<CategoryInfo>? = null
    private val categoryService = RetrofitClient.categoryService

    //    private val aliyunConfigRepository: AliyunConfigRepository
    private val fileInfoRepository: FileInfoRepository
    private val _clickItem = MutableLiveData<Event<DocumentInfo>>()
    val clickItem: LiveData<Event<DocumentInfo>>
        get() = _clickItem
    val cloudStore: CloudStore = CloudStoreInstance.getCloudStore()
    val cloudStoreType: Int = CloudStoreInstance.getCloudStoreType()
//    val aliyunConfig: LiveData<AliyunConfig?>

    private val _updateProgress = MutableLiveData<Int>()
    val updateProgress: LiveData<Int>
        get() = _updateProgress

    private val _showProgress = MutableLiveData<Event<Unit>>()
    val showProgress: LiveData<Event<Unit>>
        get() = _showProgress

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ")

    init {
        val db = CollectHelpDatabase.getDatabase(getApplication(), viewModelScope)
//        aliyunConfigRepository = AliyunConfigRepository(db.aliyunConfigDao())
        fileInfoRepository = FileInfoRepository(db.fileInfoDao())
//        aliyunConfig = aliyunConfigRepository.aliyunConfig
    }

    private val _item = MutableLiveData<List<DocumentInfo>>(emptyList())
    val item: LiveData<List<DocumentInfo>>
        get() = _item

    private val _toastText = MutableLiveData<Event<Int>>()
    val toastMessage: LiveData<Event<Int>>
        get() = _toastText


    @RequiresApi(Build.VERSION_CODES.O)
    fun start() {
        loadData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData() {
        viewModelScope.launch {
            val docs = fileInfoRepository.getFileInfoByFileTypeAndStoreType(
                FileType.DOCUMENT,
                CloudStoreInstance.getCloudStoreType()
            )
            try {
                val categoriesRes = categoryService.getCategory(Category.DOCUMENT, cloudStoreType)
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
            val documentIfs = docs.map {
                DocumentInfo(
                    FileHelper.getFileName(it.key) ?: it.key,
                    it.key,
                    map[it.categoryId] ?: CategoryInfo.unCategorized(Category.DOCUMENT),
                    formatter.format(it.createTime),
                    cloudStore.getFileUrl(it.key) ?: "",
                    getDocumentType(it.key)
                )
            }
            _item.value = documentIfs
        }
    }

    fun sendMessage(messageId: Int) {
        _toastText.value = Event(messageId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refresh() {
        loadData()
    }

    private fun getDocumentType(fileName: String): Int {
        val suffix = FileHelper.getFileSuffix(fileName)
        return when {
            suffix == null -> DocumentInfo.OTHER
            suffix.startsWith("doc") -> DocumentInfo.WORD
            suffix.startsWith("xls") -> DocumentInfo.EXCEL
            suffix.startsWith("ppt") -> DocumentInfo.PPT
            suffix.startsWith("pdf") -> DocumentInfo.PDF
            else -> DocumentInfo.OTHER
        }
    }

    fun clickItem(documentInfo: DocumentInfo) {
        _showProgress.value = Event(Unit)
        val file =
            File(getApplication<CollectHelperApplication>().filesDir, documentInfo.documentName)
        Log.d(TAG, file.absolutePath)
        cloudStore.downloadFile(
            "",
            documentInfo.key,
            file.absolutePath,
            object : CloudStoreCallback {
                override fun <T> onSuccess(t: T) {
                    documentInfo.filePath = file.absolutePath
                    viewModelScope.launch(Dispatchers.Main) {
                        _clickItem.value =
                            Event(
                                documentInfo
                            )

                    }
                }

                override fun onFailed() {
                    viewModelScope.launch(Dispatchers.Main) {
                        sendMessage(R.string.file_download_error)
                    }
                }

            },
            object : CloudStoreProgressListener {
                override fun progressChange(progress: Int, currentBytes: Long?, totalBytes: Long?) {
                    viewModelScope.launch(Dispatchers.Main) {
                        _updateProgress.value = progress
                    }
                }

            },
            false
        )


    }
}