package pl.codziennemotto.services.authentication

import org.springframework.stereotype.Component
import pl.codziennemotto.data.dao.UserDao
import pl.codziennemotto.data.dto.User
import pl.codziennemotto.security.PasswordTool

@Component
class AuthenticationService(private val userDao: UserDao, private val passwordTool: PasswordTool) {
    fun tryAuthenticate(usernameOrEmail: String, rawPassword: String): User? {
        val user = userDao.getByUsernameOrEmail(usernameOrEmail) ?: return null
        return if (passwordTool.matches(rawPassword, user.passwordHash)) user else null
    }
}