package online.mengchen.collectionhelper.ui.main

import android.content.Context
import online.mengchen.collectionhelper.common.Constant

class MainViewModel {
    lateinit var mContext: Context
    fun isQiniuConfig(): Boolean {
        val sp = mContext.getSharedPreferences(Constant.SP_STATUS_KEY, Context.MODE_PRIVATE)
        return sp.getBoolean(Constant.IS_QINIU_CONFIG, false)
    }

    fun isLogin(): Boolean {
        val sp = mContext.getSharedPreferences(Constant.SP_STATUS_KEY, Context.MODE_PRIVATE)
        return sp.getBoolean(Constant.IS_LOGIN, false)
    }
}