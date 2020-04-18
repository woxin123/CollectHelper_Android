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
//        if (!mainVideoModel.isQiniuConfig()) {
//            startActivity(Intent(this, QiniuConfigActivity::class.java))
//        }
//        if (!mainVideoModel.isLogin()) {
//            startActivity(Intent(this, LoginActivity::class.java))
//        }
//        Log.d(TAG, SessionInterceptor.cookieSir ?: "出错")
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
//        GlobalScope.launch(Dispatchers.IO) {
//            val token = QiniuConfig.getToken("mctest001")
//            val qiniuFileOperator = QiniuFileOperator(token)
//            val bytes: ByteArray = ByteArray(2048)
//            val inputStream = this@MainActivity.resources.assets.open("ic_launcher.png")
//            inputStream.read(bytes)
//
//            qiniuFileOperator.upload("test.jpg", bytes)
//        }
    }

}
