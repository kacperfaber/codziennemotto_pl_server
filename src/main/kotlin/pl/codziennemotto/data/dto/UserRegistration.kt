package pl.codziennemotto.data.dto

import BaseDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "user_registration")
class UserRegistration : BaseDto(){
    @Column(unique = true)
    lateinit var username: String

    @Column
    lateinit var passwordHash: String

    @Column(name = "email_address", unique = true)
    lateinit var emailAddress: String

    @Column(name = "verification_code")
    lateinit var verificationCode: String
}