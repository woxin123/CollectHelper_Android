package online.mengchen.collectionhelper.data.file

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.file.aliyun.AliyunCloudStore
import online.mengchen.collectionhelper.data.file.aliyun.AliyunConfiguration
import online.mengchen.collectionhelper.data.repository.AliyunConfigRepository
import online.mengchen.collectionhelper.data.sp.StatusProperties
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicBoolean

object CloudStoreInstance {

    private val isInit: AtomicBoolean = AtomicBoolean(false)


    private lateinit var cloudStore: CloudStore

    fun init(context: Context, scope: CoroutineScope): Boolean {
        val storeType = StatusProperties.getCloudStore(context)
        if (storeType == null) {
            Toast.makeText(context, "没有选择云存储", Toast.LENGTH_SHORT).show()
            return false
        }
        getCloudStoreInstanceByStoreType(storeType, context, scope)
        isInit.set(true)
        return true
    }

    fun getCloudStore(): CloudStore {
        if (isInit.get()) {
            return cloudStore
        } else {
            throw IllegalStateException("没有调用初始化方法")
        }
    }


    private fun getCloudStoreInstanceByStoreType(
        @StoreType.TypeStore storeType: Int,
        context: Context,
        scope: CoroutineScope
    ) {
        val db = CollectHelpDatabase.getDatabase(context, GlobalScope)
        when (storeType) {
            StoreType.ALIYUN -> {
                val aliyunConfigRepository = AliyunConfigRepository(db.aliyunConfigDao())
                scope.launch(Dispatchers.Main) {
                    val config = withContext(Dispatchers.IO) {
                        aliyunConfigRepository.findByUid()
                    }
                    val aliyunConfiguration =
                        AliyunConfiguration(config.accessKey, config.secretKey, config.bucket)
                    cloudStore = AliyunCloudStore(cfg = aliyunConfiguration, onSuccess = {
                        Toast.makeText(context, "阿里云初始化成功", Toast.LENGTH_SHORT).show()
                    }, onFailure = { s: String, throwable: Throwable ->
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
                        throwable.printStackTrace()
                    })
                }
            }
        }
    }


    fun getAliyunInstance(
        cfg: AliyunConfiguration,
        scope: CoroutineScope
    ): LiveData<AliyunCloudStore?> {
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