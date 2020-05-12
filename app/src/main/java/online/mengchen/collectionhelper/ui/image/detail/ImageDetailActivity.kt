package online.mengchen.collectionhelper.ui.image.detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image_detail.*
import online.mengchen.collectionhelper.BR
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.base.BaseActivity
import online.mengchen.collectionhelper.databinding.ActivityImageDetailBinding
import online.mengchen.collectionhelper.ui.image.list.ImageListActivity

class ImageDetailActivity : BaseActivity<ActivityImageDetailBinding, ImageDetailViewModel>() {

    private lateinit var imageViews: Array<ImageView>
    private var position = 0

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_image_detail
    }

    override fun initParam() {
        super.initParam()
        position = intent.getIntExtra(ImageListActivity.IMAGE_POSITION, 0)
        val imageUrls = intent.getStringArrayExtra(ImageListActivity.IMAGE_INFO_ARRAY)
        if (imageUrls != null) {
            imageViews = Array(imageUrls.size) { index ->
                val imageView = ImageView(this)
                Glide.with(this).load(imageUrls[index]).into(imageView)
                imageView
            }
        }
    }

    override fun initData() {
        super.initData()
        imageViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int {
                return  imageViews.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val imageView = imageViews[position % imageViews.size]
                container.addView(imageView)
                return imageView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }
        imageViewPager.currentItem = position
    }

    override fun initViewModelId(): Int {
        return BR.viewModel
    }

}
