package online.mengchen.collectionhelper.common

import androidx.annotation.IntDef

object FileType {

    const val DOCUMENT = 0
    const val IMAGE = 1
    const val MUSIC = 2
    const val VIDEO = 3

    @IntDef(value = [DOCUMENT, IMAGE, MUSIC, VIDEO])
    @Retention(AnnotationRetention.SOURCE)
    annotation class TypeFile {

    }

}