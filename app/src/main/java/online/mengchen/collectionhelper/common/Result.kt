package online.mengchen.collectionhelper.common

data class Result(
    var status: Int = 0,
    var message: String? = null,
    var data: Any? = null
)