package online.mengchen.collectionhelper.utils

import android.content.Intent
import com.google.gson.Gson
import okhttp3.ResponseBody
import online.mengchen.collectionhelper.CollectHelperApplication
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.HTTPStatus
import online.mengchen.collectionhelper.ui.user.LoginActivity
import retrofit2.HttpException

object HttpExceptionProcess {

    private val gson = Gson()


    fun process(e: HttpException): ApiResult<Unit> {
        val code = e.code()
        val errorRes = parseErrorBody(e.response()?.errorBody()!!)
        when (code) {
            HTTPStatus.BAD_REQUEST.code -> {
                return errorRes
            }
            HTTPStatus.UNAUTHORIZED.code -> {
                CollectHelperApplication.context.startActivity(Intent(CollectHelperApplication.context, LoginActivity::class.java))
            }
        }
        return errorRes
    }

    fun parseErrorBody(errorBody: ResponseBody): ApiResult<Unit> {
        return gson.fromJson<ApiResult<Unit>>(errorBody.string(), ApiResult::class.java)
    }

}