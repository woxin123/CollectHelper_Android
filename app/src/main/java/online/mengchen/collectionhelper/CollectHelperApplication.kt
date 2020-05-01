package online.mengchen.collectionhelper

import android.R.attr
import android.app.Application
import android.content.Context
import android.util.Log
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection
import com.squareup.leakcanary.LeakCanary
import com.tencent.smtt.sdk.QbSdk
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.file.CloudStore


class CollectHelperApplication : Application() {

    companion object {
        lateinit var context: Context
        const val TAG = "CollectHelperApplication"
    }

    var cloudStore: CloudStore? = null

    override fun onCreate() {
        super.onCreate()
        context = this
        val pcb = object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                Log.d(TAG, "完成 TBS Core 的初始化")
            }

            override fun onViewInitFinished(p0: Boolean) {
                Log.d(TAG, "内核是否加载成功: $p0")
            }
        }
        // x5内核预加载，异步初始化x5 webview所需环境
        QbSdk.initX5Environment(applicationContext, pcb)

        // 内存泄漏检测
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }

        // file downloader 初始化
        FileDownloader.setupOnApplicationOnCreate(this)
            .connectionCreator(
                FileDownloadUrlConnection.Creator(
                    FileDownloadUrlConnection.Configuration()
                        .connectTimeout(Constant.FILE_DOWNLOADER_CONNECT_TIME_OUT)
                        .readTimeout(Constant.FILE_DOWNLOADER_READ_TIME_OUT)
                )
            )

    }

}