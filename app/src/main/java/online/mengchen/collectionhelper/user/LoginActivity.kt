package online.mengchen.collectionhelper.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.layout_login.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.databinding.LayoutLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LoginActivity"
    }

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        // initView()
        val binding: LayoutLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.layout_login)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.loginModel = loginViewModel
        binding.lifecycleOwner = this
        loginViewModel.activity = this
    }
}
