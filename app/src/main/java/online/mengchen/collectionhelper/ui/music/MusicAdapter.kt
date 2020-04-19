package online.mengchen.collectionhelper.ui.music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.domain.model.MusicInfo
import online.mengchen.collectionhelper.ui.music.MusicAdapter.*

class MusicAdapter :
    RecyclerView.Adapter<MusicViewHolder>() {

    private var data = mutableListOf<MusicInfo>()

    fun replace(musics: List<MusicInfo>) {
        data = mutableListOf(*musics.toTypedArray())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_music_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.musicName.text = data[position].musicName
        holder.musicCategory.text = data[position].categoryName
    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musicName: TextView = itemView.findViewById(R.id.musicName)
        val musicCategory: TextView = itemView.findViewById(R.id.musicCategory)
        val start: ImageView = itemView.findViewById(R.id.start)
        val more: ImageView = itemView.findViewById(R.id.more)
    }
}