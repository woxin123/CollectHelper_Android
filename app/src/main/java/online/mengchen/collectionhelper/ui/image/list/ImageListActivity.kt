package online.mengchen.collectionhelper.ui.image.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.data.file.CloudStoreInstance
import online.mengchen.collectionhelper.data.file.aliyun.AliyunCloudStore
import online.mengchen.collectionhelper.data.file.aliyun.AliyunConfiguration
import online.mengchen.collectionhelper.databinding.ActivityImageListBinding
import online.mengchen.collectionhelper.ui.image.ImageCategory
import online.mengchen.collectionhelper.ui.image.ImageFragment

class ImageListActivity : AppCompatActivity() {

    private lateinit var mViewModel: ImageListViewModel
    private lateinit var mBindings: ActivityImageListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBindings = DataBindingUtil.setContentView(this, R.layout.activity_image_list)
        supportActionBar?.hide()
        // 从传入的 intent 中或是 ImageCategory
        val imageCategory = intent.getParcelableExtra<ImageCategory>(ImageFragment.IMAGE_CATEGORY)
        // 获取 ViewModel
        mViewModel = ViewModelProvider(this).get(ImageListViewModel::class.java)
        mViewModel.imageCategory = imageCategory!!
        mBindings.viewModel = mViewModel
        mBindings.lifecycleOwner = this
        initView()
        initObserver()
    }

    private fun initView() {
        recyclerView.adapter = ImageListAdapter(emptyList(), this)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
    }

    private fun initObserver() {
        mViewModel.aliyunConfig.observe(this, Observer { it ->
            if (it == null) {
                Toast.makeText(this, "阿里云OSS初始化失败", Toast.LENGTH_SHORT).show()
            } else {
                CloudStoreInstance.getAliyunInstance(
                    AliyunConfiguration(
                        it.accessKey,
                        it.secretKey,
                        it.bucket
                    ), mViewModel.viewModelScope
                ).observe(this, Observer {
                    if (it == null) {
                        Toast.makeText(this, "阿里云OSS初始化失败", Toast.LENGTH_SHORT).show()
                    } else {
                        mViewModel.cloudStore = it
                        mViewModel.start()
                    }
                })
            }
        })
    }
}
