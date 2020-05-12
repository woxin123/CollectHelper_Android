package online.mengchen.collectionhelper.ui.image.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_image_list.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.databinding.ActivityImageListBinding
import online.mengchen.collectionhelper.ui.image.ImageCategory
import online.mengchen.collectionhelper.ui.image.ImageFragment
import online.mengchen.collectionhelper.ui.image.detail.ImageDetailActivity

class ImageListActivity : AppCompatActivity() {

    companion object {
        const val IMAGE_POSITION = "image_position"
        const val IMAGE_INFO_ARRAY = "image_info_list"
    }

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
        mViewModel.start()
    }

    private fun initView() {
        recyclerView.adapter = ImageListAdapter(emptyList(), this, mViewModel)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
    }

    private fun initObserver() {
//        mViewModel.aliyunConfig.observe(this, Observer { it ->
//            if (it == null) {
//                Toast.makeText(this, "阿里云OSS初始化失败", Toast.LENGTH_SHORT).show()
//            } else {
//                CloudStoreInstance.getAliyunInstance(
//                    AliyunConfiguration(
//                        it.accessKey,
//                        it.secretKey,
//                        it.bucket
//                    ), mViewModel.viewModelScope
//                ).observe(this, Observer {
//                    if (it == null) {
//                        Toast.makeText(this, "阿里云OSS初始化失败", Toast.LENGTH_SHORT).show()
//                    } else {
//                        mViewModel.cloudStore = it
//                        mViewModel.start()
//                    }
//                })
//            }
//        })
        mViewModel.clickItem.observe(this, Observer {
            val intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra(IMAGE_POSITION, it)
            intent.putExtra(IMAGE_INFO_ARRAY, mViewModel.items.value?.toTypedArray())
            startActivity(intent)
        })
    }
}
