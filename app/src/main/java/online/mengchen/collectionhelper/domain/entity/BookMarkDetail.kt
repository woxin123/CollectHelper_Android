package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import online.mengchen.collectionhelper.domain.model.BookMarkDetailInfo

@Entity(tableName = "book_mark_detail")
class BookMarkDetail (
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "bid") val bid: Long
) {
    fun getBookMarkDetailInfo(): BookMarkDetailInfo {
        return BookMarkDetailInfo(
            id!!,
            title,
            summary,
            icon
        )
    }
}