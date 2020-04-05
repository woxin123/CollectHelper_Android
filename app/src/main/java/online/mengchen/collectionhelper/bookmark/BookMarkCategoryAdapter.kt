package online.mengchen.collectionhelper.bookmark

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.R

class BookMarkCategoryAdapter :
    RecyclerView.Adapter<BookMarkCategoryViewHolder>() {

    var data: MutableList<BookMarkCategory> = mutableListOf()
        set(value)  {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickListener: ((BookMarkCategory) -> Unit)? = null

    fun addBookMarkCategory(bookMarkCategory: BookMarkCategory) {
        data.add(bookMarkCategory)
        notifyItemInserted(data.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkCategoryViewHolder {
        return BookMarkCategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_book_mark_share_category_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BookMarkCategoryViewHolder, position: Int) {
        holder.textView.text = data[position].categoryName
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(data[position])
        }
    }


}


class BookMarkCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.textView)
}

class BookMarkCategoryRecyclerViewItemDecoration: RecyclerView.ItemDecoration() {
    private val space: Int = 15
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) %3==0) {
            outRect.left = 0;
        }
    }
}