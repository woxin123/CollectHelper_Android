package online.mengchen.collectionhelper.ui.user.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.base.Event
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.HTTPStatus
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.ui.user.RegisterUser
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {

    companion object {
        private const val TAG = "RegisterViewModel"
    }

    val usernameLiveData = MutableLiveData<String>("")

    val passwordLiveData = MutableLiveData<String>()

    val confirmPasswordLiveData = MutableLiveData<String>()

    private val _toastText = MutableLiveData<Event<Int>>()
    val toastValue: LiveData<Event<Int>>
        get() = _toastText

    private val _registerRes = MutableLiveData<Event<Boolean>>()
    val registerRes: LiveData<Event<Boolean>>
        get() = _registerRes

    private val userService = RetrofitClient.userService

    fun sendMessage(messageId: Int) {
        _toastText.value = Event(messageId)
    }

    fun register() {
        // 参数检测
        val username = usernameLiveData.value
        val password = passwordLiveData.value
        val confirmPassword = confirmPasswordLiveData.value

        Log.d(TAG, "username = $username password = $password confirmPassword = $confirmPassword")

        if (username.isNullOrBlank()) {
            _toastText.value =
                Event(R.string.username_is_not_empty)
            return
        }
        if (password.isNullOrBlank()) {
            _toastText.value =
                Event(R.string.password_is_not_empty)
            return
        }
        if (confirmPassword.isNullOrBlank()) {
            _toastText.value =
                Event(R.string.confirm_password_is_not_empty)
            return
        }
        if (password != confirmPassword) {
            _toastText.value =
                Event(R.string.password_is_inconsistent)
            return
        }
        if (password.length < 6) {
            _toastText.value =
                Event(R.string.password_must_big_six)
            return
        }

        viewModelScope.launch {
            try {
                val userRes = userService.registerUser(RegisterUser(username, password))
                if (userRes.status == HTTPStatus.CREATED.code) {
                    sendMessage(R.string.register_success)
                    _registerRes.value =
                        Event(true)
                }
            } catch (e: HttpException) {
                sendMessage(R.string.register_fail)
                _registerRes.value =
                    Event(false)
                e.printStackTrace()
            }
        }

    }
}