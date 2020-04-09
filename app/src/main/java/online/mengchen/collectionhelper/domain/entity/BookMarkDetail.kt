package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_mark_detail")
class BookMarkDetail (
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "bid") val bid: Long
)