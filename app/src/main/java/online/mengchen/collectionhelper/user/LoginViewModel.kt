package online.mengchen.collectionhelper.user

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.data.network.SessionInterceptor
import online.mengchen.collectionhelper.data.network.api.LoginService
import java.lang.Exception

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "LoginViewModel"
    }

    var username = ObservableField<String>("")
    var password = ObservableField<String>("")
    var rememberLogin = ObservableField<Boolean>(false)
    lateinit var activity: AppCompatActivity
    val isLoginSuccess = MutableLiveData<Boolean>()
    val user = User()


    private val loginService = RetrofitClient.loginService

    fun login() {
        viewModelScope.launch {
            try {
                val loginRes = withContext(Dispatchers.IO) {
                    loginService.login(LoginUser(username.get()!!, password.get()!!))
                }
                if (loginRes.status == 400) {
                    Toast.makeText(activity, loginRes.message, Toast.LENGTH_SHORT).show()
                }
                if (loginRes.status == 201) {
                    // 存储登录状态
                    activity.getSharedPreferences(Constant.SP_STATUS_KEY, Context.MODE_PRIVATE)
                        .edit().putBoolean(Constant.IS_LOGIN, true).apply()
                    activity.getSharedPreferences(Constant.SP_COOKIE, Context.MODE_PRIVATE)
                        .edit().putString(Constant.COOKIE, SessionInterceptor.cookieSir).apply()
                    Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show()
                    activity.finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(activity, "请求出错", Toast.LENGTH_SHORT).show()
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