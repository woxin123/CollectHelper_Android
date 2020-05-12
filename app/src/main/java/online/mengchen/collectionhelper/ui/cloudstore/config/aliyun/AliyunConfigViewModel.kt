package online.mengchen.collectionhelper.ui.cloudstore.config.aliyun

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.base.Event
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.repository.AliyunConfigRepository
import online.mengchen.collectionhelper.domain.entity.AliyunConfig
import online.mengchen.collectionhelper.utils.LoginUtils

class AliyunConfigViewModel(application: Application) : AndroidViewModel(application) {

    private val aliyunConfigRepository: AliyunConfigRepository
    private var aliyunConfig: AliyunConfig? = null
    private var _aliyunCommitRes = MutableLiveData<Boolean>()
    val aliyunCommitRes: LiveData<Boolean>
        get() = _aliyunCommitRes

    init {
        val db = CollectHelpDatabase.getDatabase(getApplication(), viewModelScope)
        aliyunConfigRepository = AliyunConfigRepository(db.aliyunConfigDao())
    }

    val accessKey: MutableLiveData<String> = MutableLiveData()
    val accessKeySecret: MutableLiveData<String> = MutableLiveData()
    val bucketName: MutableLiveData<String> = MutableLiveData()

    private val _toastText = MutableLiveData<Event<Int>>()
    val toastMessage: LiveData<Event<Int>>
        get() = _toastText

    fun setAliyunConfig(aliyunConfig: AliyunConfig) {
        this.aliyunConfig = aliyunConfig
        accessKey.value = aliyunConfig.accessKey
        accessKeySecret.value = aliyunConfig.secretKey
        bucketName.value = aliyunConfig.bucket
    }

    fun getAliyunConfig(): LiveData<AliyunConfig?> {
        return aliyunConfigRepository.aliyunConfig
    }

    fun sendMessage(messageId: Int) {
        _toastText.value = Event(messageId)
    }

    fun commit() {
        // 检查参数
        if (!check()) {
            return
        }
         // 添加
        val accessKeyString = accessKey.value!!
        val accessKeySecretString = accessKeySecret.value!!
        val bucketNameString = bucketName.value!!
        if (aliyunConfig == null) {
            aliyunConfig = AliyunConfig(null, accessKeyString, accessKeySecretString, bucketNameString, LoginUtils.user?.userId!!)
            viewModelScope.launch {
                aliyunConfigRepository.insert(aliyunConfig!!)
                // TODO check config
                _aliyunCommitRes.value = true
            }
        } else{
            // 更新
            aliyunConfig?.accessKey = accessKeyString
            aliyunConfig?.secretKey = accessKeySecretString
            aliyunConfig?.bucket = bucketNameString
            viewModelScope.launch {
                aliyunConfigRepository.update(aliyunConfig!!)
                _aliyunCommitRes.value = true
            }
        }
    }

    private fun check(): Boolean {
        var checked =  true
        val accessKey = accessKey.value
        val accessKeySecret = accessKeySecret.value
        val bucketName = bucketName.value
        if (accessKey.isNullOrBlank()) {
            checked = false
            sendMessage(R.string.access_key_is_not_empty)
        }
        if (accessKeySecret.isNullOrBlank()) {
            checked = false
            sendMessage(R.string.access_key_secret_is_not_empty)
        }
        if (bucketName.isNullOrBlank()) {
            checked = false
            sendMessage(R.string.bucket_name_is_not_empty)
        }
        return checked
    }

}