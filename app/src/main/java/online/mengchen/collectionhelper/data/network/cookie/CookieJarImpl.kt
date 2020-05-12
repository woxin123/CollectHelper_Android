package online.mengchen.collectionhelper.data.network.cookie

import android.content.Context
import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieJarImpl(context: Context) : CookieJar {

    companion object {
        const val TAG = "CookieJarImpl"
    }

    private val persistentCookieStore = PersistentCookieStore(context)

    override fun loadForRequest(url: HttpUrl): List<Cookie> {

        val cookies = this.persistentCookieStore.getCookie(url)
        for (cookie in cookies) {
            Log.d(TAG, "cookie = ${cookie.value}")
        }
        return cookies
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.persistentCookieStore.saveCookie(url, cookies)
    }
}