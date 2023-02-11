package pl.codziennemotto.data.dto

import jakarta.persistence.*

@Entity
@Table(name = "`user`")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int? = null

    open lateinit var email: String

    open lateinit var username: String

    @Column(name = "password_hash")
    open lateinit var passwordHash: String

    @OneToMany(mappedBy = "user")
    open lateinit var readers: MutableList<Reader>

    @OneToMany(mappedBy = "owner")
    open lateinit var textSets: MutableList<TextSet>
}