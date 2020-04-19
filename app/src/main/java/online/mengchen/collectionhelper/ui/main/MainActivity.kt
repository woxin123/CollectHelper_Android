package online.mengchen.collectionhelper.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

}
