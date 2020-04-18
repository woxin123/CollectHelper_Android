package online.mengchen.collectionhelper.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.ui.main.MainActivity
import online.mengchen.collectionhelper.ui.user.LoginActivity
import online.mengchen.collectionhelper.utils.LoginUtils

class SplashActivity : AppCompatActivity() {

    private lateinit var mViewModel: SplashViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        mViewModel.start()
        initObserver()
    }

    fun initObserver() {
        mViewModel.complete.observe(this, Observer {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
        LoginUtils.initUser(this, mViewModel.viewModelScope).observe(this, Observer {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        })
    }
}
