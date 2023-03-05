package pl.codziennemotto.services.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        val before = textDao.findAll().count()
        assertTrue(textService.deleteText(0, 121, User().apply { this.id = 1 }))
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