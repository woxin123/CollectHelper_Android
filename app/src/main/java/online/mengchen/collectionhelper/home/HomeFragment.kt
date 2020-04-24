package online.mengchen.collectionhelper.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.bookmark.BookMarkFragment
import online.mengchen.collectionhelper.databinding.LayoutHomeBinding
import online.mengchen.collectionhelper.ui.document.DocumentFragment
import online.mengchen.collectionhelper.ui.image.ImageFragment
import online.mengchen.collectionhelper.ui.music.MusicFragment
import online.mengchen.collectionhelper.ui.video.VideoFragment

class HomeFragment: Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mBinding: LayoutHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_home, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mBinding.homeViewModel = homeViewModel
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.viewPager.adapter = HomePagerAdapter(childFragmentManager,
            listOf(BookMarkFragment(), ImageFragment(), DocumentFragment(), MusicFragment.newInstance(), VideoFragment()),
            listOf("书签", "图片", "文档", "音乐", "视频"))
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }

}