package online.mengchen.collectionhelper.bookmark

import java.time.LocalDateTime

data class BookMarkCategory (
    var categoryId: Long,
    var categoryName: String,
    var createTime: LocalDateTime,
    var updateTime: LocalDateTime
)
