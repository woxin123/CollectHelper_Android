package online.mengchen.collectionhelper.ui.share.video

import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.CollectHelperApplication
import online.mengchen.collectionhelper.base.Event
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.bookmark.AddOrUpdateCategory
import online.mengchen.collectionhelper.bookmark.CategoryInfo
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.FileType
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.file.CloudStoreCallback
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.domain.entity.FileInfo
import online.mengchen.collectionhelper.ui.share.common.ShareViewModel
import online.mengchen.collectionhelper.utils.FileHelper
import online.mengchen.collectionhelper.utils.LoginUtils
import online.mengchen.collectionhelper.utils.UriHelper
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime

class VideoShareVideoModel(application: Application) : ShareViewModel(application) {

    private val categoryService = RetrofitClient.categoryService

    override suspend fun getCategories(): ApiResult<List<CategoryInfo>> {
        return  categoryService.getVideoCategory()
    }

    override fun getAddOrUpdateCategory(categoryName: String): AddOrUpdateCategory {
        return AddOrUpdateCategory(categoryType = Category.VIDEO, categoryName = categoryName)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveVideo(uris: List<Uri>, categories: MutableList<CategoryInfo>) {
        if (uris.isEmpty()) {
            sendMessage(R.string.share_content_empty)
            return
        }
        // 如果没有分类，就构造一个未分类
        if (categories.isEmpty()) {
            categories.add(CategoryInfo.unCategorized(Category.MUSIC))
        }
        _startUpload.value =
            Event(uris.size * 100)
        val uploadTasks = Array(uris.size) { UploadTask() }
        viewModelScope.launch {
            for (i in uris.indices) {
                // 获取文件名
                val filePath = UriHelper.getMusicPath(uris[i], getApplication())
                if (filePath == null) {
                    sendMessage(R.string.get_file_path_error)
                }
                val fileName: String = if (filePath != null) {
                    FileHelper.getFileName(filePath)!!
                } else {
                    System.currentTimeMillis().toString() + ".mp3"
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
                    progressListener = { progress: Int, current: Long, total: Long ->
                        uploadTasks[i].progress = progress
                        updateProgressDialog(uploadTasks)
                    }, callback = object: CloudStoreCallback {
                        override fun <T> onSuccess(t: T) {
                            file.delete()
                            uploadTasks[i].succeed = true
                            categories.forEach {categoryInfo ->
                                saveFileInfo(
                                    FileInfo(
                                        null,
                                        fileName,
                                        null,
                                        FileType.VIDEO,
                                        StoreType.ALIYUN,
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