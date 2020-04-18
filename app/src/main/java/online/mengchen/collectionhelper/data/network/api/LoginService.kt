package online.mengchen.collectionhelper.data.network.api

import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.ui.user.LoginUser
import online.mengchen.collectionhelper.ui.user.UserData
import retrofit2.http.*

interface LoginService {
    @POST("/sessions")
    suspend fun login(@Body user: LoginUser): ApiResult<UserData>
}