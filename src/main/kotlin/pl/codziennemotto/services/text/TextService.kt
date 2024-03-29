package pl.codziennemotto.services.text

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pl.codziennemotto.data.dao.JoinLinkDao
import pl.codziennemotto.data.dao.ReaderDao
import pl.codziennemotto.data.dao.TextDao
import pl.codziennemotto.data.dao.TextSetDao
import pl.codziennemotto.data.dto.Reader
import pl.codziennemotto.data.dto.Text
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User
import pl.codziennemotto.services.reader.ReaderService
import pl.codziennemotto.services.user.UserService
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Component
class TextService(
        val textSetDao: TextSetDao,
        private val joinLinkDao: JoinLinkDao,
        private val readerService: ReaderService,
        private val textDao: TextDao,
        private val userService: UserService,
        private val readerDao: ReaderDao
) {
    enum class JoinWithCodeResult(val value: Int) {
        AlreadyJoined(0),
        TextSetNotFound(1),
        CodeNotFound(2),
        OK(3)
    }

    data class JoinWithCode(val reader: Reader?, val result: JoinWithCodeResult)

    private fun joinWithCodeResult(reader: Reader?, result: JoinWithCodeResult) = JoinWithCode(reader, result)

    fun joinWithCode(textSetId: Int, authorizedUser: User, code: String): JoinWithCode {
        val textSet = textSetDao.findByIdOrNull(textSetId)
                ?: return joinWithCodeResult(null, JoinWithCodeResult.TextSetNotFound)
        if (readerService.isUserReader(authorizedUser, textSet)) return joinWithCodeResult(null, JoinWithCodeResult.AlreadyJoined)
        val joinLink =
                joinLinkDao.getByCodeAndTextSetId(code, textSetId)
                        ?: return joinWithCodeResult(null, JoinWithCodeResult.CodeNotFound)
        val reader = readerService.createReader(authorizedUser, textSet)
        joinLinkDao.delete(joinLink)
        return joinWithCodeResult(reader, JoinWithCodeResult.OK)
    }

    fun getTextSet(id: Int, authorizedUser: User): TextSet? {
        return textSetDao.getByIdAndUserAllowed(id, authorizedUser)
    }

    fun getTextSetReaders(id: Int, authorizedUser: User): List<Reader> =
            textSetDao.getReadersByIdAndUser(id, authorizedUser)

    fun getAllTexts(id: Int, authorizedUser: User) = textSetDao.getAllTextsByIdAndUser(id, authorizedUser)

    private fun isTextSetOwner(textSet: TextSet, authorizedUser: User): Boolean {
        return textSet.ownerId!! == authorizedUser.id
    }

    private fun isTextSetReader(textSet: TextSet, authorizedUser: User): Boolean {
        return textSet.readers.any {it.userId == authorizedUser.id}
    }

    fun getAllVisibleTexts(id: Int, authorizedUser: User): List<Text>? {
        val textSet = getTextSet(id, authorizedUser) ?: return null
        return if (isTextSetOwner(textSet, authorizedUser)) getAllTexts(id, authorizedUser) else getPastTexts(id, authorizedUser)
    }

    fun getPastTexts(id: Int, authorizedUser: User): List<Text> =
            textSetDao.getPastTextsByIdAndUser(id, authorizedUser, LocalDate.now())

    private fun generateText(text: String, date: LocalDate?, order: Int, textSet: TextSet): Text = Text().apply {
        this.order = order
        this.textSet = textSet
        this.date = date
        this.text = text
    }

    fun addText(id: Int, authorizedUser: User, text: String, date: LocalDate?, order: Int): Text? {
        val textSet = textSetDao.getByIdAndOwner(id, authorizedUser) ?: return null
        return textDao.save(generateText(text, date, order, textSet))
    }

    private fun generateTextSet(title: String, description: String, user: User) = TextSet().apply {
        this.description = description
        this.owner = user
        this.title = title
    }

    fun createNewTextSet(user: User, title: String, description: String): TextSet =
            textSetDao.save(generateTextSet(title, description, user))

    fun deleteText(setId: Int, textId: Int, authorizedUser: User): Boolean {
        textDao.getByIdAndTextSetIdAndTextSetOwner(textId, setId, authorizedUser) ?: return false
        textDao.deleteAllByIdInBatch(mutableListOf(textId))
        return true
    }

    fun getAllByOwner(user: User): List<TextSet> = textSetDao.getAllByOwner(user)

    fun getAllByReader(user: User): List<TextSet> = textSetDao.getAllByReader(user)

    fun pickDailyText(textSet: TextSet): Text? {
        val text = textDao.findFirstByShownIsNullAndTextSetOrderByOrderAsc(textSet) ?: return null
        return textDao.save(text.apply { shown = LocalDate.now() })
    }

    fun getDailyText(textSet: TextSet): String? {
        val text = textDao.getByTextSetAndShown(textSet, LocalDate.now())
        return text?.text ?: pickDailyText(textSet)?.text
    }

    fun getAllByUser(user: User): List<TextSet> = textSetDao.getAllByUser(user)

    fun getReadersOfTextSet(textSetId: Int, authorizedUser: User): List<Reader>? {
        return getTextSet(textSetId, authorizedUser)?.readers
    }

    data class ReaderIncludeUser(val reader: Reader, val userName: String, val userId: Int)

    fun getReadersAsTextSetOwner(textSetId: Int, authorizedUser: User): Iterable<Reader>? {
        val textSet = textSetDao.getByIdAndOwner(textSetId, authorizedUser) ?: return null
        return textSet.readers
    }

    fun getReadersIncludeUsers(authorizedUser: User, textSetId: Int): Iterable<ReaderIncludeUser>? {
        val readers = getReadersAsTextSetOwner(textSetId, authorizedUser) ?: return null
        return readers.map {
            val user = userService.getUser(it.userId!!)!!
            return@map ReaderIncludeUser(it, user.username, user.id!!)
        }
    }

    fun deleteTextSet(authorizedUser: User, textSetId: Int): Boolean {
        val textSet = textSetDao.getByIdAndOwner(textSetId, authorizedUser) ?: return false
        textSetDao.deleteAllByIdInBatch(listOf(textSet.id))
        return true
    }
    fun quit(authorizedUser: User, textSetId: Int): Boolean {
        val textSet = textSetDao.getByIdAndReader(textSetId, authorizedUser) ?: return false
        val reader = readerDao.getByUserAndTextSet(authorizedUser, textSet) ?: return false
        readerDao.deleteAllByIdInBatch(listOf(reader.id))
        return true
    }

    fun getTextByIdAndSetId(authorizedUser: User, textId: Int, setId: Int): Text? {
        val texts = getAllVisibleTexts(setId, authorizedUser) ?: return null
        return texts.firstOrNull {it.id == textId}
    }

    fun deleteReader(authorizedUser: User, textSetId: Int, readerId: Int): Boolean {
        val textSet = textSetDao.getByIdAndOwner(textSetId, authorizedUser) ?: return false
        readerDao.getByIdAndTextSet(readerId, textSet) ?: return false
        readerDao.deleteAllByIdInBatch(listOf(readerId))
        return true
    }

    fun getTextById(authorizedUser: User, textId: Int): Text? {
        val text = textDao.getTextById(textId) ?: return null
        if (isTextSetReader(text.textSet, authorizedUser) && text.shown != null) {
            return text
        }

        if (isTextSetOwner(text.textSet, authorizedUser)){
            return text
        }

        return null
    }

    fun joinWithCode(authorizedUser: User, code: String): Reader? {
        val joinLink = joinLinkDao.getByCode(code) ?: return null
        return readerService.createReader(authorizedUser, joinLink.textSet).apply {
            textSetId = joinLink.textSet.id!!
            userId = authorizedUser.id!!
        }
    }
}