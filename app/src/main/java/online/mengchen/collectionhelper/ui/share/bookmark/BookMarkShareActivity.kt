package online.mengchen.collectionhelper.ui.share.bookmark

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.ui.share.common.ShareActivity
import online.mengchen.collectionhelper.ui.share.common.ShareViewModel


class BookMarkShareActivity : ShareActivity() {

    companion object {
        const val TAG = "BookMarkShareActivity"
    }

    private lateinit var mViewModel: BookMarkShareViewModel

    private lateinit var shareUrl: String


    override fun getShareContent() {
        shareUrl = intent.getStringExtra(Intent.EXTRA_TEXT)!!

    }

    override fun getViewModel(): ShareViewModel {
        if (!this::mViewModel.isInitialized) {
            mViewModel = ViewModelProvider(this).get(BookMarkShareViewModel::class.java)
        }
        return mViewModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun commit() {
        mViewModel.addBookMark(mSelectCategories, shareUrl)
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.addBookMarkInfoStatus.observe(this, Observer {
            mViewModel.sendMessage(R.string.save_success)
        })
    }

}
