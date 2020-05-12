package online.mengchen.collectionhelper.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import kotlin.properties.Delegates


abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : Fragment(), BaseView {

    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM
    private var mViewModelId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            initContentView(inflater, container, savedInstanceState),
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 初始化 Databinding 和 ViewModel
        initViewDataBinding()
        initData()
        initViewObserver()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // 抽象方法
    abstract fun initContentView(
        inflate: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int

    abstract fun initVariableId(): Int
    // 抽象方法

    fun initViewModel(): VM? {
        return null
    }

    override fun initParam() {

    }

    override fun initData() {
    }

    override fun initViewObserver() {
        mViewModel.toastMessage.observe(this.viewLifecycleOwner, Observer {
            Toast.makeText(this.activity, getString(it), Toast.LENGTH_SHORT).show()
        })
    }

    // 私有方法
    private fun initViewDataBinding() {
        mViewModelId = initVariableId()
        val viewModel = initViewModel()
        if (viewModel == null) {

            val type: Type = javaClass.genericSuperclass!!
            val modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<VM>
            } else {
                BaseViewModel::class.java
            }
            mViewModel = createViewModel(this, modelClass) as VM
        } else {
            mViewModel = viewModel
        }
        mBinding.setVariable(mViewModelId, mViewModel)
        mBinding.lifecycleOwner = this
        lifecycle.addObserver(mViewModel)
    }

    // 私有方法

    // 共有方法

    fun <T: BaseViewModel> createViewModel(fragment: Fragment, clazz: Class<T>): T {
        return ViewModelProvider(fragment).get(clazz)
    }

    // 共有方法
}
