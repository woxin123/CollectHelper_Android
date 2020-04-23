package online.mengchen.collectionhelper.ui.cloudstore.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.databinding.FragmentQiniuConfigBinding

class QiNiuYunConfigFragment : Fragment() {

    private lateinit var mBinding: FragmentQiniuConfigBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_qiniu_config, container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

}