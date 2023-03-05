package pl.codziennemotto.controllers.textset

import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import pl.codziennemotto.data.dao.TextSetDao
import testutils.IntegrationTest
import testutils.WebLayerTest
import testutils.auth
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@WebLayerTest
@SpringBootTest(properties = ["spring.profiles.active=test"])
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TextSetControllerWebTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var textSetDao: TextSetDao

    @Test
    @IntegrationTest
    fun `textSetByIdEndpoint returns FORBIDDEN if not authorized`() {
        mockMvc.get("/text-set/1").andExpect { status { isForbidden() } }
    }

    @Test
    @IntegrationTest
    fun `textSetByIdEndpoint returns OK if is token presented`() {
        mockMvc.get("/text-set/0") { auth(1) }.andExpect { status { isOk() } }
    }

    @Test
    @IntegrationTest
    fun `textSetByIdEndpoint returns expected TextSet from database`() {
        mockMvc.get("/text-set/0") { auth(1) }.andExpect {
            jsonPath("$.ownerId", `is`(1))
            jsonPath("$.id", `is`(0))
            jsonPath("$.title", `is`("HelloWorld!"))
            jsonPath("$.description", `is`("This is the greatest set in the page."))
        }
    }

    private fun createNewTextSetBody(title: String, description: String): String {
        return "{\"title\": \"$title\", \"description\": \"$description\"}"
    }

    @Test
    @IntegrationTest
    fun `createNewTextSetEndpoint returns FORBIDDEN if not authorized`() {
        mockMvc.post("/text-set/create-new") {
            contentType = MediaType.APPLICATION_JSON
            content = createNewTextSetBody("Hello World!", "Test!")
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @IntegrationTest
    fun `createNewTextSetEndpoint returns OK if authorized`() {
        mockMvc.post("/text-set/create-new") {
            auth(1)
            contentType = MediaType.APPLICATION_JSON
            content = createNewTextSetBody("Hello World!", "Test!")
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    @IntegrationTest
    fun `createNewTextSetEndpoint returns expected title and description`() {
        val title = "Hello World!"
        val description = "Test!"
        mockMvc.post("/text-set/create-new") {
            auth(1)
            contentType = MediaType.APPLICATION_JSON
            content = createNewTextSetBody(title, description)
        }.andExpect {
            jsonPath("$.title", `is`(title))
            jsonPath("$.description", `is`(description))
        }
    }

    @Test
    @IntegrationTest
    fun `createNewTextSetEndpoint makes the TextSet table greater`() {
        val before = textSetDao.findAll().count()

        mockMvc.post("/text-set/create-new") {
            auth(1)
            contentType = MediaType.APPLICATION_JSON
            content = createNewTextSetBody("Test", "Desc")
        }.andExpect {
            assertEquals(before+1, textSetDao.findAll().count())
        }
    }

    @Test
    @IntegrationTest
    fun `createNewTextSetEndpoint creates TextSet in database`() {
        val title = UUID.randomUUID().toString()

        mockMvc.post("/text-set/create-new") {
            auth(1)
            contentType = MediaType.APPLICATION_JSON
            content = createNewTextSetBody(title, "Desc")
        }.andExpect {
            assertNotNull(textSetDao.findAll().firstOrNull {it.title == title})
        }
    }
}