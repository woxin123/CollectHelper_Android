package online.mengchen.collectionhelper.ui.cloudstore.config.qiniu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import online.mengchen.collectionhelper.BR
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.base.BaseFragment
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.sp.StatusProperties
import online.mengchen.collectionhelper.databinding.FragmentQiniuConfigBinding
import online.mengchen.collectionhelper.ui.cloudstore.CloudStoreChooseActivity
import online.mengchen.collectionhelper.ui.cloudstore.CloudStoreConfigActivity

class QiNiuYunConfigFragment : BaseFragment<FragmentQiniuConfigBinding, QiNiuYunConfigViewModel>() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_qiniu_config, container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun initContentView(
        inflate: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_qiniu_config
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewObserver() {
        super.initViewObserver()
        mViewModel.completeComplete.observe(this.viewLifecycleOwner, Observer {
            mViewModel.sendToastMessage(R.string.save_success)
            StatusProperties.setCloudStore(this.activity!!, StoreType.QNIUYUN)
            val activity = this.activity
            if (activity is CloudStoreConfigActivity) {
                activity.configSuccessForResult()
            }
            activity?.finish()
        })
    }

}