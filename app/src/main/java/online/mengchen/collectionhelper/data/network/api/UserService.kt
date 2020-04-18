package online.mengchen.collectionhelper.data.network.api

import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.ui.user.RegisterUser
import online.mengchen.collectionhelper.ui.user.UserData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    companion object {
        private const val USER = "users"
    }

    @GET(USER)
    suspend fun getUserInfo(): ApiResult<UserData>

    @POST(USER)
    suspend fun registerUser(@Body registerUser: RegisterUser): ApiResult<UserData>

}