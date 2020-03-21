package online.mengchen.collectionhelper.data.file.qiniu

import com.qiniu.android.common.FixedZone
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager
import com.qiniu.util.Auth

object QiniuConfig {
    private val config = Configuration.Builder()
        .connectTimeout(20)
        .useHttps(true)
        .responseTimeout(60)
        .zone(FixedZone.zone0)
        .build()

    val uploadManager = UploadManager(config, 3)

    private val token: String? = null

    fun  getToken(bucketName: String): String {
        if (token != null) {
            return token
        }
        val auth = Auth.create("Qjr_bc6d3t4625r2E1oaqeZi2bW59973X2S-1ZtJ", "UBxNm5iGyMU-H8tsht4bmObXDrCHFLiFWR6VPZBR")
        return auth.uploadToken(bucketName)
    }
}