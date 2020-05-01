package online.mengchen.collectionhelper.ui.splash

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.base.Event
import online.mengchen.collectionhelper.base.SingleLiveEvent
import online.mengchen.collectionhelper.data.network.RetrofitClient

class SplashViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        const val TAG = "SplashViewModel"
        const val TIME = 3
    }

    private val userService = RetrofitClient.userService

    val initStatus: SingleLiveEvent<Boolean> = SingleLiveEvent(false)

    private val _complete = MutableLiveData<Event<Unit>>()
    val complete: LiveData<Event<Unit>>
        get() = _complete

    private val _needLogin = MutableLiveData<Boolean>(true)
    val needLogin: LiveData<Boolean>
        get() = _needLogin

    var delayComplete: Boolean = false

    fun start() {
        viewModelScope.launch {
            delay(TIME * 1000L)
            delayComplete = true
            if (initStatus.value == true) {
                _complete.value = Event(Unit)
            }
        }
    }




}