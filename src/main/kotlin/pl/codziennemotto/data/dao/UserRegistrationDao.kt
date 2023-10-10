package pl.codziennemotto.data.dao

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.data.jpa.repository.JpaRepository
import pl.codziennemotto.data.dto.UserRegistration

@Hidden
interface UserRegistrationDao : JpaRepository<UserRegistration, Int> {
    fun getByEmailAddressAndVerificationCode(emailAddress: String, verificationCode: String): UserRegistration?
}