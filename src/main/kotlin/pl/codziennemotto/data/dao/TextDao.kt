package pl.codziennemotto.data.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.codziennemotto.data.dto.Text
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User
import java.time.LocalDate

interface TextDao : JpaRepository<Text, Int> {
    fun getByIdAndTextSetIdAndTextSetOwner(id: Int, textSetId: Int, textSetOwner: User): Text?
    fun getByTextSetAndShown(textSet: TextSet, shown: LocalDate): Text?
    fun findFirstByShownIsNullAndTextSetOrderByOrderAsc(textSet: TextSet): Text?
}