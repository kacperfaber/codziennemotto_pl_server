package pl.codziennemotto.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder

@Configuration
class PasswordEncoderConfiguration {
    @Value("password_encoder.secret")
    lateinit var passwordEncoderSecret: String

    @Bean
    fun passwordEncoder() = Pbkdf2PasswordEncoder(passwordEncoderSecret, 32, 2000, 256)
}