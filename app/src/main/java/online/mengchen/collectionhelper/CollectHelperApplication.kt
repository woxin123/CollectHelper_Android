package online.mengchen.collectionhelper

import android.R.attr
import android.app.Application
import android.content.Context
import online.mengchen.collectionhelper.data.file.CloudStore


class CollectHelperApplication : Application() {

    companion object {
        lateinit var context: Context
    }

    var cloudStore: CloudStore? = null

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}