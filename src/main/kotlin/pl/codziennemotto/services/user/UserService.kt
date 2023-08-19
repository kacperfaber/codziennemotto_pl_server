package pl.codziennemotto.services.user

import org.springframework.stereotype.Component
import pl.codziennemotto.data.dao.UserDao
import pl.codziennemotto.data.dto.User
import kotlin.jvm.optionals.getOrNull

@Component
class UserService(private val userDao: UserDao) {
    @OptIn(ExperimentalStdlibApi::class)
    fun getUser(id: Int): User? {
        return userDao.findById(id).getOrNull()
    }

    fun createUser(username: String, emailAddress: String, passwordHash: String): User? {
        return userDao.save(User().apply {
            this.username = username
            this.email = emailAddress
            this.passwordHash = passwordHash
        })
    }

    fun isUsernameAndEmailNotTaken(username: String, email: String): Boolean {
        return userDao.getByUsernameIgnoreCaseOrEmailIgnoreCase(username, email) == null
    }
}