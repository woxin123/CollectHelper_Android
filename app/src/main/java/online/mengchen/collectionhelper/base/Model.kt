package online.mengchen.collectionhelper.base

interface Model {

    /**
     * ViewModel 销毁时清除 Model，与 ViewModel 共存亡。Model 层同样不会持有声明周期对象
     */
    fun onCleared()
}