package online.mengchen.collectionhelper.bookmark

import java.time.LocalDateTime

data class BookMark(
    var id: Long,
    var url: String,
    var bookMarkDetail: BookMarkDetail?,
    var bookMarkCategory: BookMarkCategory,
    var createTime: LocalDateTime
)

data class BookMarkDetail (
    var id: Long,
    var title: String,
    var summary: String,
    var icon: String
)
