package online.mengchen.collectionhelper.base

interface BaseView {
    /**
     * 初始化界面传入的参数
     */
    fun initParam()

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 初始化界面观察者监听
     */
    fun initViewObserver()
}