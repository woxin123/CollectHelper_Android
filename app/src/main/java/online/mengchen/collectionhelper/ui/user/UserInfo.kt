package online.mengchen.collectionhelper.ui.user

class UserInfo {
    private var username: String? = null
    private var avatar: String? = null
    private var phoneNumber: String? = null

}

data class LoginUser (
    var username: String,
    var password: String
)

data class UserData(
    var userId: Long = -1,
    var username: String = "",
    var phone: String = "",
    var avatar: String = ""

)

/**
 * 注册时的用户
 */
data class RegisterUser (
    val username: String,
    var password: String
)