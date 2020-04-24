package online.mengchen.collectionhelper.ui.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tencent.smtt.sdk.TbsVideo
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.domain.model.VideoInfo

class VideoAdapter(private val mViewModel: VideoViewModel) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var data = mutableListOf<VideoInfo>()

    fun replace(videos: List<VideoInfo>) {
        data = mutableListOf(*videos.toTypedArray())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_video_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoInfo = data[position]
        holder.videoName.text = videoInfo.videoName
        holder.categoryName.text = videoInfo.categoryInfo.categoryName
        holder.createTime.text = videoInfo.createTime
        holder.itemView.setOnClickListener {
            TbsVideo.openVideo(mViewModel.getApplication(), videoInfo.videoUrl)
        }
    }


    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoName: TextView = itemView.findViewById(R.id.videoName)
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val createTime: TextView = itemView.findViewById(R.id.createTime)
    }
}