package online.mengchen.collectionhelper.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import online.mengchen.collectionhelper.MyPageAdapter
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.RecentFragment
import online.mengchen.collectionhelper.home.HomeFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
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
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        val titleList = mutableListOf("首页", "最近浏览", "我的")
        val fragmentList = mutableListOf(
            HomeFragment(),
            RecentFragment(),
            MyFragment()
        )
        val pageAdapter = MyPageAdapter(
            supportFragmentManager,
            fragmentList,
            titleList
        )
        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private val PERMISSON = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET)


    private fun requestPermission() {
        val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, PERMISSON, 1);
        }
    }

}
