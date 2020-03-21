package online.mengchen.collectionhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.data.file.qiniu.QiniuConfig
import online.mengchen.collectionhelper.data.file.qiniu.QiniuFileOperator
import online.mengchen.collectionhelper.home.HomeFragment
import online.mengchen.collectionhelper.image.ImageFragment
import online.mengchen.collectionhelper.user.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        val titleList = mutableListOf("首页", "最近浏览", "我的")
        val fragmentList = mutableListOf(HomeFragment() ,RecentFragment(), MyFragment())
        val pageAdapter = MyPageAdapter(supportFragmentManager, fragmentList, titleList)
        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)
         startActivity(Intent(this, LoginActivity::class.java))
        GlobalScope.launch(Dispatchers.IO) {
            val token = QiniuConfig.getToken("mctest001")
            val qiniuFileOperator = QiniuFileOperator(token)
            val bytes: ByteArray = ByteArray(2048)
            val inputStream = this@MainActivity.resources.assets.open("ic_launcher.png")
            inputStream.read(bytes)

            qiniuFileOperator.upload("test.jpg", bytes)
        }
    }

}
