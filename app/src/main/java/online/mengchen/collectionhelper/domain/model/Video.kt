package online.mengchen.collectionhelper.domain.model

import online.mengchen.collectionhelper.bookmark.CategoryInfo

data class VideoInfo (
    var videoName: String,
    var categoryInfo: CategoryInfo,
    var videoUrl: String,
    var createTime: String
)