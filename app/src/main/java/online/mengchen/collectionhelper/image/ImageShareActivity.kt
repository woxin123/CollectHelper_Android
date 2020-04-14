package online.mengchen.collectionhelper.image

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.activity_image_share.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.bookmark.CategoryInfo
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.file.aliyun.AliyunCloudStore
import online.mengchen.collectionhelper.data.file.aliyun.AliyunConfiguration
import online.mengchen.collectionhelper.data.network.SessionInterceptor
import online.mengchen.collectionhelper.databinding.ActivityImageShareBinding
import online.mengchen.collectionhelper.ui.custom.CustomProgressDialog
import online.mengchen.collectionhelper.user.LoginActivity
import online.mengchen.collectionhelper.utils.FileHelper
import online.mengchen.collectionhelper.utils.UriHelper
import java.io.*

class ImageShareActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ImageShareActivity"
        const val REQUEST_CODE_PERMISSION_STORAGE = 100
    }

    private lateinit var mViewModel: ImageShareViewModel
    private lateinit var mBinding: ActivityImageShareBinding
    private lateinit var mCategoryAdapter: ImageCategoryAdapter
    private var imageUri: Uri? = null
    private lateinit var progressDialog: CustomProgressDialog
    private val selectCategory: MutableList<CategoryInfo> by lazy { mutableListOf<CategoryInfo>() }


    @SuppressLint("Recycle")
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

        // 动态权限获取
        checkAndRequestPermission()

        // 获取分享的图片
        val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        this.imageUri = imageUri
        mViewModel.aliyunConfig.observe(this, Observer { it ->
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
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.loadCategories()
    }

    private fun initView() {
        mCategoryAdapter = ImageCategoryAdapter(emptyList(), mViewModel)
        categoriesRecyclerView.adapter = mCategoryAdapter
        categoriesRecyclerView.layoutManager =
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        categoriesRecyclerView.addItemDecoration(CategoryRecyclerViewItemDecoration())
        progressDialog = CustomProgressDialog(this, "正在上传...")
    }

    private fun initListener() {
        space.setOnClickListener {
            finish()
        }
        // 点击创建新分类按钮
        createCategory.setOnClickListener {
            showCreateCategoryView()
        }
        // 点击其他部分收起创建新分类的 View
        createCategorySpace.setOnClickListener {
            hideCreateCategoryView()
        }
        // 点击添加新分类提交按钮
        commitCreateCategory.setOnClickListener {
            mViewModel.addNewCategory()
        }
        commit.setOnClickListener {
            progressDialog.show()
            mViewModel.uploadImage(this, imageUri!!, selectCategory)
        }
    }

    private fun initObserver() {
        mViewModel.toastTextMessage.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(this, getString(it), Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.addCategoryEvent.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
                addCategory(it)
                hideCreateCategoryView()
            }
        })
        mViewModel.uploadProgressEvent.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                progressDialog.setProgress(it)
            }
        })
        mViewModel.uploadCompleted.observe(this, Observer { event ->
            Log.d(TAG, "aaaaaaaaaaa")
            event.getContentIfNotHandled()?.let {
                progressDialog.dismiss()
            }
        })
        mViewModel.selectCategory.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { p ->
                if (p.first) {
                    selectCategory.add(p.second)
                } else {
                    selectCategory.remove(p.second)
                }
            }
        })
    }

    private fun addCategory(categoryInfo: CategoryInfo) {
        mCategoryAdapter.addCategory(categoryInfo)
    }

    private fun showCreateCategoryView() {
        mViewModel.showCreateCategoryView()
        QMUIKeyboardHelper.showKeyboard(newCategoryName, 0)
    }

    private fun hideCreateCategoryView() {
        mViewModel.hideCreateCategoryView()
        QMUIKeyboardHelper.hideKeyboard(newCategoryName)
    }

    private fun checkAndRequestPermission() {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).forEach {
            if (ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(it),
                    REQUEST_CODE_PERMISSION_STORAGE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION_STORAGE) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
