package online.mengchen.collectionhelper.ui.image

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView


object ImageCategoryListBindings {

    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: List<ImageCategory>) {
        with(recyclerView.adapter as ImageRecyclerViewAdapter) {
            replaceData(items)
        }
    }
}