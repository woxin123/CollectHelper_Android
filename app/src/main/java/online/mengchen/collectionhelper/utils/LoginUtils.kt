package online.mengchen.collectionhelper.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.data.network.SessionInterceptor
import online.mengchen.collectionhelper.ui.user.UserData
import retrofit2.HttpException

object LoginUtils {

    const val TAG = "LoginUtils"

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

    /**
     * 初始化用户数据，检测 session 是否可用，同时 set user info
     * true 表示需要登录
     * false 表示不需要登录
     */
    fun initUser(context: Context ,scope: CoroutineScope): LiveData<Boolean> {
        val needLogin = MutableLiveData<Boolean>()
        if (isNeedLogin(context)) {
            Log.d(TAG, "需要登录")
            needLogin.value = true
            return needLogin
        }
        scope.launch {
            try {
                val userInfoRes = RetrofitClient.userService.getUserInfo()
                user = userInfoRes.data
                needLogin.value = false
            } catch (e: HttpException) {
                e.printStackTrace()
                needLogin.value = true
            }
        }
        return needLogin
    }



}