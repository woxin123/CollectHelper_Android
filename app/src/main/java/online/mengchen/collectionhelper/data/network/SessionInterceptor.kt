package online.mengchen.collectionhelper.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class SessionInterceptor : Interceptor {

    companion object {
        const val TAG = "SessionInterceptor"
        var cookieSir: String? = null
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        when {
            request.url.encodedPath == "/sessions" -> {
                val response = chain.proceed(request)
                cookieSir = response.header("Set-Cookie")
                return response
            }
            cookieSir != null -> {
                builder.addHeader("Cookie", cookieSir!!)
            }
            else -> {
                Log.e(TAG, "Cookie not found")
            }
        }
        return chain.proceed(builder.build())
    }

}