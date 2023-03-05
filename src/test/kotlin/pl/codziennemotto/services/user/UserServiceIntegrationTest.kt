package pl.codziennemotto.services.user

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import testutils.IntegrationTest
import kotlin.test.assertEquals

@SpringBootTest(properties = ["spring.profiles.active=test"])
@IntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceIntegrationTest {
    @Autowired
    lateinit var userService: UserService

    @Test
    fun `getUser where id=1 returns expected User from database`() {
        val user = userService.getUser(1)!!
        assertEquals("kacperf1234@gmail.com", user.email)
        assertEquals("kacperfaber", user.username)
        assertEquals(1, user.id)
    }
}