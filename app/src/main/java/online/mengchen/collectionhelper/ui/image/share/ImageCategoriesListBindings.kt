package online.mengchen.collectionhelper.ui.image.share

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.bookmark.CategoryInfo
import online.mengchen.collectionhelper.ui.image.share.ImageCategoryAdapter

object ImageCategoriesListBindings {

    @BindingAdapter("app:image_share_items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: List<CategoryInfo>) {
        with(recyclerView.adapter as ImageCategoryAdapter) {
            replaceData(items)
        }
    }
}