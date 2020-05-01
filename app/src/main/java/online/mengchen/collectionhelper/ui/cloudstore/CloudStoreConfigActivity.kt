package online.mengchen.collectionhelper.ui.cloudstore

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_cloud_store_config.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.sp.StatusProperties
import online.mengchen.collectionhelper.ui.cloudstore.config.AliyunConfigFragment
import online.mengchen.collectionhelper.ui.cloudstore.config.qiniu.QiNiuYunConfigFragment

class CloudStoreConfigActivity : AppCompatActivity() {

    companion object {
        const val CHOOSE_CLOUD_STORE = 101
        const val CLOUD_STORE = "cloud_store"
        const val CONFIG_SUCCESS = "config_success"
    }

    @StoreType.TypeStore
    private var cloudStoreType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_store_config)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        cloudStoreType = StatusProperties.getCloudStore(this) ?: StoreType.ALIYUN
    }

    private fun initView() {
        topBar.setTitle("配置云存储")
        topBar.setTitleGravity(Gravity.CENTER_HORIZONTAL)
        cloudStoreChoose.text = "云存储选择"
        updateCloudStoreChoose(cloudStoreType)
    }

    private fun initListener() {
        cloudStoreChoose.setOnClickListener {
            // 开启 CloudStoreChooseActivity 并且传入现在选择的 cloud store
            val intent = Intent(this, CloudStoreChooseActivity::class.java)
            intent.putExtra(CLOUD_STORE, cloudStoreType)
            startActivityForResult(intent, CHOOSE_CLOUD_STORE)
        }
    }

    private fun updateCloudStoreChoose(cloudStoreType: Int) {
        var cloudStoreTypeName = "阿里云"
        var fragment: Fragment? = null
        when (cloudStoreType) {
            StoreType.ALIYUN -> {
                cloudStoreTypeName = "阿里云"
                fragment = AliyunConfigFragment()
            }
            StoreType.TENGXUNYUN -> {
                cloudStoreTypeName = "腾讯云"

            }
            StoreType.QNIUYUN -> {
                cloudStoreTypeName = "七牛云"
                fragment =
                    QiNiuYunConfigFragment()
            }
            StoreType.BAIDUWANGPAN -> {
                cloudStoreTypeName = "百度网盘"
            }
        }
        cloudStoreChoose.detailText = cloudStoreTypeName
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }

    }

    fun configSuccessForResult() {
        setResult(Activity.RESULT_OK, Intent().apply { putExtra(CONFIG_SUCCESS, true) })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_CLOUD_STORE && resultCode == Activity.RESULT_OK) {
            cloudStoreType = data?.getIntExtra(CLOUD_STORE, StoreType.ALIYUN)!!
            updateCloudStoreChoose(cloudStoreType)
        }
    }
}
