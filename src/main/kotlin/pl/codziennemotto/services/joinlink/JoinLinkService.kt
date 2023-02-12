package pl.codziennemotto.services.joinlink

import org.springframework.stereotype.Component
import pl.codziennemotto.data.dao.JoinLinkDao
import pl.codziennemotto.data.dao.TextSetDao
import pl.codziennemotto.data.dto.JoinLink
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User
import java.time.LocalDateTime

@Component
class JoinLinkService(
    private val textSetDao: TextSetDao,
    private val joinLinkCodeGenerator: JoinLinkCodeGenerator,
    private val joinLinkDao: JoinLinkDao
) {
    private fun generateJoinLinkCode() = joinLinkCodeGenerator.generateCode()

    private fun generateJoinLink(textSet: TextSet) = JoinLink().apply {
        this.textSet = textSet
        this.code = generateJoinLinkCode()
        this.activeUntil = LocalDateTime.now().plusDays(14)
    }

    fun createJoinLink(id: Int, user: User): JoinLink? {
        val textSet = textSetDao.getByIdAndOwner(id, user) ?: return null
        return joinLinkDao.save(generateJoinLink(textSet))
    }
}