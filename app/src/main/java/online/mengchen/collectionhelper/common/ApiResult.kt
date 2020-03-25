package online.mengchen.collectionhelper.common

/**
 * 服务端返回的数据
 */
class ApiResult<T> {
    /**
     * status 使用的是 HTTP 的状态码
     * 200 成功
     * 201 创建成功
     * 204 没有内容
     */
    var status: Int = 0

    /**
     * 服务端返回的消息，可能是对成功的描述，也可能是对错误的描述
     */
    var message: String = ""
    /**
     * 服务端返回的数据，可能为 null
     */
    var data: T? = null
    /**
     * 服务端返回的数据
     * - 对错误的描述
     * - 额外的数据
     */
    var extra: Map<String, Any>? = null

    override fun toString(): String {
        return "ApiResult(status=$status, message='$message', data=$data, extra=$extra)"
    }


}