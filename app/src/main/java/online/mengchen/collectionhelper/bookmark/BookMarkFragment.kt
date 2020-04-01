package online.mengchen.collectionhelper.bookmark

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_book_mark.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.databinding.FragmentBookMarkBinding

class BookMarkFragment : Fragment() {

    companion object {
        const val TAG = "BookMarkFragment"
    }

    private lateinit var mBinding: FragmentBookMarkBinding
    private lateinit var mViewModel: BookMarkViewModel

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
        val adapter = BookMarkAdapter()
        mViewModel.bookMarkAdapter = adapter
        mBinding.recyclerView.adapter = adapter
        adapter.listener = object : BookMarkAdapter.OnItemClickListener {
            override fun onClick(bookMark: BookMark) {
                startActivity(
                    Intent(
                        this@BookMarkFragment.activity,
                        BookMarkViewActivity::class.java
                    ).apply {
                        putExtra(Constant.BOOK_MARK_URL, bookMark.url)
                    })
            }
        }
        mBinding.recyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        initListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (mViewModel.bookMarkAdapter.data.isEmpty()) {
            //        mViewModel.loginSuccess.observe(viewLifecycleOwner, Observer {
            mViewModel.getBookMarks(true).observe(viewLifecycleOwner, Observer {
                if (it.data == null) {

//                Toast.makeText(this@BookMarkFragment, "请稍后再试", Toast.LENGTH_SHORT).show()
                } else {
                    it.data!!.content.let { it1 ->
                        mViewModel.addBooMarks(it1)
                        Log.d(TAG, it1.toString())
                    }
                }
            })
//    })
        }

    }

    fun initListener() {
        refresh.setOnRefreshListener {
            mViewModel.getBookMarks(true).observe(this.viewLifecycleOwner, Observer {
                if (it.data == null) {
                    Toast.makeText(this@BookMarkFragment.activity, "书签获取出错", Toast.LENGTH_SHORT).show()
                } else {
                    it.data!!.content.let { bookMarks ->
                        mViewModel.addBooMarks(bookMarks)
                    }
                }
                refresh.finishRefresh()
            })
        }
    }


}