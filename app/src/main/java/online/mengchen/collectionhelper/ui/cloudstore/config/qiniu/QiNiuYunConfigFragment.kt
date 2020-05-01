package online.mengchen.collectionhelper.ui.cloudstore.config.qiniu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import online.mengchen.collectionhelper.BR
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.base.BaseFragment
import online.mengchen.collectionhelper.databinding.FragmentQiniuConfigBinding

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
        container: ViewGroup,
        savedInstanceState: Bundle
    ): Int {
        return R.layout.fragment_qiniu_config
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

}