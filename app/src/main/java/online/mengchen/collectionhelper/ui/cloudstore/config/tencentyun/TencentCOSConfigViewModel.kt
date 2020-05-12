package online.mengchen.collectionhelper.ui.cloudstore.config.tencentyun

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.base.BaseViewModel
import online.mengchen.collectionhelper.base.SingleLiveEvent
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.repository.TencentCOSConfigRepository
import online.mengchen.collectionhelper.domain.entity.TencentCOSConfig
import online.mengchen.collectionhelper.utils.LoginUtils

class TencentCOSConfigViewModel(application: Application) : BaseViewModel(application) {

    val secretId = MutableLiveData<String>()
    val secretKey = MutableLiveData<String>()
    val bucket = MutableLiveData<String>()
    val region = MutableLiveData<String>()

    private val tencentCOSConfigRepository: TencentCOSConfigRepository
    private var tencentCOSConfig: TencentCOSConfig? = null

    private val _commitSuccess = SingleLiveEvent<Unit>()
    val commitSuccess: LiveData<Unit>
        get() = _commitSuccess

    init {
        val db = CollectHelpDatabase.getDatabase(getApplication(), viewModelScope)
        tencentCOSConfigRepository = TencentCOSConfigRepository(db.tencentCOSConfigDao())
        viewModelScope.launch {
            tencentCOSConfig = tencentCOSConfigRepository.getTencentCOSConfig()
            if (tencentCOSConfig != null) {
                secretId.value = tencentCOSConfig?.secretId
                secretKey.value = tencentCOSConfig?.secretKey
                bucket.value = tencentCOSConfig?.bucket
                region.value = tencentCOSConfig?.region
            }
        }
    }


    fun commit() {
        if (!checked()) {
            return
        }
        val secretId = secretId.value!!
        val secretKey = secretKey.value!!
        val bucket = bucket.value!!
        val region = region.value!!
        if (tencentCOSConfig == null) {
            tencentCOSConfig = TencentCOSConfig(null, secretId, secretKey, bucket, region, LoginUtils.user?.userId!!)
            viewModelScope.launch {
                tencentCOSConfigRepository.insert(tencentCOSConfig!!)
            }
        } else {
            val tencentCOSConfig = tencentCOSConfig!!
            tencentCOSConfig.secretId = secretId
            tencentCOSConfig.secretKey = secretKey
            tencentCOSConfig.bucket = bucket
            tencentCOSConfig.region = region
            viewModelScope.launch {
                tencentCOSConfigRepository.update(tencentCOSConfig)
            }
        }
        _commitSuccess.setValue(Unit)
    }

    private fun checked(): Boolean {
        if (secretId.value.isNullOrBlank()) {
            sendToastMessage(R.string.secret_id_is_not_empty)
            return false
        }
        if (secretKey.value.isNullOrBlank()) {
            sendToastMessage(R.string.secret_key_is_not_empty)
            return false
        }
        if (bucket.value.isNullOrBlank()) {
            sendToastMessage(R.string.bucket_name_is_not_empty)
            return false
        }
        if (region.value.isNullOrBlank()) {
            sendToastMessage(R.string.region_is_not_empty)
        }
        return true
    }

}