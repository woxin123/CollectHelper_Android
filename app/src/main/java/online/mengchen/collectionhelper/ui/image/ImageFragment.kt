package online.mengchen.collectionhelper.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_image.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.databinding.FragmentImageBinding

class ImageFragment: Fragment() {

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

    }

    private fun initView() {
        val recyclerViewAdapter = ImageRecyclerViewAdapter(mutableListOf(), context!!)
        imageCategories.layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        imageCategories.adapter = recyclerViewAdapter
    }



}