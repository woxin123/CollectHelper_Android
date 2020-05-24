package online.mengchen.collectionhelper.base

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.properties.Delegates

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(), BaseView {

    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM
    private var mViewModelId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
        initViewDataBinding(savedInstanceState)
        initData()
        // 页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObserver()
    }

    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        mBinding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))
        val viewModel = initViewModel()
        mViewModel = if (viewModel == null) {

            val type: Type = javaClass.genericSuperclass!!
            val modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<VM>
            } else {
                BaseViewModel::class.java
            }
            createViewModel(this, modelClass) as VM
        } else {
            viewModel
        }
        mViewModelId = initViewModelId()
        mBinding.setVariable(mViewModelId, mViewModel)
        mBinding.lifecycleOwner = this
        lifecycle.addObserver(mViewModel)
    }

    /**
     * 初始化参数
     */
    override fun initParam() {

    }

    /**
     * 初始化数据
     */
    override fun initData() {

    }


    override fun initViewObserver() {
        mViewModel.toastMessage.observe(this, Observer {
            Toast.makeText(this, getString(it), Toast.LENGTH_SHORT).show()
        })
    }


    /**
     * 初始化根布局
     * @return 布局 layout id
     */
    abstract fun initContentView(savedInstanceState: Bundle?): Int

    /**
     * 初始化 ViewModel
     */
    fun initViewModel(): VM? {
        return null
    }

    /**
     * 初始化 viewModel id
     */
    abstract fun initViewModelId(): Int

    /**
     * 创建 ViewModel
     */
    fun <T : BaseViewModel> createViewModel(activity: AppCompatActivity, clazz: Class<T>): T {
        return ViewModelProvider(activity).get(clazz)
    }

}