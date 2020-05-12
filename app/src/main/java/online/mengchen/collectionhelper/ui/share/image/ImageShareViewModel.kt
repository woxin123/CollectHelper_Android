package online.mengchen.collectionhelper.ui.share.image

import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.CollectHelperApplication
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.base.Event
import online.mengchen.collectionhelper.domain.model.AddOrUpdateCategory
import online.mengchen.collectionhelper.domain.model.CategoryInfo
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.FileType
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.file.CloudStoreCallback
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.domain.entity.FileInfo
import online.mengchen.collectionhelper.data.repository.FileInfoRepository
import online.mengchen.collectionhelper.ui.share.common.ShareViewModel
import online.mengchen.collectionhelper.utils.FileHelper
import online.mengchen.collectionhelper.utils.LoginUtils
import online.mengchen.collectionhelper.utils.UriHelper
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime

class ImageShareViewModel(application: Application) : ShareViewModel (application) {

    companion object {
        const val TAG = "ImageShareViewModel"
    }
//    private val aliyunConfigRepository: AliyunConfigRepository
    private val fileInfoRepository: FileInfoRepository
//    val aliyunConfig: LiveData<AliyunConfig?>
    private val categoryService = RetrofitClient.categoryService


    init {
        val db = CollectHelpDatabase.getDatabase(application, viewModelScope)
        val aliyunDao = db.aliyunConfigDao()
//        aliyunConfigRepository = AliyunConfigRepository(aliyunDao)
        fileInfoRepository = FileInfoRepository(db.fileInfoDao())
//        aliyunConfig = aliyunConfigRepository.aliyunConfig
    }



    override suspend fun getCategories(): ApiResult<List<CategoryInfo>> {
        return categoryService.getCategory(Category.IMAGE, cloudStoreType)
    }

    override fun getAddOrUpdateCategory(categoryName: String): AddOrUpdateCategory {
        return AddOrUpdateCategory(
            categoryType = Category.IMAGE,
            categoryName = categoryName,
            storeType = cloudStoreType
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveImages(uris: List<Uri>, categories: MutableList<CategoryInfo>) {
        if (uris.isEmpty()) {
            sendMessage(R.string.share_content_empty)
            return
        }
        // 如果没有分类，就构造一个未分类
        if (categories.isEmpty()) {
            categories.add(CategoryInfo.unCategorized(Category.IMAGE))
        }
        _startUpload.value =
            Event(uris.size * 100)
        val uploadTasks = Array(uris.size) { UploadTask() }
        viewModelScope.launch {
            for (i in uris.indices) {
                // 获取文件名
                val filePath = UriHelper.getImagePath(uris[i], getApplication())
                if (filePath == null) {
                    sendMessage(R.string.get_file_path_error)
                }
                val fileName: String = if (filePath != null) {
                    FileHelper.getFileName(filePath)!!
                } else {
                    System.currentTimeMillis().toString() + ".jpg"
                }
                // 将文件保存到本地
                val application = getApplication<CollectHelperApplication>()
                val dir = application.filesDir
                val file = File(dir, fileName)
                val fd =
                    application.contentResolver.openFileDescriptor(uris[i], "r")?.fileDescriptor
                FileHelper.saveFile(FileInputStream(fd!!), file)
                cloudStore.uploadFile(
                    "",
                    fileName,
                    file.absolutePath,
                    FileType.IMAGE,
                    progressListener = { progress: Int, current: Long, total: Long ->
                        uploadTasks[i].progress = progress
                        updateProgressDialog(uploadTasks)
                    }, callback = object : CloudStoreCallback {
                        override fun <T> onSuccess(t: T) {
                            file.delete()
                            uploadTasks[i].succeed = true
                            val key = if (t is String) {
                                t
                            } else {
                                fileName
                            }
                            categories.forEach { categoryInfo ->
                                saveFileInfo(
                                    FileInfo(
                                        null,
                                        key,
                                        null,
                                        FileType.IMAGE,
                                        cloudStoreType,
                                        categoryInfo.categoryId,
                                        LoginUtils.user?.userId!!,
                                        LocalDateTime.now()
                                    )
                                )
                            }
                            checkComplete(uploadTasks)
                        }

                        override fun onFailed() {
                            file.delete()
                            uploadTasks[i].failed
                            viewModelScope.launch {
                                sendMessage(R.string.file_upload_fail)
                            }
                            checkComplete(uploadTasks)
                        }

                    })
            }
        }
    }

    private fun updateProgressDialog(tasks: Array<UploadTask>) {
        var progress = 0
        tasks.forEach {
            progress += it.progress
        }
        viewModelScope.launch(Dispatchers.Main) {
            _uploadProgress.value =
                Event(progress)
        }
    }

    private fun checkComplete(task: Array<UploadTask>) {
        var completed = false
        task.forEach {
            completed = it.complete
        }
        viewModelScope.launch {
            if (completed) {
                _uploadCompleted.value =
                    Event(true)
            }
        }
    }
}