package pl.codziennemotto.services.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.codziennemotto.data.dao.ReaderDao
import pl.codziennemotto.data.dao.TextDao
import pl.codziennemotto.data.dto.Text
import pl.codziennemotto.data.dto.User
import testutils.IntegrationTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest(properties = ["spring.profiles.active=test"])
@IntegrationTest
class TextServiceIntegrationTest {
    @Autowired
    lateinit var textService: TextService

    @Autowired
    lateinit var textDao: TextDao

    @Autowired
    lateinit var readerDao: ReaderDao

    @Test
    fun `deleteText does not throw`() {
        assertDoesNotThrow { textService.deleteText(1, 1, User().apply { this.id = 1 }) }
    }

    @Test
    fun `deleteText deletes the Text from database and returns true`() {
        textDao.save(Text().apply {
            this.id = 121
            this.order = 1
            this.textSetId = 1
        })

        val before = textDao.findAll().count()
        assertTrue(textService.deleteText(1, 121, User().apply { this.id = 1 }))
        val after = textDao.findAll().count()
        assertEquals(before - 1, after)
    }

    @Test
    fun `deleteText returns false if given parameters were wrong`() {
        assertFalse {
            textService.deleteText(1, 1, User().apply { this.id = 0 })
        }

        assertFalse {
            textService.deleteText(20, 1, User().apply { this.id = 1 })
        }

        assertFalse {
            textService.deleteText(1, 20, User().apply { this.id = 1 })
        }
    }
}