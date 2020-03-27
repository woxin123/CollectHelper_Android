package online.mengchen.collectionhelper.common

class Page<T> {
    var content: List<T> = listOf()
    var totalElements: Int = 0
    var totalPages: Int = 0
    var last = false
    var number = 0
    var size = 100
    var numberOfElements = 0
}