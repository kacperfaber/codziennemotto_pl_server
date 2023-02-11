package pl.codziennemotto.data.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.codziennemotto.data.dto.TextSet

interface TextSetDao : JpaRepository<TextSet, Int> {
}