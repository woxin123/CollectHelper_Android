package online.mengchen.collectionhelper.ui.share.music

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.ui.image.share.ImageShareViewModel
import online.mengchen.collectionhelper.ui.share.common.ShareActivity
import online.mengchen.collectionhelper.ui.share.common.ShareViewModel

class MusicShareActivity : ShareActivity() {

    companion object {
        private const val TAG = "MusicShareActivity"
    }

    private lateinit var mViewModel: MusicShareViewModel
    private val mUris = mutableListOf<Uri>()

    /**
     * 获取分享的音乐
     */
    override fun getShareContent() {
        when (intent.action) {
            Intent.ACTION_SEND -> {
                val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                mUris.add(uri!!)
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                val uris = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                mUris.addAll(uris!!)
            }
            else -> {
                mViewModel.sendMessage(R.string.share_content_empty)
            }
        }
        Log.d(TAG, mUris.toString())
    }

    override fun getViewModel(): ShareViewModel {
        if (!this::mViewModel.isInitialized) {
            mViewModel = ViewModelProvider(this).get(MusicShareViewModel::class.java)
        }
        return mViewModel
    }

    /**
     * 上传音乐
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun commit() {
        mViewModel.saveMusics(mUris, mSelectCategories)
    }
}
