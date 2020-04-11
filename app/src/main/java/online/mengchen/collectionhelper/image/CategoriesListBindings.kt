package online.mengchen.collectionhelper.image

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.bookmark.CategoryInfo

object CategoriesListBindings {

    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: List<CategoryInfo>) {
        with(recyclerView.adapter as ImageCategoryAdapter) {
            replaceData(items)
        }
    }
}