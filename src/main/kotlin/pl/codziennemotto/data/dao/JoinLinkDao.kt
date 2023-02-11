package pl.codziennemotto.data.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.codziennemotto.data.dto.JoinLink

interface JoinLinkDao : JpaRepository<JoinLink, Int> {
    fun getByCode(code: String): JoinLink?
    fun getByCodeAndTextSetId(code: String, textSetId: Int): JoinLink?
}