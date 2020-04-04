package online.mengchen.collectionhelper.bookmark

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

data class BookMarkCategory (
    var categoryId: Long,
    var categoryName: String,
    var createTime: LocalDateTime,
    var updateTime: LocalDateTime
) {
    companion object {
        // 创建一个未分类的 bookMarkCategory
        @RequiresApi(Build.VERSION_CODES.O)
        fun unCategorized(): BookMarkCategory {
            return BookMarkCategory(-1, "未分类", LocalDateTime.now(), LocalDateTime.now())
        }
    }
}
