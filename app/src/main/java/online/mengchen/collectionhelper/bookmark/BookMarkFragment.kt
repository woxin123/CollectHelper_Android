package online.mengchen.collectionhelper.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.databinding.FragmentBookMarkBinding

class BookMarkFragment: Fragment() {

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
        mViewModel.loadData()
        val adapter = BookMarkAdapter()
        mViewModel.bookMarkAdapter = adapter
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }




}