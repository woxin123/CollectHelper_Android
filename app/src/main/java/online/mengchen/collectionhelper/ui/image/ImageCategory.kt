package online.mengchen.collectionhelper.ui.image

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class ImageCategory(
    val categoryId: Long,
    var cover: Bitmap? = null,
    var name: String? = "分类"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(categoryId)
        parcel.writeParcelable(cover, flags)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageCategory> {
        override fun createFromParcel(parcel: Parcel): ImageCategory {
            return ImageCategory(parcel)
        }

        override fun newArray(size: Int): Array<ImageCategory?> {
            return arrayOfNulls(size)
        }
    }
}