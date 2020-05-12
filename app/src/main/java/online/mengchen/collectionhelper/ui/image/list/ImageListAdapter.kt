package online.mengchen.collectionhelper.ui.image.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_image_lsit.view.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.ui.image.ImageViewHolder

class ImageListAdapter(private var data: List<String>, private val context: Context, private val viewModel: ImageListViewModel) :
    RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder>() {

    fun replaceData(urlList: List<String>) {
        data = mutableListOf(*urlList.toTypedArray())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListViewHolder {
        return ImageListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_image_lsit,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        Glide.with(context).load(data[position])
            .error(R.mipmap.ic_launcher).into(holder.itemView.imageView)
        holder.itemView.setOnClickListener {
            viewModel.clickItem(holder.adapterPosition)
        }
    }

    class ImageListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}