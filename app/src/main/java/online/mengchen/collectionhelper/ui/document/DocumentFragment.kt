package online.mengchen.collectionhelper.ui.document

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
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction
import kotlinx.android.synthetic.main.fragment_document.*

import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.data.file.CloudStoreInstance
import online.mengchen.collectionhelper.data.file.aliyun.AliyunConfiguration
import online.mengchen.collectionhelper.databinding.FragmentDocumentBinding
import online.mengchen.collectionhelper.ui.custom.CustomProgressDialog


class DocumentFragment : Fragment() {

    private lateinit var mBinding: FragmentDocumentBinding
    private lateinit var mViewModel: DocumentViewModel
    private lateinit var mProgressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_document, container, false)
        mBinding.lifecycleOwner = this
        mViewModel = ViewModelProvider(this).get(DocumentViewModel::class.java)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        recyclerView.adapter = DocumentAdapter(mViewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        mProgressDialog = CustomProgressDialog(this.context!!, "文件下载中...")
        mProgressDialog.dismiss()
        pullLayout.setActionListener { pullAction ->
            pullLayout.postDelayed({
                mViewModel.loadData()
                pullLayout.finishActionRun(pullAction)
            }, 300L)
        }
        val swipeAction = QMUIRVItemSwipeAction(true, object: QMUIRVItemSwipeAction.Callback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)
            }

            override fun getSwipeDirection(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return QMUIRVItemSwipeAction.SWIPE_LEFT
            }

            override fun onClickAction(
                swipeAction: QMUIRVItemSwipeAction?,
                selected: RecyclerView.ViewHolder?,
                action: QMUISwipeAction?
            ) {
                super.onClickAction(swipeAction, selected, action)
            }
        })
        swipeAction.attachToRecyclerView(recyclerView)
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

        mViewModel.clickItem.observe(this.viewLifecycleOwner, Observer {event ->
            event.getContentIfNotHandled()?.let {
                mProgressDialog.dismiss()
                if (it.filePath == null) {
                    mViewModel.sendMessage(R.string.file_download_error)
                } else {
                    val intent = Intent(this.activity, DisplayDocumentActivity::class.java)
                    intent.putExtra(DisplayDocumentActivity.FILE_PATH, it.filePath)
                    startActivity(intent)
                }
            }
        })
        mViewModel.showProgress.observe(this.viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                mProgressDialog.show()
                mProgressDialog.setMaxProgress(100)
            }
        })
        mViewModel.updateProgress.observe(this.viewLifecycleOwner, Observer {
            mProgressDialog.setProgress(it)
        })
    }

}
