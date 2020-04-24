package online.mengchen.collectionhelper.ui.splash

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.activity_splash.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.data.sp.StatusProperties
import online.mengchen.collectionhelper.ui.cloudstore.CloudStoreConfigActivity
import online.mengchen.collectionhelper.ui.main.MainActivity
import online.mengchen.collectionhelper.ui.user.LoginActivity
import online.mengchen.collectionhelper.ui.user.LoginActivity.Companion.LOGIN_STATUS
import online.mengchen.collectionhelper.ui.user.LoginActivity.Companion.REQUEST_CODE_LOGIN
import online.mengchen.collectionhelper.utils.LoginUtils

class SplashActivity : AppCompatActivity() {


    private lateinit var mViewModel: SplashViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        webView.loadUrl("http://debugtbs.qq.com")
        mViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        mViewModel.start()
        initObserver()
    }

    private fun initObserver() {
        mViewModel.complete.observe(this, Observer {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
        LoginUtils.initUser(this, mViewModel.viewModelScope).observe(this, Observer {
            if (it) {
                startActivityForResult(Intent(this, LoginActivity::class.java), REQUEST_CODE_LOGIN)
            } else {
                checkCloudStore()
            }
        })
    }

    private fun checkCloudStore() {
        if (StatusProperties.getCloudStore(this) != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, CloudStoreConfigActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            val loginStatus = data?.getBooleanExtra(LOGIN_STATUS, true)!!
            if (loginStatus) {
                checkCloudStore()
            }
        }
    }
}
