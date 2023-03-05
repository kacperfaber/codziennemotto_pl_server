package pl.codziennemotto.services.text

import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.internal.verification.Times
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.DirtiesContext
import pl.codziennemotto.data.dao.TextSetDao
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User
import testutils.UnitTest
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringBootTest(properties = ["spring.profiles.active=test"])
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@UnitTest
class TextServiceTest {
    @Autowired
    lateinit var textService: TextService

    @MockBean
    lateinit var textSetDao: TextSetDao

    @Test
    fun `getTextSet returns what userDao returns`() {
        val textSet = TextSet()
        `when`(textSetDao.getByIdAndUserAllowed(anyInt(), any())).thenReturn(textSet)
        assertEquals(textSet, textService.getTextSet(1, User()))
    }

    @Test
    fun `addText returns null if textSetDao returned null`() {
        `when`(textSetDao.getByIdAndOwner(anyInt(), any())).thenReturn(null)
        assertNull(textService.addText(20, User().apply { this.id = 1 }, "Test", null, 0))
    }

    @Test
    fun `addText dont calls textDao-save if textSetDao returned null`() {
        `when`(textSetDao.getByIdAndOwner(anyInt(), any())).thenReturn(null)
        textService.addText(20, User().apply { this.id = 1 }, "Test", null, 0)
        verify(textSetDao, Times(0)).save(any())
    }
}