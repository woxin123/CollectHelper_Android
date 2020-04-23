package online.mengchen.collectionhelper.ui.cloudstore

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.android.synthetic.main.activity_cloud_store_choose.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.ui.cloudstore.CloudStoreConfigActivity.Companion.CHOOSE_CLOUD_STORE
import online.mengchen.collectionhelper.ui.cloudstore.CloudStoreConfigActivity.Companion.CLOUD_STORE

class CloudStoreChooseActivity : AppCompatActivity() {

    private lateinit var checked: ImageView
    private lateinit var aliyun: QMUICommonListItemView
    private lateinit var tengxunyun: QMUICommonListItemView
    private lateinit var qiniuyun: QMUICommonListItemView
    private lateinit var baiduwangpan: QMUICommonListItemView
    private var choose: QMUICommonListItemView? = null
    private var chooseCloudStore: Int = 0
    private val result = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_store_choose)
        initData()
        initView()
        choose(chooseCloudStore)
    }

    private fun initData() {
        chooseCloudStore = intent.getIntExtra(CLOUD_STORE, StoreType.ALIYUN)
    }

    private fun initView() {
        checked = ImageView(this).apply { this.setImageResource(R.drawable.ic_check_black_24dp) }
        topBar.setTitle("选择云存储")
        topBar.addLeftImageButton(R.drawable.top_bar_back, true, R.id.qmui_topbar_item_left_back)
            .setOnClickListener {
                finish()
            }
        aliyun = cloudStores.createItemView("阿里云")
        aliyun.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM
        choose = aliyun
        tengxunyun = cloudStores.createItemView("腾讯云")
        tengxunyun.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM
        qiniuyun = cloudStores.createItemView("七牛云")
        qiniuyun.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM
        baiduwangpan = cloudStores.createItemView("百度网盘")
        baiduwangpan.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM
        QMUIGroupListView.newSection(this)
            .addItemView(aliyun) {
                choose(StoreType.ALIYUN)
            }.addItemView(tengxunyun) {
                choose(StoreType.TENGXUNYUN)
            }.addItemView(qiniuyun) {
                choose(StoreType.QNIUYUN)
            }.addItemView(baiduwangpan) {
                choose(StoreType.BAIDUWANGPAN)
            }.setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(this, 16), 0)
            .addTo(cloudStores)

    }

    private fun choose(@StoreType.TypeStore storeType: Int) {
        chooseCloudStore = storeType
        when (storeType) {
            StoreType.ALIYUN -> {
                checked(aliyun)
            }
            StoreType.TENGXUNYUN -> {
                checked(tengxunyun)
            }
            StoreType.QNIUYUN -> {
                checked(qiniuyun)
            }
            StoreType.BAIDUWANGPAN -> {
                checked(baiduwangpan)
            }
        }
        this.result.putExtra(CLOUD_STORE, storeType)
    }

    private fun checked(item: QMUICommonListItemView) {
        choose?.accessoryContainerView?.removeView(checked)
        item.addAccessoryCustomView(checked)
        choose = item
    }

    override fun finish() {
        setResult(Activity.RESULT_OK, result)
        super.finish()
    }
}
