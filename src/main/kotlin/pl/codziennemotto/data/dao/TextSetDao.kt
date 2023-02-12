package pl.codziennemotto.data.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User

interface TextSetDao : JpaRepository<TextSet, Int> {
    fun getByIdAndOwner(id: Int, owner: User): TextSet?

    @Query(value = "SELECT S FROM TextSet S INNER JOIN S.readers R WHERE R.user = :user OR S.owner = :user")
    fun getByIdAndUserAllowed(id: Int, user: User): TextSet?
}