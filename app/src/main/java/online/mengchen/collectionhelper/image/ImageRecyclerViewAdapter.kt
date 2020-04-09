package online.mengchen.collectionhelper.image

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.R

class ImageRecyclerViewAdapter(val data: MutableList<ImageCategory>, private val context: Context): RecyclerView.Adapter<ImageViewHolder>() {


    init {
        data.apply {
            for (i in 0..10) {
                add(ImageCategory().apply { cover = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher) })
            }
        }
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageImageViewHolder = holder as ImageImageViewHolder
        val category = data[position]
        imageImageViewHolder.categoryName.text = category.name
    }

}