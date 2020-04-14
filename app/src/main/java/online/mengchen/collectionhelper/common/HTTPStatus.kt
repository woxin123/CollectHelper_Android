package online.mengchen.collectionhelper.common

/**
 * 常用 HTTP 状态码
 */
enum class HTTPStatus(val code: Int) {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500)
}