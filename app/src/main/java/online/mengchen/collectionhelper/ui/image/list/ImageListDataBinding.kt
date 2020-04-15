package online.mengchen.collectionhelper.ui.image.list

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object ImageListDataBinding {

    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: List<String>) {
        with(recyclerView.adapter as ImageListAdapter) {
            replaceData(items)
        }
    }
}