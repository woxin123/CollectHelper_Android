package online.mengchen.collectionhelper.data.sp

import android.content.Context
import online.mengchen.collectionhelper.common.StoreType

object StatusProperties {

    private const val SP_STATUS = "status_key"
    private const val CLOUD_STORE_KEY = "cloud_store_status"

    /**
     * null 表示没有获取到值
     */
    fun getCloudStore(context: Context): Int? {
        val cloudStoreType = context.getSharedPreferences(SP_STATUS, Context.MODE_PRIVATE)
            .getInt(CLOUD_STORE_KEY, -1)
        return if (cloudStoreType == -1) {
            null
        } else {
            cloudStoreType
        }
    }

    fun setCloudStore(context: Context, @StoreType.TypeStore storeType: Int) {
        context.getSharedPreferences(SP_STATUS, Context.MODE_PRIVATE).edit()
            .putInt(CLOUD_STORE_KEY, storeType)
            .apply()
    }
}