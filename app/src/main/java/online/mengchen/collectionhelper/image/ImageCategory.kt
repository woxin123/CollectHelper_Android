package online.mengchen.collectionhelper.image

import android.graphics.Bitmap

data class ImageCategory(
    var cover: Bitmap? = null,
    var name: String = "分类",
    var count: Int = 100
)