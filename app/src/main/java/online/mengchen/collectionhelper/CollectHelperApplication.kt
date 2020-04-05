package online.mengchen.collectionhelper

import android.app.Application
import android.content.Context

class CollectHelperApplication : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}