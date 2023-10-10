package pl.codziennemotto.data.dao

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.data.jpa.repository.JpaRepository
import pl.codziennemotto.data.dto.Reader
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User

@Hidden
interface ReaderDao : JpaRepository<Reader, Int> {
    fun getByUserAndTextSet(user: User, textSet: TextSet): Reader?

    fun getByIdAndTextSet(id: Int, textSet: TextSet): Reader?
}