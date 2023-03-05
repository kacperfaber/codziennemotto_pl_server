package pl.codziennemotto.controllers.server

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import testutils.UnitTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(properties = ["spring.profiles.active=test"])
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ServerControllerTest {
    @Autowired
    lateinit var serverController: ServerController

    @UnitTest
    @Test
    fun `profileEndpoint returns ok`() {
        assertEquals(HttpStatus.OK, serverController.profileEndpoint().statusCode as HttpStatus)
    }

    @UnitTest
    @Test
    fun `profileEndpoint returns expected body`() {
        val expected = "test"
        val actual = serverController.profileEndpoint().body?.profile
        assertNotNull(actual)
        assertEquals(expected, actual)
    }
}