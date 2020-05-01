package online.mengchen.collectionhelper.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel(application: Application) : AndroidViewModel(application), BaseViewLifecycleObserver {

    private val _toastMessage = SingleLiveEvent<Int>()
    val toastMessage: LiveData<Int>
        get() = _toastMessage

    /**
     * 发送 toast 提示
     */
    fun sendToastMessage(messageId: Int) {
        _toastMessage.setValue(messageId)
    }

    override fun onAny() {

    }

    override fun onCreate() {

    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onDestroy() {

    }

}