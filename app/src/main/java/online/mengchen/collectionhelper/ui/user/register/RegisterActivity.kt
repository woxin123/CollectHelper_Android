package online.mengchen.collectionhelper.ui.user.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_register.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.databinding.ActivityRegisterBinding

/**
 * 用户注册
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mViewModel: RegisterViewModel
    private lateinit var mHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        mBinding.lifecycleOwner = this
        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        mBinding.viewModel = mViewModel
        supportActionBar?.hide()
        mHandler = Handler(Looper.getMainLooper())
        initListener()
        initObserver()
    }

    private fun initListener() {
        commit.setOnClickListener {
            mViewModel.register()
        }
    }

    private fun initObserver() {
        mViewModel.toastValue.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(this, resources.getString(it), Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.registerRes.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    mHandler.postDelayed({
                        finish()
                    }, 300L)
                }
            }
        })
    }
}
