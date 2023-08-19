package pl.codziennemotto.services.user.registration

import org.springframework.stereotype.Component
import pl.codziennemotto.data.dao.UserDao
import pl.codziennemotto.data.dao.UserRegistrationDao
import pl.codziennemotto.data.dto.UserRegistration
import pl.codziennemotto.security.PasswordTool
import pl.codziennemotto.services.email.verification.VerificationEmailSender
import pl.codziennemotto.services.user.UserService

@Component
class UserRegistrationService(private val emailSender: VerificationEmailSender, private val userService: UserService, private val verificationCodeGenerator: VerificationCodeGenerator, private val passTool: PasswordTool, private val userRegistrationDao: UserRegistrationDao, private val userDao: UserDao) {
    fun register(username: String, emailAddress: String, password: String): RegisterResult {
        if (!userService.isUsernameAndEmailNotTaken(username, emailAddress)) return RegisterResult.EmailOrUsernameTaken

        val code = verificationCodeGenerator.generate()

        userRegistrationDao.save(UserRegistration().apply {
            this.username = username
            this.emailAddress = emailAddress
            this.passwordHash = passTool.encode(password)
            this.verificationCode = code
        })

        emailSender.send(emailAddress, code)

        return RegisterResult.Ok
    }

    fun confirm(emailAddress: String, code: String): Boolean {
        val reg = userRegistrationDao.getByEmailAddressAndVerificationCode(emailAddress, code) ?: return false
        userService.createUser(reg.username, reg.emailAddress, reg.passwordHash)
        return true
    }
}