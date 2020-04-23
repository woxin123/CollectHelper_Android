package online.mengchen.collectionhelper.ui.share.document

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.ui.share.common.ShareActivity
import online.mengchen.collectionhelper.ui.share.common.ShareViewModel

class DocumentShareActivity : ShareActivity() {

    companion object {
        private const val TAG = "DocumentShareActivity"
    }

    private lateinit var mViewModel: DocumentShareViewModel
    private val mUris = mutableListOf<Uri>()


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
            mViewModel = ViewModelProvider(this).get(DocumentShareViewModel::class.java)
        }
        return mViewModel
    }

    override fun commit() {
        // TODO
    }

}
