package online.mengchen.collectionhelper.ui.document

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.domain.model.DocumentInfo
import online.mengchen.collectionhelper.domain.model.MusicInfo

object DocumentDataBindings {

    @JvmStatic
    @BindingAdapter("app:document_items")
    fun setItems(recyclerView: RecyclerView, docs: List<DocumentInfo>) {
        with(recyclerView.adapter as DocumentAdapter) {
            replace(docs)
        }
    }
}