package online.mengchen.collectionhelper.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.domain.entity.Category.*
import online.mengchen.collectionhelper.utils.LoginUtils
import java.time.LocalDateTime

data class CategoryInfo (
    var categoryId: Long,
    var categoryName: String,
    @CategoryType var categoryType: Int,
    var createTime: LocalDateTime,
    var updateTime: LocalDateTime
) {
    companion object {
        // 创建一个未分类的 bookMarkCategory
        @RequiresApi(Build.VERSION_CODES.O)
        fun unCategorized(@CategoryType categoryType: Int): CategoryInfo {
            return CategoryInfo(
                -1,
                "未分类",
                categoryType,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        }
    }

    fun getCategory(@StoreType.TypeStore storeType: Int): Category {
        return Category(categoryId, categoryName, categoryType, storeType, createTime, updateTime, LoginUtils.user?.userId!!)
    }
}

data class AddOrUpdateCategory(
    var categoryId: Long = -1,
    @CategoryType
    var categoryType: Int,
    var categoryName: String,
    @StoreType.TypeStore
    var storeType: Int
)
