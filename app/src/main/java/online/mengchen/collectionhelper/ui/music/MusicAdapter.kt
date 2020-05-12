package online.mengchen.collectionhelper.ui.music

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction
import com.qmuiteam.qmui.recyclerView.QMUISwipeViewHolder
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.domain.model.MusicInfo
import online.mengchen.collectionhelper.ui.music.MusicAdapter.*

class MusicAdapter(private val mViewModel: MusicViewModel) :
    RecyclerView.Adapter<MusicViewHolder>() {

    private var data = mutableListOf<MusicInfo>()

    private val mDeleteAction: QMUISwipeAction

    init {
        val builder = QMUISwipeAction.ActionBuilder()
            .textSize(QMUIDisplayHelper.sp2px(mViewModel.getApplication(), 14))
            .textColor(Color.WHITE)
            .paddingStartEnd(QMUIDisplayHelper.dp2px(mViewModel.getApplication(), 14))
        mDeleteAction = builder.text("删除").backgroundColor(Color.RED).build()
    }

    fun replace(musics: List<MusicInfo>) {
        data = mutableListOf(*musics.toTypedArray())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val musicViewHolder = MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_music_item,
                parent,
                false
            )
        )
        musicViewHolder.addSwipeAction(mDeleteAction)
        return musicViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.musicName.text = data[position].musicName
        holder.musicCategory.text = data[position].categoryName
        holder.itemView.setOnClickListener {
            mViewModel.changeMusic(holder.adapterPosition)
        }
    }

    class MusicViewHolder(itemView: View) : QMUISwipeViewHolder(itemView) {
        val musicName: TextView = itemView.findViewById(R.id.musicName)
        val musicCategory: TextView = itemView.findViewById(R.id.musicCategory)
        val more: ImageView = itemView.findViewById(R.id.more)
    }
}