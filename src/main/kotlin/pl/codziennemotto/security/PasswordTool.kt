package pl.codziennemotto.security

import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

interface PasswordTool {
    fun matches(rawPassword: String, encodedPassword: String): Boolean
    fun encode(rawPassword: String): String
}

@Profile("prod")
@Component
class ProdPasswordTool(private val passwordEncoder: PasswordEncoder) : PasswordTool {
    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }

    override fun encode(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword)
    }
}

@Profile("test")
@Component
class TestPasswordTool : PasswordTool {
    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return rawPassword == encodedPassword
    }

    override fun encode(rawPassword: String): String {
        return rawPassword
    }
}