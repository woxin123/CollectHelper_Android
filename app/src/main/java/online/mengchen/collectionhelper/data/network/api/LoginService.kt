package online.mengchen.collectionhelper.data.network.api

import online.mengchen.collectionhelper.common.Result
import retrofit2.Call
import retrofit2.http.*

interface LoginService {
    @POST("/login")
    @FormUrlEncoded
    fun login(@Field("account") account: String, @Field("password") password: String): Call<Result>
}