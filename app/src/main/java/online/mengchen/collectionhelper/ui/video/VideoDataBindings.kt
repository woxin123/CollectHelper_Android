package online.mengchen.collectionhelper.ui.video

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.domain.model.DocumentInfo
import online.mengchen.collectionhelper.domain.model.MusicInfo
import online.mengchen.collectionhelper.domain.model.VideoInfo

object VideoDataBindings {

    @JvmStatic
    @BindingAdapter("app:video_items")
    fun setItems(recyclerView: RecyclerView, docs: List<VideoInfo>) {
        with(recyclerView.adapter as VideoAdapter) {
            replace(docs)
        }
    }
}