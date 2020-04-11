package online.mengchen.collectionhelper.image

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_image_share.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.file.aliyun.AliyunCloudStore
import online.mengchen.collectionhelper.data.file.aliyun.AliyunConfiguration
import online.mengchen.collectionhelper.data.network.SessionInterceptor
import online.mengchen.collectionhelper.databinding.ActivityImageShareBinding
import online.mengchen.collectionhelper.user.LoginActivity

class ImageShareActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ImageShareActivity"
    }

    private lateinit var mViewModel: ImageShareViewModel
    private lateinit var mBinding: ActivityImageShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        QMUIStatusBarHelper.translucent(window)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_share)
        mBinding.lifecycleOwner = this
        if (SessionInterceptor.cookieSir == null) {
            val cookie = getSharedPreferences(Constant.SP_COOKIE, Context.MODE_PRIVATE)
                .getString(Constant.COOKIE, null)
            if (cookie == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                SessionInterceptor.cookieSir = cookie
            }
        }
        mViewModel = ViewModelProvider(this).get(ImageShareViewModel::class.java)
        mBinding.viewModel = mViewModel
        // 获取分享的图片
        val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        Log.d(TAG, imageUri?.toString()!!)

        mViewModel.aliyunConfig.observe(this, Observer { it ->
            Log.d(TAG, it.toString())
            Log.d(TAG, "aaaaa")
            mViewModel.viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    mViewModel.cloudStore = AliyunCloudStore(
                        AliyunConfiguration(it.accessKey, it.secretKey, it.bucket), /*{ success: String ->
                        Toast.makeText(this, success, Toast.LENGTH_SHORT).show()
                    }, { s: String, _: Throwable ->
                        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
                    }*/ null, null
                    )
                }
            }
        })
        initView()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.loadCategories()
    }

    private fun initView() {
        categoriesRecyclerView.adapter = ImageCategoryAdapter(emptyList())
        categoriesRecyclerView.layoutManager =
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
    }

    private fun initListener() {

    }
}
