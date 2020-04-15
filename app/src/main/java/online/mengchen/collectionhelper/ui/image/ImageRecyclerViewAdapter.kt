package online.mengchen.collectionhelper.ui.image

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import online.mengchen.collectionhelper.R


class ImageRecyclerViewAdapter(var data: MutableList<ImageCategory>, private val viewModel: ImageViewModel): RecyclerView.Adapter<ImageViewHolder>() {


    fun replaceData(imageCategories: List<ImageCategory>) {
        data = mutableListOf(*imageCategories.toTypedArray())
        notifyDataSetChanged()
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
        holder.itemView.setOnClickListener {
            viewModel.openImageCategory(data[position])
        }
    }

}