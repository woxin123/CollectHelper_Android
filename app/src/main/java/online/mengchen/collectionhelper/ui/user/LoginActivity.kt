package online.mengchen.collectionhelper.ui.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.layout_login.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.common.HTTPStatus
import online.mengchen.collectionhelper.data.network.SessionInterceptor
import online.mengchen.collectionhelper.databinding.LayoutLoginBinding
import online.mengchen.collectionhelper.ui.user.register.RegisterActivity
import online.mengchen.collectionhelper.utils.LoginUtils

class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LoginActivity"
        const val REQUEST_CODE_LOGIN = 100
        const val LOGIN_STATUS = "login_status"
    }

    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        // initView()
        val binding: LayoutLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.layout_login)
        mLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.loginModel = mLoginViewModel
        binding.lifecycleOwner = this
        mLoginViewModel.activity = this
        initListener()
    }

    private fun initListener() {
        loginButton.setOnClickListener {
            mLoginViewModel.login()
        }

        btn_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        mLoginViewModel.mLoginRes.observe(this, Observer {
            // 存储登录状态
            this.getSharedPreferences(Constant.SP_STATUS_KEY, Context.MODE_PRIVATE)
                .edit().putBoolean(Constant.IS_LOGIN, true).apply()
            LoginUtils.writeSession(this, SessionInterceptor.cookieSir!!)
            LoginUtils.user = it.data
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK, Intent().apply { putExtra(LOGIN_STATUS, true) })
            this.finish()
        })
        mLoginViewModel.mLoginError.observe(this, Observer {
            if (it.status == HTTPStatus.BAD_REQUEST.code) {
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show()
            } else if (it.status == HTTPStatus.INTERNAL_SERVER_ERROR.code) {
                Toast.makeText(this, "服务器出错", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
