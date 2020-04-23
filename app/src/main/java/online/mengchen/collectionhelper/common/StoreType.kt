package online.mengchen.collectionhelper.common

import androidx.annotation.IntDef

object StoreType {

    const val ALIYUN = 0
    const val TENGXUNYUN = 1
    const val QNIUYUN = 2
    const val BAIDUWANGPAN = 3


    @IntDef(value = [ALIYUN])
    @Retention(AnnotationRetention.SOURCE)
    annotation class TypeStore {

    }
}