package online.mengchen.collectionhelper.ui.cloudstore.config.tencentyun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import online.mengchen.collectionhelper.BR

import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.base.BaseFragment
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.sp.StatusProperties
import online.mengchen.collectionhelper.databinding.FragmentTencentCOSCloudStoreConfigBinding
import online.mengchen.collectionhelper.ui.cloudstore.CloudStoreConfigActivity

class TencentCOSConfigFragment : BaseFragment<FragmentTencentCOSCloudStoreConfigBinding, TencentCOSConfigViewModel>() {
    override fun initContentView(
        inflate: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_tencent_c_o_s_cloud_store_config
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewObserver() {
        super.initViewObserver()
        mViewModel.commitSuccess.observe(this.viewLifecycleOwner, Observer {
            mViewModel.sendToastMessage(R.string.save_success)
            StatusProperties.setCloudStore(this.activity!!, StoreType.TENGXUNYUN)
            val activity = this.activity
            if (activity is CloudStoreConfigActivity) {
                activity.configSuccessForResult()
            }
            activity?.finish()
        })
    }

}
