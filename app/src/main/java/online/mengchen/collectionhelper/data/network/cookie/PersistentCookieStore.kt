package online.mengchen.collectionhelper.data.network.cookie

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.internal.and
import okhttp3.internal.toHexString
import java.io.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

class PersistentCookieStore(context: Context) {

    companion object {
        const val TAG = "PersistentCookieStore"
        const val COOKIE_PREFS = "cookie_prefs"
        const val COOKIE_NAME_PREF = "cookie_"

        private fun isCookieExpired(cookie: Cookie): Boolean {
            return cookie.expiresAt < System.currentTimeMillis()
        }
    }

    private val cookies: HashMap<String, ConcurrentHashMap<String, Cookie>>

    private val cookiePrefs: SharedPreferences

    init {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE)
        cookies = HashMap()

        val prefsMap = cookiePrefs.all
        prefsMap.forEach { entry ->
            if (entry.value != null && entry.key.startsWith(COOKIE_NAME_PREF)) {
                val cookieNames = (entry.value as String).split(",")
                for (name in cookieNames) {
                    val encodeCookie = cookiePrefs.getString(COOKIE_NAME_PREF + name, null)
                    if (encodeCookie != null) {
                        val decodeCookie = decodeCookie(encodeCookie)
                        if (decodeCookie != null) {
                            if (!cookies.containsKey(entry.key)) {
                                cookies[entry.key] = ConcurrentHashMap()
                            }
                            cookies[entry.key]!![name] = decodeCookie
                        }
                    }
                }
            }
        }
    }

    private fun getCookieToken(cookie: Cookie): String {
        return cookie.name + "@" + cookie.domain
    }

    fun saveCookie(url: HttpUrl, urlCookies: List<Cookie>) {
        if (!cookies.containsKey(url.host)) {
            cookies[url.host] = ConcurrentHashMap()
        }
        for (cookie in urlCookies) {
            // 检查当前 cookie 是否过期'
            if (isCookieExpired(
                    cookie
                )
            ) {
                removeCookie(url, cookie)
            } else {
                saveCookie(url, cookie, getCookieToken(cookie))
            }
        }
    }

    fun saveCookie(url: HttpUrl, cookie: Cookie) {
        if (!cookies.containsKey(url.host)) {
            cookies[url.host] = ConcurrentHashMap()
        }
        // 检查当前 cookie 是否过期'
        if (isCookieExpired(
                cookie
            )
        ) {
            removeCookie(url, cookie)
        } else {
            saveCookie(url, cookie, getCookieToken(cookie))
        }
    }

    private fun saveCookie(url: HttpUrl, cookie: Cookie, name: String) {
        // 内存缓存
        cookies[url.host]!![name] = cookie
        // 文件缓存
        val editor = cookiePrefs.edit()
        editor.putString(url.host, TextUtils.join(",", cookies[url.host]!!.keys))
        editor.putString(
            COOKIE_NAME_PREF + name, encodeCookie(
                SerializableHttpCookie(
                    cookie
                )
            ))
        editor.apply()
    }

    fun removeCookie(url: HttpUrl, cookie: Cookie): Boolean {
        val name = getCookieToken(cookie)
        if (cookies.containsKey(url.host) && cookies[url.host]!!.contains(name)) {
            // 内存移除
            cookies[url.host]!!.remove(name)
            val editor = cookiePrefs.edit()
            if (cookiePrefs.contains(COOKIE_NAME_PREF + name)) {
                editor.remove(COOKIE_NAME_PREF + name)
            }
            editor.putString(url.host, TextUtils.join(",", cookies[url.host]!!.keys))
            editor.apply()
            return true
        } else {
            return false
        }
    }

    fun removeCookie(url: HttpUrl): Boolean {
        if (cookies.containsKey(url.host)) {
            // 文件一移除
            val cookieNames = cookies[url.host]!!.keys
            val editor = cookiePrefs.edit()
            for (name in cookieNames) {
                if (cookiePrefs.contains(COOKIE_NAME_PREF + cookieNames)) {
                    editor.remove(COOKIE_NAME_PREF + cookieNames)
                }
            }
            editor.remove(url.host)
            // 内存移除
            cookies.remove(url.host)
            return true
        } else {
            return false
        }
    }

    fun removeAllCookie(url: HttpUrl): Boolean {
        val editor = cookiePrefs.edit()
        editor.clear().apply()
        cookies.clear()
        return true
    }

    fun getAllCookie(): List<Cookie> {
        val result = mutableListOf<Cookie>()
        cookies.forEach { entry ->
            result.addAll(entry.value.values)
        }
        return result
    }

    fun getCookie(url: HttpUrl): List<Cookie> {
        val result = mutableListOf<Cookie>()
        val map = cookies[url.host]
        if (map != null) {
            result.addAll(map.values)
        }
        return result
    }

    private fun encodeCookie(cookie: SerializableHttpCookie): String? {
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            Log.d(TAG, "IOException in encodeCookie", e)
            return null
        }
        return byteArrayToHexString(os.toByteArray())
    }

    private fun decodeCookie(cookieString: String): Cookie? {
        val bytes = hexStringToByteArray(cookieString)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: Cookie? = null
        try {
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as SerializableHttpCookie).getCookie()
        } catch (e: IOException) {
            Log.d(TAG, "IOException in decodeCookie", e)
        } catch (e: ClassNotFoundException) {
            Log.d(TAG, "ClassNotFoundException in decodeCookie", e)
        }
        return cookie
    }

    private fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (byte in bytes) {
            val v = byte and 0xFF
            if (v < 16) {
                sb.append('0')
            }
            sb.append(v.toHexString())
        }
        return sb.toString().toUpperCase(Locale.US)
    }


    private fun hexStringToByteArray(hexString: String): ByteArray {
        val length = hexString.length
        val byteArray = ByteArray(length.div(2))
        for (i in 0 until length step 2) {
            byteArray[i.div(2)] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(
                hexString[i + 1],
                16
            )).toByte()
        }
        return byteArray
    }


}