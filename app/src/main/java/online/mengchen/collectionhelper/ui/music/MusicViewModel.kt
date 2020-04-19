package online.mengchen.collectionhelper.ui.music

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.common.FileType
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.data.repository.FileInfoRepository
import online.mengchen.collectionhelper.domain.model.MusicInfo
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import retrofit2.HttpException

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val fileInfoRepository: FileInfoRepository
    private val categoryService = RetrofitClient.categoryService

    private val _items = MutableLiveData<List<MusicInfo>>(emptyList())
    val items: LiveData<List<MusicInfo>>
        get() = _items


    init {
        val db = CollectHelpDatabase.getDatabase(getApplication(), viewModelScope)
        fileInfoRepository = FileInfoRepository(db.fileInfoDao())
    }

    fun start() {
        loadMusics()
    }

    private fun loadMusics() {
        viewModelScope.launch {
            try {
                val categoryRes = categoryService.getMusicCategory()
                val musicIfs = fileInfoRepository.getFileInfoByFileTypeAndStoreType(
                    FileType.MUSIC,
                    StoreType.ALIYUN
                )
                val categories = categoryRes.data!!
                val map = mutableMapOf<Long, String>()
                categories.forEach {
                    map[it.categoryId] = it.categoryName
                }
                _items.value = musicIfs.map { MusicInfo(it.key, map[it.categoryId] ?: "未分类") }
            } catch (e: HttpException) {
                e.printStackTrace()
                HttpExceptionProcess.process(e)
            }

        }
    }

}
