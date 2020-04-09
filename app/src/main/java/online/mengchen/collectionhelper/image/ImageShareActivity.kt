package online.mengchen.collectionhelper.image

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import online.mengchen.collectionhelper.R

class ImageShareActivity : AppCompatActivity() {

    private lateinit var mViewModel: ImageShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        QMUIStatusBarHelper.translucent(window)
        setContentView(R.layout.activity_image_share)
        mViewModel = ViewModelProvider(this).get(ImageShareViewModel::class.java)
        initView()
        initListener()
    }

    private fun initView() {

    }

    private fun initListener() {

    }
}
