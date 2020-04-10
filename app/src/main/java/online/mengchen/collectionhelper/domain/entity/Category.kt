package online.mengchen.collectionhelper.domain.entity

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import online.mengchen.collectionhelper.bookmark.CategoryInfo
import java.lang.annotation.RetentionPolicy
import java.time.LocalDateTime

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true) var cid: Long? = null,
    @ColumnInfo(name = "category_name") var categoryName: String,
    @ColumnInfo(name = "category_type") var categoryType: Int,
    @ColumnInfo(name = "create_time") var createTime: LocalDateTime,
    @ColumnInfo(name = "update_time") var updateTime: LocalDateTime,
    @ColumnInfo(name = "uid") var uid: Long
) {
    companion object {
        const val BOOKMARK = 0
        const val IMAGE = 1
        const val DOCUMENT = 2
        const val MUSIC = 3
        const val VIDEO = 4
    }

    @IntDef(value = [BOOKMARK, IMAGE, DOCUMENT, MUSIC, VIDEO])
    @Retention(AnnotationRetention.SOURCE)
    annotation class CategoryType {

    }

    fun getCategoryInfo(): CategoryInfo {
        return CategoryInfo(cid!!, categoryName, categoryType, createTime, updateTime)
    }
}