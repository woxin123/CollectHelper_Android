package online.mengchen.collectionhelper.ui.document

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction
import com.qmuiteam.qmui.recyclerView.QMUISwipeViewHolder
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.domain.model.DocumentInfo
import online.mengchen.collectionhelper.domain.model.DocumentInfo.DocumentType


class DocumentAdapter(private val mViewModel: DocumentViewModel) :
    RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    private var data = mutableListOf<DocumentInfo>()

    private val mDeleteAction: QMUISwipeAction

    init {
        val builder = QMUISwipeAction.ActionBuilder()
            .textSize(QMUIDisplayHelper.sp2px(mViewModel.getApplication(), 14))
            .textColor(Color.WHITE)
            .paddingStartEnd(QMUIDisplayHelper.dp2px(mViewModel.getApplication(), 14))
        mDeleteAction = builder.text("删除").backgroundColor(Color.RED).build()
    }

    fun replace(musics: List<DocumentInfo>) {
        data = mutableListOf(*musics.toTypedArray())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        return DocumentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_document_item,
                parent,
                false
            )
        ).apply { addSwipeAction(mDeleteAction) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val documentInfo = data[position]
        holder.fileName.text = documentInfo.documentName
        holder.categoryName.text = documentInfo.categoryInfo.categoryName
        holder.createTime.text = documentInfo.createTime
        holder.icon.setImageResource(getDocumentIcon(documentInfo.documentType))
        holder.itemView.setOnClickListener {
            mViewModel.clickItem(data[position])
        }
    }

    private fun getDocumentIcon(@DocumentType type: Int): Int {
        return when (type) {
            DocumentInfo.WORD -> {
                R.drawable.word
            }
            DocumentInfo.EXCEL -> {
                R.drawable.excel
            }
            DocumentInfo.PPT -> {
                R.drawable.ppt
            }
            DocumentInfo.PDF -> {
                R.drawable.pdf
            }
            DocumentInfo.OTHER -> {
                R.drawable.document
            }
            else -> {
                R.drawable.document
            }
        }
    }

    class DocumentViewHolder(itemView: View) : QMUISwipeViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val fileName: TextView = itemView.findViewById(R.id.fileName)
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val createTime: TextView = itemView.findViewById(R.id.createTime)
    }
}