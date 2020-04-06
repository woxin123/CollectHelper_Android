package online.mengchen.collectionhelper.user

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.data.network.SessionInterceptor
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import online.mengchen.collectionhelper.utils.LoginUtils
import retrofit2.HttpException
import java.lang.Exception

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "LoginViewModel"
    }

    var username = ObservableField<String>("")
    var password = ObservableField<String>("")
    var rememberLogin = ObservableField<Boolean>(false)
    lateinit var activity: AppCompatActivity
    val mLoginRes by lazy { MutableLiveData<ApiResult<UserData>>() }
    val mLoginError by lazy { MutableLiveData<ApiResult<Unit>>() }
    val user = User()


    private val loginService = RetrofitClient.loginService

    fun login() {
        viewModelScope.launch {
            try {
                val loginRes = withContext(Dispatchers.IO) {
                    loginService.login(LoginUser(username.get()!!, password.get()!!))
                }
                mLoginRes.value = loginRes
            } catch (e: HttpException) {
                e.printStackTrace()
                val loginError = HttpExceptionProcess.process(e)
                mLoginError.value = loginError
            }
        }
//        doLogin(username.get()!!, password.get()!!).observe(activity!!, Observer {
//            if (it == null) {
//                Toast.makeText(activity, "服务器连接失败", Toast.LENGTH_SHORT).show()
//            } else {
//                if (it.status == 400) {
//                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
//                }
//                if (it.status == 201) {
//                    // 存储登录状态
//                    activity.getSharedPreferences(Constant.SP_STATUS_KEY, Context.MODE_PRIVATE)
//                        .edit().putBoolean(Constant.IS_LOGIN, true).apply()
//                    activity.getSharedPreferences(Constant.SP_COOKIE, Context.MODE_PRIVATE)
//                        .edit().putString(Constant.COOKIE, SessionInterceptor.cookieSir).apply()
//                    Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show()
//                    activity.finish()
//                }
//            }
//        })
    }

//    private fun doLogin(username: String, password: String): LiveData<ApiResult<UserData>?> {
//        return loginService.login(LoginUser(username, password))
//    }

    fun check(username: String, password: String) {

    }
}