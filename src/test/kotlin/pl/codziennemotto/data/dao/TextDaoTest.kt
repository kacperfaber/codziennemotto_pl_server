package pl.codziennemotto.data.dao

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.codziennemotto.data.dto.TextSet
import testutils.IntegrationTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(properties = ["spring.profiles.active=test"])
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@IntegrationTest
class TextDaoTest {
    @Autowired
    lateinit var textDao: TextDao

    @Autowired
    lateinit var textSetDao: TextSetDao

    @Test
    fun `returns not null if null is not expected`() {
        val textSet = textSetDao.getById(0)
        val r = textDao.findFirstByShownIsNullAndTextSetOrderByOrderAsc(textSet)
        assertNotNull(r)
    }

    @Test
    fun `returns expected Text`() {
        val textSet = textSetDao.getById(1)
        val r = textDao.findFirstByShownIsNullAndTextSetOrderByOrderAsc(textSet)
        assertEquals(100, r!!.id)
    }
}