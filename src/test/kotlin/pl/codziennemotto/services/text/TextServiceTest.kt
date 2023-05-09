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
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

    @Test
    fun `deleteTextSet returns true if textSetDao-getByIdAndOwner returned not null`() {
        `when`(textSetDao.getByIdAndOwner(anyInt(), any())).thenReturn(TextSet().apply { id = 1 })
        assertTrue { textService.deleteTextSet(User(), 0) }
    }

    @Test
    fun `deleteTextSet returns false if textSetDao-getByIdAndOwner returned null`() {
        `when`(textSetDao.getByIdAndOwner(anyInt(), any())).thenReturn(null)
        assertFalse { textService.deleteTextSet(User(), 0) }
    }

    @Test
    fun `deleteTextSet calls textSetDao-getByIdAndOwner once`() {
        assertFalse { textService.deleteTextSet(User(), 0) }
        verify(textSetDao, Times(1)).getByIdAndOwner(anyInt(), any())
    }

    @Test
    fun `deleteTextSet calls textSetDao-getByIdAndOwner with expected authorizedUser`() {
        val user = User().apply { id = 5 }
        assertFalse { textService.deleteTextSet(user, 0) }
        verify(textSetDao).getByIdAndOwner(anyInt(), org.mockito.kotlin.eq(user))
    }

    @Test
    fun `deleteTextSet calls textSetDao-getByIdAndOwner with expected textSetId`() {
        val value = 68
        assertFalse { textService.deleteTextSet(User(), value) }
        verify(textSetDao).getByIdAndOwner(org.mockito.kotlin.eq(value), any())
    }
}