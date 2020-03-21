package online.mengchen.collectionhelper.user

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.data.network.LoginNetwork
import retrofit2.Callback
import online.mengchen.collectionhelper.common.Result
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Callable

class LoginViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        const val TAG = "LoginViewModel"
    }

    var username = ObservableField<String>("")
    var password = ObservableField<String>("")
    var rememberLogin = ObservableField<Boolean>(false)
    var activity: Activity? = null
    val isLoginSuccess = MutableLiveData<Boolean>()
    val user = User()


    private val loginNetwork = LoginNetwork()

    fun login() {
//        launch({
//            val result = loginNetwork.login(username.get()!!, password.get()!!)
//            val user =
//            Log.d(TAG, "result = $result")
//            isLoginSuccess.value = true
//        }, {
//            it.printStackTrace()
//            Toast.makeText(activity, "登录失败", Toast.LENGTH_SHORT).show()
//            isLoginSuccess.value = true
//        })
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                delay(500L)
            }
            isLoginSuccess.value = true
        }
    }

    private suspend fun doLogin(username: String, password: String) = withContext(Dispatchers.IO) {
        val result = loginNetwork.login(username, password)
        result
    }

    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }
}