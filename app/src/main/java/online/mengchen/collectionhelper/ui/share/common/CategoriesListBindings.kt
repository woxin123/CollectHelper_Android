package online.mengchen.collectionhelper.ui.share.common

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.domain.model.CategoryInfo

object CategoriesListBindings {

    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: List<CategoryInfo>) {
        with(recyclerView.adapter as CategoryAdapter) {
            replaceData(items)
        }
    }
}