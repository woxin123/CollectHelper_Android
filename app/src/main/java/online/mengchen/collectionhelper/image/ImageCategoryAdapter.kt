package online.mengchen.collectionhelper.image

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_book_mark_item.view.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.bookmark.CategoryInfo

class ImageCategoryAdapter(categories: List<CategoryInfo>, private val viewModel: ImageShareViewModel) :
    RecyclerView.Adapter<ImageCategoryAdapter.ImageCategoryViewHolder>() {

    private var data: MutableList<CategoryInfo> = mutableListOf(*categories.toTypedArray())

    fun replaceData(categories: List<CategoryInfo>) {
        setData(categories)
    }

    fun addCategory(categoryInfo: CategoryInfo) {
        data.add(categoryInfo)
        notifyItemInserted(data.size - 1)
    }

    private fun setData(categories: List<CategoryInfo>) {
        this.data = mutableListOf(*categories.toTypedArray())
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCategoryViewHolder {
        return ImageCategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_category_item, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ImageCategoryViewHolder, position: Int) {
        holder.categoryCb.text = data[position].categoryName
        holder.itemView.setOnClickListener {
            val isChecked = holder.categoryCb.isChecked
            if (isChecked) {
                viewModel.checkedCategory(isChecked, data[position])
            }
        }
    }

    class ImageCategoryViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val categoryCb: CheckBox = item.findViewById(R.id.categoryCb)
    }

}

class CategoryRecyclerViewItemDecoration: RecyclerView.ItemDecoration() {
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