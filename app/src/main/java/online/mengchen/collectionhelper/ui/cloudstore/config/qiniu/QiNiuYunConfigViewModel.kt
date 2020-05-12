package online.mengchen.collectionhelper.ui.cloudstore.config.qiniu

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.base.BaseViewModel
import online.mengchen.collectionhelper.base.SingleLiveEvent
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.repository.QiNiuYunConfigRepository
import online.mengchen.collectionhelper.domain.entity.QiniuConfig
import online.mengchen.collectionhelper.utils.LoginUtils

class QiNiuYunConfigViewModel(application: Application) : BaseViewModel(application) {

    private val qiNiuYunConfigRepository: QiNiuYunConfigRepository
    private var qiNiuConfig: QiniuConfig? = null

    private val _commitComplete = SingleLiveEvent<Unit>()
    val completeComplete: LiveData<Unit>
        get() = _commitComplete


    val accessKey = MutableLiveData<String>()
    val secretKey = MutableLiveData<String>()
    val bucket = MutableLiveData<String>()
    val documentBucket = MutableLiveData<String>()
    val imageBucket = MutableLiveData<String>()
    val musicBucket = MutableLiveData<String>()
    val videoBucket = MutableLiveData<String>()
    val documentPath = MutableLiveData<String>()
    val imagePath = MutableLiveData<String>()
    val musicPath = MutableLiveData<String>()
    val videoPath = MutableLiveData<String>()
    val domain = MutableLiveData<String>()

    init {
        val db = CollectHelpDatabase.getDatabase(getApplication(), viewModelScope)
        qiNiuYunConfigRepository = QiNiuYunConfigRepository(db.qiNiuYunConfigDao())
        viewModelScope.launch {
            qiNiuConfig = qiNiuYunConfigRepository.getByUid()
            if (qiNiuConfig != null) {
                accessKey.value = qiNiuConfig?.accessKey
                secretKey.value = qiNiuConfig?.secretKey
                bucket.value = qiNiuConfig?.bucket
                documentBucket.value = qiNiuConfig?.documentBucket
                imageBucket.value = qiNiuConfig?.imageBucket
                musicBucket.value = qiNiuConfig?.musicBucket
                videoBucket.value = qiNiuConfig?.videoBucket
                documentPath.value = qiNiuConfig?.documentPath
                imagePath.value = qiNiuConfig?.imagePath
                musicPath.value = qiNiuConfig?.musicPath
                videoPath.value = qiNiuConfig?.videoPath
                domain.value = qiNiuConfig?.domain
            }
        }
    }

    fun commit() {
        if (!check()) {
            return
        }
        val accessKeyString = accessKey.value!!
        val secretKeyString = secretKey.value!!
        val bucketString = bucket.value!!
        val documentBucketString = documentBucket.value
        val imageBucketString = imageBucket.value
        val musicBucketString = musicBucket.value
        val videoBucketString = videoBucket.value
        val documentPathString = documentPath.value
        val imagePathString = imagePath.value
        val musicPathString = musicPath.value
        val videoPathString = videoPath.value
        val domainString = domain.value!!
        if (qiNiuConfig == null) {
            qiNiuConfig = QiniuConfig(
                null,
                accessKeyString,
                secretKeyString,
                bucketString,
                imageBucketString,
                documentBucketString,
                musicBucketString,
                videoBucketString,
                imagePathString,
                documentPathString,
                musicPathString,
                videoPathString,
                domainString,
                LoginUtils.user?.userId!!
            )
            viewModelScope.launch {
                qiNiuYunConfigRepository.insert(qiNiuConfig!!)
            }
        } else {
            val qiNiuConfig = qiNiuConfig!!
            qiNiuConfig.accessKey = accessKeyString
            qiNiuConfig.secretKey = secretKeyString
            qiNiuConfig.bucket = bucketString
            qiNiuConfig.imageBucket = imageBucketString
            qiNiuConfig.documentBucket = documentBucketString
            qiNiuConfig.musicBucket = musicBucketString
            qiNiuConfig.videoBucket = videoBucketString
            qiNiuConfig.imagePath = imagePathString
            qiNiuConfig.documentPath = documentPathString
            qiNiuConfig.musicPath = musicPathString
            qiNiuConfig.videoPath = videoPathString
            qiNiuConfig.domain = domainString
            viewModelScope.launch {
                qiNiuYunConfigRepository.update(qiNiuConfig)
            }
        }
        _commitComplete.setValue(Unit)
    }

    private fun check(): Boolean {
        if (accessKey.value.isNullOrBlank()) {
            sendToastMessage(R.string.access_key_is_not_empty)
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
        if (domain.value.isNullOrBlank()) {
            sendToastMessage(R.string.domain_is_not_empty)
            return false
        }
        return true
    }

}