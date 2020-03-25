package online.mengchen.collectionhelper.data.network

import android.se.omapi.Session
import android.util.Log
import androidx.lifecycle.LiveData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.network.api.LoginService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofitClient = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(SessionInterceptor())
            .build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

    val loginService: LoginService = retrofitClient.create(LoginService::class.java)
}