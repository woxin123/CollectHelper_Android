package online.mengchen.collectionhelper.ui.image

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.bookmark.CategoryInfo
import online.mengchen.collectionhelper.ui.image.share.ImageCategoryAdapter


object ImageCategoryListBindings {

    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: List<ImageCategory>) {
        with(recyclerView.adapter as ImageRecyclerViewAdapter) {
            replaceData(items)
        }
    }
}