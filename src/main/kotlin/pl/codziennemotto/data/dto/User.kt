package pl.codziennemotto.data.dto

import BaseDto
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "`user`")
open class User : BaseDto() {
    open lateinit var email: String

    open lateinit var username: String

    @Column(name = "password_hash")
    open lateinit var passwordHash: String

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    open lateinit var readers: MutableList<Reader>

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    open lateinit var textSets: MutableList<TextSet>
}