package online.mengchen.collectionhelper.bookmark

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookMarkViewModel: ViewModel() {

    val bookMarkCount: ObservableField<String> = ObservableField("0")
    lateinit var bookMarkAdapter: BookMarkAdapter

    /**
     * viewModelScope 默认是在 Main 线程中
     */
    fun loadData() {
        val data = mutableListOf<BookMark>()
        viewModelScope.launch(Dispatchers.IO) {
            BookMarkUtils.parseUrlToBookMark("https://blog.csdn.net/u010356768/article/details/76923080")
                .run { if (this != null) data.add(this) }
            BookMarkUtils.parseUrlToBookMark("https://blog.csdn.net/alice_tl/article/details/75948345")
                .run { if (this != null) data.add(this) }
            Log.d("mengchen", "size = ${data.size}")
            viewModelScope.launch {
                bookMarkAdapter.data = data
                bookMarkCount.set(data.size.toString())
            }
        }
        bookMarkCount.set(data.size.toString())
    }

}