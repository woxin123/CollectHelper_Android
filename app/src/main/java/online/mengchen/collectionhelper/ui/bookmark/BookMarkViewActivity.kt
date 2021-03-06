package online.mengchen.collectionhelper.ui.bookmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.activity_book_mark_view.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.Constant

class BookMarkViewActivity : AppCompatActivity() {

    companion object {
        const val TAG = "BookMarkViewActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_mark_view)
        val url = intent.getStringExtra(Constant.BOOK_MARK_URL)
        Log.d(TAG, url!!)
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.settings.blockNetworkImage = false
        webView.loadUrl(url)
    }
}
