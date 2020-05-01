package online.mengchen.collectionhelper.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.layout_main.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.bookmark.BookMarkFragment
import online.mengchen.collectionhelper.ui.document.DocumentFragment
import online.mengchen.collectionhelper.ui.image.ImageFragment
import online.mengchen.collectionhelper.ui.music.MusicFragment
import online.mengchen.collectionhelper.ui.video.VideoFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private val mainVideoModel: MainViewModel by lazy {
        MainViewModel().apply {
            this.mContext = this@MainActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        requestPermission()
        val tabBuilder = tabSegment.tabBuilder().setGravity(Gravity.CENTER)
        tabSegment.addTab(tabBuilder.setText("书签").build(this))
        tabSegment.addTab(tabBuilder.setText("图片").build(this))
        tabSegment.addTab(tabBuilder.setText("文档").build(this))
        tabSegment.addTab(tabBuilder.setText("音乐").build(this))
        tabSegment.addTab(tabBuilder.setText("视频").build(this))
        contentViewPager.adapter =
            HomePagerAdapter(
                supportFragmentManager,
                listOf(
                    BookMarkFragment(),
                    ImageFragment(),
                    DocumentFragment(),
                    MusicFragment.newInstance(),
                    VideoFragment()
                )
            )
        tabSegment.setupWithViewPager(contentViewPager, false)
    }

    private val PERMISSON = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )


    private fun requestPermission() {
        val permission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSON, 1);
        }
    }


}
