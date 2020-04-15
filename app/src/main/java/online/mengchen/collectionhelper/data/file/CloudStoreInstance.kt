package online.mengchen.collectionhelper.data.file

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.data.file.aliyun.AliyunCloudStore
import online.mengchen.collectionhelper.data.file.aliyun.AliyunConfiguration

object CloudStoreInstance {

    fun getAliyunInstance(cfg: AliyunConfiguration, scope: CoroutineScope): LiveData<AliyunCloudStore?> {
        val aliyunCloudStoreLiveData: MutableLiveData<AliyunCloudStore?> = MutableLiveData()
        scope.launch {
            var success = true
            val aliyunCloudStore = AliyunCloudStore(cfg, onSuccess = {
                success = true
            }, onFailure = { s: String, throwable: Throwable ->
                success = false
                throwable.printStackTrace()
            })
            if (success) {
                aliyunCloudStoreLiveData.value = aliyunCloudStore
            } else {
                aliyunCloudStoreLiveData.value = null
            }
        }
        return aliyunCloudStoreLiveData
    }

}