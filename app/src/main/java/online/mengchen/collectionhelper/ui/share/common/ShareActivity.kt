package online.mengchen.collectionhelper.ui.share.common

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_share.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.data.file.CloudStoreInstance
import online.mengchen.collectionhelper.domain.model.CategoryInfo
import online.mengchen.collectionhelper.databinding.ActivityShareBinding
import online.mengchen.collectionhelper.ui.custom.CustomProgressDialog
import online.mengchen.collectionhelper.ui.user.LoginActivity
import online.mengchen.collectionhelper.utils.LoginUtils

abstract class ShareActivity: AppCompatActivity() {

    companion object {
        private const val TAG = "ShareActivity"
        private const val REQUEST_CODE_PERMISSION_STORAGE = 100
    }

    private lateinit var mBinding: ActivityShareBinding
    lateinit var mCategoryAdapter: CategoryAdapter
    lateinit var mProgressDialog: CustomProgressDialog
    val mSelectCategories: MutableList<CategoryInfo> by lazy {  mutableListOf<CategoryInfo>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 检测是否已经登录
        LoginUtils.initUser(this, getViewModel().viewModelScope).observe(this, Observer {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        })

        supportActionBar?.hide()
        QMUIStatusBarHelper.translucent(window)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_share)
        mBinding.viewModel = getViewModel()
        mBinding.lifecycleOwner = this
        CloudStoreInstance.init(this, getViewModel().viewModelScope)
        checkAndRequestPermission()
        initView()
        initData()
        initListener()
        initObserver()
        getViewModel().cloudStore = CloudStoreInstance.getCloudStore()
    }

    override fun onResume() {
        super.onResume()
        getViewModel().loadCategories()
    }

    open fun initView() {
        // 初始化 recycler view
        mCategoryAdapter = CategoryAdapter(emptyList(), getViewModel())
        mBinding.categoriesRecyclerView.adapter = mCategoryAdapter
        mBinding.categoriesRecyclerView.layoutManager = GridLayoutManager(this, 3)
        mProgressDialog = CustomProgressDialog(this, "正在上传")
    }

    open fun initData() {
        getShareContent()
    }

    open fun initListener() {
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
            Log.d(TAG, "添加新的分类")
            getViewModel().addNewCategory()
        }
        commit.setOnClickListener {
            commit()
        }
    }

    open fun initObserver() {
        val mViewModel = getViewModel()
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
                mProgressDialog.setProgress(it)
            }
        })
        mViewModel.uploadCompleted.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                mProgressDialog.dismiss()
            }
        })
        mViewModel.selectCategory.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { p ->
                if (p.first) {
                    mSelectCategories.add(p.second)
                } else {
                    mSelectCategories.remove(p.second)
                }
            }
        })
        mViewModel.startUpload.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                mProgressDialog.setMaxProgress(it)
                mProgressDialog.show()
            }
        })
    }

    /**
     * 获取分享的内容
     */
    abstract fun getShareContent()

    /**
     * 获取 ShareViewModel
     */
    abstract fun getViewModel(): ShareViewModel

    /**
     * 提交并上传
     */
    abstract fun commit()

    private fun addCategory(categoryInfo: CategoryInfo) {
        mCategoryAdapter.addCategory(categoryInfo)
    }

    private fun showCreateCategoryView() {
        getViewModel().showCreateCategoryView()
        QMUIKeyboardHelper.showKeyboard(newCategoryName, 0)
    }

    private fun hideCreateCategoryView() {
        getViewModel().hideCreateCategoryView()
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