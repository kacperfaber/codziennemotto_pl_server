package pl.codziennemotto.data.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.codziennemotto.data.dto.Reader
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User

interface ReaderDao : JpaRepository<Reader, Int> {
    fun getByUserAndTextSet(user: User, textSet: TextSet): Reader?
}