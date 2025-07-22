import java.time.LocalDateTime


/**
 * pure domain model
 */

// dto 클래스
class UserDto (

    var userId: Long,

    var name: String,

    var phoneNumber: Int,

    val password : String,

    var email: String,

    var ci: String,

    var birth: Int,

    var isDelete: Boolean,

    var emailVerified: Boolean,

    var createAt: LocalDateTime,

    var updateAt: LocalDateTime,

    var deleteAt: LocalDateTime,

    val role: String = "USER" // 기본값으로 줬음. 권한별로 확장이 필요함
)