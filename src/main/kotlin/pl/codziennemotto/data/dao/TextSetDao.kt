package pl.codziennemotto.data.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import pl.codziennemotto.data.dto.Reader
import pl.codziennemotto.data.dto.Text
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User
import java.time.LocalDateTime

interface TextSetDao : JpaRepository<TextSet, Int> {
    fun getByIdAndOwner(id: Int, owner: User): TextSet?

    @Query(value = "SELECT S FROM TextSet S LEFT JOIN S.readers R WHERE (R.user = :user OR S.owner = :user) AND S.id = :id")
    fun getByIdAndUserAllowed(id: Int, user: User): TextSet?

    @Query(value = "SELECT S.readers FROM TextSet S WHERE S.owner = :user AND S.id = :id")
    fun getReadersByIdAndUser(id: Int, user: User): MutableList<Reader>

    @Query(value = "SELECT S.texts FROM TextSet S WHERE S.owner = :user AND S.id = :id")
    fun getAllTextsByIdAndUser(id: Int, user: User): MutableList<Text>

    @Query(value = "SELECT S.texts FROM TextSet S LEFT JOIN S.texts T LEFT JOIN S.readers R WHERE S.id = :id AND (S.owner = :user OR R.user = :user) AND T.date <= :today")
    fun getPastTextsByIdAndUser(id: Int, user: User, today: LocalDateTime): MutableList<Text>

    @Query(value = "SELECT S FROM TextSet S WHERE S.owner = :user")
    fun getAllByOwner(user: User): List<TextSet>

    @Query(value = "SELECT S FROM TextSet S JOIN S.readers R WHERE R.user = :user")
    fun getAllByReader(user: User): List<TextSet>
}