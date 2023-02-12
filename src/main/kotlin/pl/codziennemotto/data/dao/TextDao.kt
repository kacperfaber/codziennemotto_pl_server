package pl.codziennemotto.data.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.codziennemotto.data.dto.Text
import pl.codziennemotto.data.dto.User

interface TextDao : JpaRepository<Text, Int> {
    fun getByIdAndTextSetIdAndTextSetOwner(id: Int, textSetId: Int, textSetOwner: User): Text?
}