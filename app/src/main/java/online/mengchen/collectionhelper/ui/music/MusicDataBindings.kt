package online.mengchen.collectionhelper.ui.music

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.domain.model.MusicInfo

object MusicDataBindings {

    @JvmStatic
    @BindingAdapter("app:music_items")
    fun setItems(recyclerView: RecyclerView, musics: List<MusicInfo>) {
        with(recyclerView.adapter as MusicAdapter) {
            replace(musics)
        }
    }
}