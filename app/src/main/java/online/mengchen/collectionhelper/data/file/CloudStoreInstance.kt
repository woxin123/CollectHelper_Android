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
import online.mengchen.collectionhelper.data.file.qiniu.QiniuCloudStore
import online.mengchen.collectionhelper.data.file.qiniu.QiniuConfiguration
import online.mengchen.collectionhelper.data.file.tencent.TencentCOSCloudStore
import online.mengchen.collectionhelper.data.file.tencent.TencentCOSConfiguration
import online.mengchen.collectionhelper.data.repository.AliyunConfigRepository
import online.mengchen.collectionhelper.data.repository.QiNiuYunConfigRepository
import online.mengchen.collectionhelper.data.repository.TencentCOSConfigRepository
import online.mengchen.collectionhelper.data.sp.StatusProperties
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicBoolean

object CloudStoreInstance {

    private val isInit: AtomicBoolean = AtomicBoolean(false)


    private lateinit var cloudStore: CloudStore


    private var cloudStoreType: Int = -1

    fun init(context: Context, scope: CoroutineScope): Boolean {
        val storeType = StatusProperties.getCloudStore(context)
        if (storeType == null) {
            Toast.makeText(context, "没有选择云存储", Toast.LENGTH_SHORT).show()
            return false
        }
        cloudStoreType = storeType
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

    fun getCloudStoreType(): Int {
        return cloudStoreType
    }


    private fun getCloudStoreInstanceByStoreType(
        @StoreType.TypeStore storeType: Int,
        context: Context,
        scope: CoroutineScope
    ) {
        if (isInit.get()) {
            return
        }
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
            StoreType.QNIUYUN -> {
                val qiQiuYunConfigRepository = QiNiuYunConfigRepository(db.qiNiuYunConfigDao())
                scope.launch {
                    val config = withContext(Dispatchers.IO) {
                        qiQiuYunConfigRepository.getByUid()
                    }!!
                    val qiNuiYunConfiguration = QiniuConfiguration(
                        config.accessKey,
                        config.secretKey,
                        config.bucket,
                        config.imageBucket,
                        config.documentBucket,
                        config.musicBucket,
                        config.videoBucket,
                        config.imagePath,
                        config.documentPath,
                        config.musicPath,
                        config.videoPath,
                        config.domain
                    )
                    cloudStore = QiniuCloudStore(qiNuiYunConfiguration)
                }
            }
            StoreType.TENGXUNYUN -> {
                val tencentCosConfigRepository =
                    TencentCOSConfigRepository(db.tencentCOSConfigDao())
                scope.launch {
                    val config = tencentCosConfigRepository.getTencentCOSConfig()!!
                    val tencentCosConfiguration = TencentCOSConfiguration(
                        config.secretId,
                        config.secretKey,
                        config.bucket,
                        config.region
                    )
                    cloudStore = TencentCOSCloudStore(tencentCosConfiguration)
                }
            }
        }
    }


}