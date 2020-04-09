package online.mengchen.collectionhelper.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.fragment_book_mark.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.databinding.FragmentBookMarkBinding
import online.mengchen.collectionhelper.utils.LoginUtils

class BookMarkFragment : Fragment() {

    companion object {
        const val TAG = "BookMarkFragment"
    }

    private lateinit var mBinding: FragmentBookMarkBinding
    private lateinit var mViewModel: BookMarkViewModel
    private lateinit var loading: QMUITipDialog
    private lateinit var adapter: BookMarkAdapter
    private var isLastPage = false
    private var page: Int = 0
    private var pageSize: Int = 10
    private var isFirstInit = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_mark, container, false)
        mViewModel = ViewModelProvider(this).get(BookMarkViewModel::class.java)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstInit && !LoginUtils.isNeedLogin(this.activity!!)) {
            isFirstInit = false
            mViewModel.getBookMarks(true)
            loading.show()
        }
    }

    private fun initView() {
        loading = QMUITipDialog.Builder(this.activity)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).create()
        adapter = BookMarkAdapter()
        mViewModel.bookMarkAdapter = adapter
        mBinding.recyclerView.adapter = adapter
        adapter.listener = object : BookMarkAdapter.OnItemClickListener {
            override fun onClick(bookMarkInfo: BookMarkInfo) {
                startActivity(
                    Intent(
                        this@BookMarkFragment.activity,
                        BookMarkViewActivity::class.java
                    ).apply {
                        putExtra(Constant.BOOK_MARK_URL, bookMarkInfo.url)
                    })
            }
        }
        mBinding.recyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        refresh.setDisableContentWhenRefresh(true)
        refresh.setEnableScrollContentWhenRefreshed(true)
    }

    private fun initListener() {
        mViewModel.refreshBookMarksInfo.observe(this.viewLifecycleOwner, Observer {
            if (it.status != 200) {
                Toast.makeText(this.activity, "书签获取出错", Toast.LENGTH_SHORT).show()
            } else {
                adapter.data = mutableListOf(*it.data?.content?.toTypedArray()!!)
            }
            isLastPage = it.data?.last!!
            loading.dismiss()
            refresh.finishRefresh()
        })
        mViewModel.loadMoreLiveData.observe(this.viewLifecycleOwner, Observer {
            if (it.status != 200) {
                Toast.makeText(this.activity, "书签获取出错", Toast.LENGTH_SHORT).show()
            } else {
                adapter.addAll(it.data?.content!!)
            }
            isLastPage = it.data?.last!!
            refresh.finishLoadMore()
        })
        refresh.setOnRefreshListener {
//            loading.show()
            mViewModel.getBookMarks(true)
        }
        refresh.setOnLoadMoreListener {
            if (isLastPage) {
                Toast.makeText(this.activity, "已经到底了", Toast.LENGTH_SHORT).show()
            }
            page++
            mViewModel.getBookMarks(false, page, pageSize)
        }
    }


}