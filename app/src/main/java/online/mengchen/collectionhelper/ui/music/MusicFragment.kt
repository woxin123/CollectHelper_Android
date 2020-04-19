package online.mengchen.collectionhelper.ui.music

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_music_item.*
import kotlinx.android.synthetic.main.music_fragment.*

import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.databinding.MusicFragmentBinding

class MusicFragment : Fragment() {

    companion object {
        fun newInstance() = MusicFragment()
    }

    private lateinit var viewModel: MusicViewModel
    private lateinit var mBinding: MusicFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.music_fragment, container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MusicViewModel::class.java)
        mBinding.viewModel = viewModel
        initView()
        viewModel.start()
    }

    private fun initView() {
        recyclerView.adapter = MusicAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this.activity, RecyclerView.VERTICAL, false)
    }

}
