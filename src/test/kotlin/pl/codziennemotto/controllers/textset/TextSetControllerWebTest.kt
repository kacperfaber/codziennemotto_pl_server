package pl.codziennemotto.controllers.textset

import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import testutils.IntegrationTest
import testutils.WebLayerTest
import testutils.auth


@WebLayerTest
@SpringBootTest(properties = ["spring.profiles.active=test"])
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
class TextSetControllerWebTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @IntegrationTest
    fun `textSetByIdEndpoint returns FORBIDDEN if not authorized`() {
        mockMvc.get("/text-set/1").andExpect { status { isForbidden() } }
    }

    @Test
    @IntegrationTest
    fun `textSetByIdEndpoint returns OK if is token presented`() {
        mockMvc.get("/text-set/1") { auth(1) }.andExpect { status { isOk() } }
    }

    @Test
    @IntegrationTest
    fun `textSetByIdEndpoint returns expected TextSet from database`() {
        mockMvc.get("/text-set/1") { auth(1) }.andExpect {
            jsonPath("$.ownerId", `is`(1))
            jsonPath("$.id", `is`(1))
            jsonPath("$.title", `is`("HelloWorld!"))
            jsonPath("$.description", `is`("This is the greatest set in the page."))
        }
    }
}