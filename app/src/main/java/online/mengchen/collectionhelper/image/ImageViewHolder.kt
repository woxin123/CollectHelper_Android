package online.mengchen.collectionhelper.image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.R

open class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

}

open class TitleImageViewHolder(var itemView: View): ImageViewHolder(itemView) {
//    init {
//        itemView = LayoutInflater.from()
//    }
}


class ImageImageViewHolder(itemView: View): ImageViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.imageView)
    val categoryName: TextView = itemView.findViewById(R.id.category)
    val count: TextView = itemView.findViewById(R.id.count)
}