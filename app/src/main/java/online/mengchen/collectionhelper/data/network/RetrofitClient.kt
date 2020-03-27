package online.mengchen.collectionhelper.data.network

import android.annotation.SuppressLint
import android.se.omapi.Session
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.network.api.BookMarkService
import online.mengchen.collectionhelper.data.network.api.LoginService
import online.mengchen.collectionhelper.utils.LocalDateDeserializer
import online.mengchen.collectionhelper.utils.LocalDateSerializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

object RetrofitClient {
    @SuppressLint("NewApi")
    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateSerializer())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateDeserializer())
        .create()

    private val retrofitClient = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(SessionInterceptor())
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

    val loginService: LoginService = retrofitClient.create(LoginService::class.java)
    val bookMarkService: BookMarkService = retrofitClient.create(BookMarkService::class.java)
}