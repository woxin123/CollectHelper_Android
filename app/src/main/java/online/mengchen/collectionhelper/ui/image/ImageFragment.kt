package online.mengchen.collectionhelper.ui.image

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_image.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.databinding.FragmentImageBinding
import online.mengchen.collectionhelper.ui.image.list.ImageListActivity

class ImageFragment: Fragment() {

    companion object {
        private const val TAG = "ImageFragment"
        const val IMAGE_CATEGORY = "image_category"
    }

    private lateinit var mBinding: FragmentImageBinding
    private lateinit var mViewModel: ImageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false)
        mBinding.lifecycleOwner = this
        mViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        mBinding.viewModel = mViewModel
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initObserver()
    }

    private fun initView() {
        val recyclerViewAdapter = ImageRecyclerViewAdapter(mutableListOf(), mViewModel)
        imageCategories.layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        imageCategories.adapter = recyclerViewAdapter
    }

    private fun initObserver() {
        mViewModel.openImageCategory.observe(this.viewLifecycleOwner, Observer {
            val intent = Intent(this.activity, ImageListActivity::class.java)
            intent.putExtras(Bundle().apply {  putParcelable(IMAGE_CATEGORY, it) })
            startActivity(intent)
        })
    }

}