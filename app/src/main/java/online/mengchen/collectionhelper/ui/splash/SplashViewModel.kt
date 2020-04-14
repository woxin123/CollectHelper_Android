package online.mengchen.collectionhelper.ui.splash

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.Event
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.utils.LoginUtils
import retrofit2.HttpException

class SplashViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        const val TAG = "SplashViewModel"
        const val TIME = 3
    }

    private val userService = RetrofitClient.userService

    private val _complete = MutableLiveData<Event<Unit>>()
    val complete: LiveData<Event<Unit>>
        get() = _complete

    private val _needLogin = MutableLiveData<Boolean>(true)
    val needLogin: LiveData<Boolean>
        get() = _needLogin

    fun start() {
        viewModelScope.launch {
            delay(TIME * 1000L)
            _complete.value = Event(Unit)
        }
    }



}