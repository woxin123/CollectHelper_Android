package online.mengchen.collectionhelper.ui.image

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.R

class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.imageView)
    val categoryName: TextView = itemView.findViewById(R.id.category)
}