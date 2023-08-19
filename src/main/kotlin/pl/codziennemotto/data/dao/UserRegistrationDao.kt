package pl.codziennemotto.data.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.codziennemotto.data.dto.UserRegistration

interface UserRegistrationDao : JpaRepository<UserRegistration, Int> {
    fun getByEmailAddressAndVerificationCode(emailAddress: String, verificationCode: String): UserRegistration?
}