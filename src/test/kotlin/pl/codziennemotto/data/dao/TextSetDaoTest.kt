package pl.codziennemotto.data.dao

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.codziennemotto.data.dto.User
import testutils.IntegrationTest
import testutils.UnitTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(properties = ["spring.profiles.active=test"])
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@IntegrationTest
@UnitTest
class TextSetDaoTest {
    @Autowired
    lateinit var textSetDao: TextSetDao

    @Test
    fun `getByIdAndOwner returns expected data from database`() {
        assertNotNull(textSetDao.getByIdAndOwner(0, User().apply { this.id = 1 }))
        assertNull(textSetDao.getByIdAndOwner(0, User().apply { this.id = 2 }))
        assertEquals("HelloWorld!", textSetDao.getByIdAndOwner(0, User().apply { this.id = 1 })?.title)
    }
}