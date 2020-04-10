package online.mengchen.collectionhelper.bookmark

import online.mengchen.collectionhelper.domain.entity.BookMark
import online.mengchen.collectionhelper.domain.entity.BookMarkDetail
import online.mengchen.collectionhelper.utils.LoginUtils
import java.time.LocalDateTime

data class BookMarkInfo(
    var id: Long,
    var url: String,
    var bookMarkDetail: BookMarkDetailInfo?,
    var category: CategoryInfo,
    var createTime: LocalDateTime
) {
    fun getBookMark(): BookMark {
        return BookMark(id, url, createTime, bookMarkDetail?.id, category.categoryId, LoginUtils.user?.userId!!)
    }
}

data class BookMarkDetailInfo (
    var id: Long,
    var title: String,
    var summary: String,
    var icon: String
) {
    fun getBookMarkDetail(): BookMarkDetail {
        return BookMarkDetail(id, title, summary, icon, LoginUtils.user?.userId!!)
    }
}

data class AddBookMark (
    var categoryId: Long = -1,
    var url: String
)
