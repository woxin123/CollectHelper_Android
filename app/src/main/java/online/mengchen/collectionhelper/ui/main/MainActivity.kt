package online.mengchen.collectionhelper.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.ui.bookmark.BookMarkFragment
import online.mengchen.collectionhelper.ui.cloudstore.CloudStoreConfigActivity
import online.mengchen.collectionhelper.ui.document.DocumentFragment
import online.mengchen.collectionhelper.ui.image.ImageFragment
import online.mengchen.collectionhelper.ui.music.MusicFragment
import online.mengchen.collectionhelper.ui.video.VideoFragment
import online.mengchen.collectionhelper.utils.LoginUtils

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
        initView()
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.cloud_store_config -> {
                    startActivity(Intent(this, CloudStoreConfigActivity::class.java))
                }
            }
            true
        }
        menuView.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun initView() {
        // 设置 toolbar
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
        val user = LoginUtils.user!!
        val headerLayout = navigationView.inflateHeaderView(R.layout.layout_drawable)
        val username = headerLayout.findViewById<TextView>(R.id.username)
        val icon = headerLayout.findViewById<ImageView>(R.id.icon)
        username.text = user.username
        if (user.avatar.isBlank()) {
            icon.setImageResource(R.drawable.collect)
        } else {
            Glide.with(this).load(user.avatar).into(icon)
        }

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
