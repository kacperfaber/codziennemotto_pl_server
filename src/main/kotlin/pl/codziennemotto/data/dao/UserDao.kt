package pl.codziennemotto.data.dao

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pl.codziennemotto.data.dto.User

@Hidden
interface UserDao : JpaRepository<User, Int> {
    fun getByUsername(username: String): User?
    fun getByEmail(email: String): User?

    fun getByUsernameIgnoreCaseOrEmailIgnoreCase(username: String, email: String): User?

    @Query("SELECT * FROM users50 WHERE (username = :x) OR (email = :x) LIMIT 1;", nativeQuery = true)
    fun getByUsernameOrEmail(@Param("x") usernameOrEmail: String): User?
}