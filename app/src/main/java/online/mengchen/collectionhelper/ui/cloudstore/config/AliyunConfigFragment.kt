package online.mengchen.collectionhelper.ui.cloudstore.config

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_aliyun_config.*

import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.sp.StatusProperties
import online.mengchen.collectionhelper.databinding.FragmentAliyunConfigBinding
import online.mengchen.collectionhelper.ui.cloudstore.CloudStoreConfigActivity

class AliyunConfigFragment : Fragment() {

    private lateinit var mBinding: FragmentAliyunConfigBinding
    private lateinit var mViewModel: AliyunConfigViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_aliyun_config, container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this).get(AliyunConfigViewModel::class.java)
        mBinding.viewModel = mViewModel
        initListener()
        initObserver()
    }

    private fun initObserver() {
        mViewModel.getAliyunConfig().observe(this.viewLifecycleOwner, Observer {
            if (it != null) {
                mViewModel.setAliyunConfig(it)
            }
        })
        mViewModel.toastMessage.observe(this.viewLifecycleOwner, Observer {event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.aliyunCommitRes.observe(this.viewLifecycleOwner, Observer {
            if (it) {
                mViewModel.sendMessage(R.string.save_success)
                StatusProperties.setCloudStore(this.activity!!, StoreType.ALIYUN)
                val activity = this.activity
                if (activity is CloudStoreConfigActivity) {
                    activity.configSuccessForResult()
                }
                this.activity?.finish()
            } else {
                mViewModel.sendMessage(R.string.save_fail)
            }
        })
    }

    private fun initListener() {
        commit.setOnClickListener {
            mViewModel.commit()
        }
    }

}
