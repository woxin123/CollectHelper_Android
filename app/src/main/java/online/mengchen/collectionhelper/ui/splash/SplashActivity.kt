package online.mengchen.collectionhelper.ui.splash

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.activity_splash.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.data.file.CloudStoreInstance
import online.mengchen.collectionhelper.data.sp.StatusProperties
import online.mengchen.collectionhelper.ui.cloudstore.CloudStoreConfigActivity
import online.mengchen.collectionhelper.ui.main.MainActivity
import online.mengchen.collectionhelper.ui.user.LoginActivity
import online.mengchen.collectionhelper.ui.user.LoginActivity.Companion.LOGIN_STATUS
import online.mengchen.collectionhelper.ui.user.LoginActivity.Companion.REQUEST_CODE_LOGIN
import online.mengchen.collectionhelper.utils.LoginUtils

class SplashActivity : AppCompatActivity() {


    private lateinit var mViewModel: SplashViewModel

    companion object {
        const val REQUEST_CONFIG_CLOUD_STORE = 1001
    }


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
                if (!checkCloudStore()) {
                    startActivityForResult(Intent(this, CloudStoreConfigActivity::class.java), REQUEST_CONFIG_CLOUD_STORE)
                }
                initCloudStore()
                mViewModel.initStatus.setValue(true)
            }
        })


        mViewModel.initStatus.observe(this, Observer {
            if (it) {
                if (mViewModel.delayComplete) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })
    }

    private fun initCloudStore() {
        CloudStoreInstance.init(this, mViewModel.viewModelScope)
        mViewModel.initStatus.setValue(true)
    }

    /**
     * 如果配置了云存储返回 true，否则返回 false
     */
    private fun checkCloudStore(): Boolean {
        return StatusProperties.getCloudStore(this) != null

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            val loginStatus = data?.getBooleanExtra(LOGIN_STATUS, true)!!
            if (loginStatus) {
                if (!checkCloudStore()) {
                    startActivityForResult(Intent(this, CloudStoreConfigActivity::class.java), REQUEST_CONFIG_CLOUD_STORE)
                } else {
                    initCloudStore()
                }
            }
        }
        if (requestCode == REQUEST_CONFIG_CLOUD_STORE && resultCode == Activity.RESULT_OK) {
            val result = data?.getBooleanExtra(CloudStoreConfigActivity.CONFIG_SUCCESS, false)!!
            if (result) {
                initCloudStore()
                mViewModel.initStatus.setValue(true)
            }
        }
    }
}
