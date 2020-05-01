package online.mengchen.collectionhelper.ui.image

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.base.Event
import online.mengchen.collectionhelper.data.network.RetrofitClient
import retrofit2.HttpException

class ImageViewModel : ViewModel() {

    companion object {
        private const val TAG = "ImageViewModel"
    }

    private val _items = MutableLiveData<List<ImageCategory>>(emptyList())
    val items: LiveData<List<ImageCategory>>
        get() = _items

    private val _openImageCategory = MutableLiveData<Event<ImageCategory>>()
    val openImageCategory: LiveData<Event<ImageCategory>>
        get() = _openImageCategory

    private val categoryService = RetrofitClient.categoryService

    init {
        start()
    }

    private fun start() {
        getImageCategory()
    }

    private fun getImageCategory() {
        viewModelScope.launch {
            try {
                val categoriesRes = categoryService.getImageCategory()
                Log.d(TAG, categoriesRes.data.toString())
                _items.value = categoriesRes.data!!.map { ImageCategory(
                    categoryId = it.categoryId,
                    name = it.categoryName) }
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }
    }

    fun openImageCategory(imageCategory: ImageCategory) {
        _openImageCategory.value =
            Event(imageCategory)
    }

}