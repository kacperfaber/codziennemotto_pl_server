package pl.codziennemotto.services.reader

import org.springframework.stereotype.Component
import pl.codziennemotto.data.dao.ReaderDao
import pl.codziennemotto.data.dto.Reader
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User

@Component
class ReaderService(private val readerDao: ReaderDao) {
    fun isUserReader(user: User, textSet: TextSet): Boolean {
        return readerDao.getByUserAndTextSet(user, textSet) != null
    }

    fun createReader(user: User, textSet: TextSet): Reader = readerDao.save(Reader().apply {
        this.user = user
        this.textSet = textSet
    })
}