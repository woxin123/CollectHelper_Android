package online.mengchen.collectionhelper.ui.bookmark

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.domain.model.BookMarkInfo

class BookMarkAdapter : RecyclerView.Adapter<BookMarkAdapter.BookMarkViewHolder>() {

    var listener: OnItemClickListener? = null

    var data = mutableListOf<BookMarkInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.d("mengchen", "data.size = ${data.size}")
        }

    fun addAll(newData: List<BookMarkInfo>) {
        Log.d("mengchen", newData.toString())
        data.addAll(newData.toTypedArray())
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkViewHolder {
        return BookMarkViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_book_mark_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {
        val bookMark = data[position]
        holder.category.text = bookMark.category.categoryName
        holder.summary.text = bookMark.bookMarkDetail?.summary
        holder.title.text = bookMark.bookMarkDetail?.title
        holder.itemView.setOnClickListener {
            listener?.onClick(bookMark)
        }
    }

    interface OnItemClickListener {
        fun onClick(bookMarkInfo: BookMarkInfo)
    }

    class BookMarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val summary: TextView = itemView.findViewById(R.id.summary)
        val category: TextView = itemView.findViewById(R.id.category)
    }
}