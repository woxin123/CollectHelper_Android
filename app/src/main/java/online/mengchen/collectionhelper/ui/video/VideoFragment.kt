package online.mengchen.collectionhelper.ui.video

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_document.*

import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.data.file.CloudStoreInstance
import online.mengchen.collectionhelper.data.file.aliyun.AliyunConfiguration
import online.mengchen.collectionhelper.databinding.FragmentVideoBinding
import online.mengchen.collectionhelper.ui.custom.CustomProgressDialog
import online.mengchen.collectionhelper.ui.document.DisplayDocumentActivity
import online.mengchen.collectionhelper.ui.document.DocumentAdapter


class VideoFragment : Fragment() {

    private lateinit var mBinding: FragmentVideoBinding
    private lateinit var mViewModel: VideoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false)
        mBinding.lifecycleOwner = this
        mViewModel = ViewModelProvider(this).get(VideoViewModel::class.java)
        mBinding.viewModel = mViewModel
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        mViewModel.start()
    }

    private fun initView() {
        recyclerView.adapter = VideoAdapter(mViewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObserver() {
//        mViewModel.aliyunConfig.observe(this.viewLifecycleOwner, Observer {
//            if (it == null) {
//                mViewModel.sendMessage(R.string.cloud_store_config_get_error)
//            } else {
//                CloudStoreInstance.getAliyunInstance(
//                    AliyunConfiguration(it.accessKey, it.secretKey, it.bucket),
//                    mViewModel.viewModelScope
//                ).observe(this.viewLifecycleOwner, Observer {
//                    if (it == null) {
//                        mViewModel.sendMessage(R.string.cloud_store_init_error)
//                    } else {
//                        mViewModel.cloudStore = it
//                        mViewModel.start()
//                    }
//                })
//            }
//        })

        mViewModel.toastMessage.observe(this.viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(this.activity, resources.getString(it), Toast.LENGTH_SHORT).show()
            }
        })

    }

}
