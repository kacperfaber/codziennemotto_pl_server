package pl.codziennemotto.services.text

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pl.codziennemotto.data.dao.JoinLinkDao
import pl.codziennemotto.data.dao.ReaderDao
import pl.codziennemotto.data.dao.TextSetDao
import pl.codziennemotto.data.dto.Reader
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User
import pl.codziennemotto.services.reader.ReaderService

@Component
class TextService(
    private val textSetDao: TextSetDao,
    private val joinLinkDao: JoinLinkDao,
    private val readerDao: ReaderDao,
    private val readerService: ReaderService
) {
    enum class JoinWithCodeResult(val value: Int) {
        AlreadyJoined(0),
        TextSetNotFound(1),
        CodeNotFound(2),
        OK(3)
    }

    data class JoinWithCode(val reader: Reader?, val result: JoinWithCodeResult) {

    }

    private fun joinWithCodeResult(reader: Reader?, result: JoinWithCodeResult) = JoinWithCode(reader, result)

    fun joinWithCode(textSetId: Int, authorizedUser: User, code: String): JoinWithCode {
        val textSet = textSetDao.findByIdOrNull(textSetId) ?: return joinWithCodeResult(null, JoinWithCodeResult.TextSetNotFound)
        if (readerService.isUserReader(authorizedUser, textSet)) return joinWithCodeResult(null, JoinWithCodeResult.AlreadyJoined)
        val joinLink = joinLinkDao.getByCodeAndTextSetId(code, textSetId) ?: return joinWithCodeResult(null, JoinWithCodeResult.CodeNotFound)
        val reader = readerService.createReader(authorizedUser, textSet)
        joinLinkDao.delete(joinLink)
        return joinWithCodeResult(reader, JoinWithCodeResult.OK)
    }

    fun getTextSet(id: Int, authorizedUser: User): TextSet? = textSetDao.getByIdAndUserAllowed(id, authorizedUser)
}