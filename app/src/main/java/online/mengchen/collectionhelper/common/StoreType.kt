package online.mengchen.collectionhelper.common

import androidx.annotation.IntDef

object StoreType {

    const val ALIYUN = 0


    @IntDef(value = [ALIYUN])
    @Retention(AnnotationRetention.SOURCE)
    annotation class TypeStore {

    }
}