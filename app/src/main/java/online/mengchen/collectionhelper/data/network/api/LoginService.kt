package online.mengchen.collectionhelper.data.network.api

import androidx.lifecycle.LiveData
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.user.LoginUser
import online.mengchen.collectionhelper.user.UserData
import retrofit2.Call
import retrofit2.http.*

interface LoginService {
    @POST("/sessions")
    fun login(@Body user: LoginUser): LiveData<ApiResult<UserData>?>
}