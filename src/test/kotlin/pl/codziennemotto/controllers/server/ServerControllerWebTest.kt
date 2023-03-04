package pl.codziennemotto.controllers.server

import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import testutils.WebLayerTest

@SpringBootTest(properties = ["spring.profiles.active=test"])
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@WebLayerTest
class ServerControllerWebTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `profileEndpoint responds ok`() {
        mockMvc.get("/server/profile").andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `profileEndpoint responds json path 'profile' equals 'test'`() {
        mockMvc.get("/server/profile").andExpect {
            jsonPath("$.profile", `is`("test"))
        }
    }
}