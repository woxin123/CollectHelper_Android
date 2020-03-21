package online.mengchen.collectionhelper.bookmark

data class BookMark(
    var url: String = "",
    var title: String = "",
    var summary: String = "",
    var time: String = "2020-02-23",
    var favicon: String = "",
    var category: String = "未分类"
)