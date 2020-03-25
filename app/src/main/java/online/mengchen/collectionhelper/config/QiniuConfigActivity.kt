package online.mengchen.collectionhelper.config

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.databinding.ActivityQiniuConfigBinding

class QiniuConfigActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityQiniuConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_qiniu_config)
    }
}
