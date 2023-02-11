package pl.codziennemotto.data.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.codziennemotto.data.dto.Text

interface TextDao : JpaRepository<Text, Int> {
    fun getBy()
}