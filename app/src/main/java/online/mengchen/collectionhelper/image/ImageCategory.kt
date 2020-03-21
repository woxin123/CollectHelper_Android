package online.mengchen.collectionhelper.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import online.mengchen.collectionhelper.R

data class ImageCategory(
    var cover: Bitmap? = null,
    var name: String = "分类",
    var count: Int = 100
)