package online.mengchen.collectionhelper.data.network.api

import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.user.UserData
import retrofit2.http.GET

interface UserService {

    companion object {
        private const val USER = "users"
    }

    @GET(USER)
    suspend fun getUserInfo(): ApiResult<UserData>

}