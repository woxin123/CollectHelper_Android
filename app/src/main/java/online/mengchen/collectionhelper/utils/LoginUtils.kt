package online.mengchen.collectionhelper.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.Cookie
import okhttp3.internal.cookieToString
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.network.SessionInterceptor
import online.mengchen.collectionhelper.user.UserData

import java.util.*

object LoginUtils {

    var user: UserData? = null

    /**
     * 这里验证了 session 是否存在
     */
    fun isNeedLogin(context: Context): Boolean {
        val sp = context.getSharedPreferences(Constant.SP_COOKIE, Context.MODE_PRIVATE)
        val sessionCookie = sp.getString(Constant.COOKIE, null) ?: return true
        // 验证 session 是否过期
        val sessionTime = sp.getLong("SESSION_TIME", 0)
        if (sessionTime != 0L && System.currentTimeMillis() - sessionTime <= Constant.SESSION_TIMEOUT) {
            if (SessionInterceptor.cookieSir == null) {
                SessionInterceptor.cookieSir = sessionCookie
            }
            return false
        }
        return true
    }

    fun writeSession(context: Context, sessionCookieStr: String) {
        val sp = context.getSharedPreferences(Constant.SP_COOKIE, Context.MODE_PRIVATE)
        sp.edit().putString(Constant.COOKIE, sessionCookieStr)
            .putLong("SESSION_TIME", System.currentTimeMillis())
            .apply()
    }



}