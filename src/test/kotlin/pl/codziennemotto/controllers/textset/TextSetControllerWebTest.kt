package pl.codziennemotto.controllers.textset

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.assertj.core.util.Lists
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.isNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.rest.webmvc.json.JsonSchema
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.*
import pl.codziennemotto.data.dao.JoinLinkDao
import pl.codziennemotto.data.dao.ReaderDao
import pl.codziennemotto.data.dao.TextDao
import pl.codziennemotto.data.dao.TextSetDao
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.services.text.TextService
import testutils.IntegrationTest
import testutils.WebLayerTest
import testutils.auth
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


@WebLayerTest
@SpringBootTest(properties = ["spring.profiles.active=test"])
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TextSetControllerWebTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var textSetDao: TextSetDao

    @Autowired
    lateinit var joinLinkDao: JoinLinkDao

    @Autowired
    lateinit var textDao: TextDao

    @Autowired
    lateinit var readerDao: ReaderDao

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
            assertEquals(before + 1, textSetDao.findAll().count())
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
            assertNotNull(textSetDao.findAll().firstOrNull { it.title == title })
        }
    }

    @Test
    @IntegrationTest
    fun `joinLinkEndpoint returns FORBIDDEN if not authorized`() {
        mockMvc.post("/text-set/0/create-join-link") { }
                .andExpect {
                    status {
                        isForbidden()
                    }
                }
    }

    @Test
    @IntegrationTest
    fun `joinLinkEndpoint returns OK if authorized`() {
        mockMvc.post("/text-set/0/create-join-link") { auth(1) }
                .andExpect {
                    status {
                        isOk()
                    }
                }
    }

    @Test
    @IntegrationTest
    fun `joinLinkEndpoint returns expected data`() {
        mockMvc.post("/text-set/0/create-join-link") { auth(1) }
                .andExpect {
                    status {
                        jsonPath("$.code", notNullValue())
                        jsonPath("$.id", notNullValue())
                    }
                }
    }

    @Test
    @IntegrationTest
    fun `joinLinkEndpoint creates object in database and returns OK`() {
        val before = joinLinkDao.findAll().filter { it.textSetId == 0 }.count()

        mockMvc.post("/text-set/0/create-join-link") { auth(1) }
                .andExpect {
                    val after = joinLinkDao.findAll().filter { it.textSetId == 0 }.count()
                    assertTrue { after == before + 1 }
                }
    }

    @Test
    @IntegrationTest
    fun `joinLinkEndpoint returns badRequest if authorized user is not set owner`() {
        mockMvc.post("/text-set/0/create-join-link") { auth(2) }
                .andExpect {
                    status {
                        isBadRequest()
                    }
                }
    }

    @Test
    @IntegrationTest
    fun `setsIAmOwnerEndpoint returns FORBIDDEN if unauthorized`() {
        mockMvc.get("/text-set/where-i-am-owner") { }
                .andExpect {
                    status {
                        isForbidden()
                    }
                }
    }

    @Test
    @IntegrationTest
    fun `setsIAmOwnerEndpoint returns OK if authorized`() {
        mockMvc.get("/text-set/where-i-am-owner") { auth(1) }
                .andExpect {
                    status {
                        isOk()
                    }
                }
    }

    @Test
    @IntegrationTest
    fun `setsIAmOwnerEndpoint returns expected data from database`() {
        mockMvc.get("/text-set/where-i-am-owner") { auth(1) }
                .andExpect {
                    jsonPath("$[0].id", `is`(0))
                    jsonPath("$[0].title", `is`("HelloWorld!"))
                    jsonPath("$[0].description", `is`("This is the greatest set in the page."))
                }
    }

    @Test
    @IntegrationTest
    fun `setsIAmOwnerEndpoint returns empty if user is not owner of any TextSet`() {
        mockMvc.get("/text-set/where-i-am-owner") { auth(2) }
                .andExpect {
                    jsonPath("$.length()", `is`(0))
                }
    }

    @Test
    @IntegrationTest
    fun `setsIAmOwnerEndpoint returns OK if user is not owner of any TextSet`() {
        mockMvc.get("/text-set/where-i-am-owner") { auth(2) }
                .andExpect {
                    status {
                        isOk()
                    }
                }
    }

    @Test
    @IntegrationTest
    fun `setsIAmReaderEndpoint returns FORBIDDEN if unauthorized`() {
        mockMvc.get("/text-set/where-i-am-reader") { }
                .andExpect {
                    status {
                        isForbidden()
                    }
                }
    }

    @Test
    @IntegrationTest
    fun `setsIAmReaderEndpoint returns OK if authorized`() {
        mockMvc.get("/text-set/where-i-am-reader") { auth(1) }
                .andExpect {
                    status {
                        isOk()
                    }
                }
    }

    @Test
    @IntegrationTest
    fun `setsIAmReaderEndpoint returns OK if authorized user is not reading TextSet`() {
        mockMvc.get("/text-set/where-i-am-reader") { auth(1) }
                .andExpect {
                    status {
                        isOk()
                    }
                }
    }

    @Test
    @IntegrationTest
    fun `setsIAmReaderEndpoint returns empty list if authorized user is not reading TextSet`() {
        mockMvc.get("/text-set/where-i-am-reader") { auth(1) }
                .andExpect {
                    jsonPath("$.length()", `is`(0))
                }
    }

    @Test
    @IntegrationTest
    fun `setsIAmReaderEndpoint returns expected data from database`() {
        mockMvc.get("/text-set/where-i-am-reader") { auth(2) }
                .andExpect {
                    jsonPath("$[0].id", `is`(0))
                    jsonPath("$[0].title", `is`("HelloWorld!"))
                    jsonPath("$[0].description", `is`("This is the greatest set in the page."))
                }
    }

    @Test
    @IntegrationTest
    fun `deleteTextByIdEndpoint returns FORBIDDEN if unauthorized`() {
        mockMvc.delete("/text-set/0/0").andExpect { status { isForbidden() } }
    }

    @Test
    @IntegrationTest
    fun `deleteTextByIdEndpoint returns NoContent if authorized as TextSet owner`() {
        mockMvc.delete("/text-set/0/0") { auth(1) }.andExpect { status { isNoContent() } }
    }

    @Test
    @IntegrationTest
    fun `deleteTextByIdEndpoint returns badRequest if authorized is as not TextSet owner`() {
        mockMvc.delete("/text-set/0/0") { auth(2) }.andExpect { status { isBadRequest() } }
    }

    @Test
    @IntegrationTest
    fun `deleteTextByIdEndpoint returns badRequest if setId and textId not exists`() {
        mockMvc.delete("/text-set/1/0") { auth(1) }.andExpect { status { isBadRequest() } }
        mockMvc.delete("/text-set/0/110") { auth(1) }.andExpect { status { isBadRequest() } }
        mockMvc.delete("/text-set/1120/110") { auth(1) }.andExpect { status { isBadRequest() } }
    }

    @Test
    @IntegrationTest
    fun `deleteTextByIdEndpoint affects database`() {
        val before = textDao.findAll().count()

        mockMvc.delete("/text-set/0/0") { auth(1) }.andExpect {
            status { isNoContent() }
        }

        val after = textDao.findAll().count()
        assertEquals(before - 1, after)
    }

    private fun writeLocalDateJSON(date: LocalDate?) = if (date != null) "\"${date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}\"" else "null"

    private fun addTextContent(text: String, date: LocalDate?, order: Int) =
            "{\"text\": \"$text\", \"date\":${writeLocalDateJSON(date)}, \"order\": $order}"

    @Test
    @IntegrationTest
    fun `addTextByIdEndpoint returns FORBIDDEN if unauthorized`() {
        mockMvc.put("/text-set/0/add") {
            contentType = MediaType.APPLICATION_JSON
            content = addTextContent("HelloWorld", null, 0)
        }.andExpect { status { isForbidden() } }
    }

    @Test
    @IntegrationTest
    fun `addTextByIdEndpoint returns OK if authorized`() {
        mockMvc.put("/text-set/0/add") {
            auth(1)
            contentType = MediaType.APPLICATION_JSON
            content = addTextContent("HelloWorld", null, 0)
        }.andExpect { status { isOk() } }
    }

    @Test
    @IntegrationTest
    fun `addTextByIdEndpoint returns badRequest if authorized user is not TextSet owner`() {
        mockMvc.put("/text-set/0/add") {
            auth(2)
            contentType = MediaType.APPLICATION_JSON
            content = addTextContent("HelloWorld", null, 0)
        }.andExpect { status { isBadRequest() } }
    }

    @Test
    @IntegrationTest
    fun `addTextByIdEndpoint affects database`() {
        val before = textDao.findAll().count()
        mockMvc.put("/text-set/0/add") {
            auth(1)
            contentType = MediaType.APPLICATION_JSON
            content = addTextContent("HelloWorld", null, 0)
        }.andExpect {
            val after = textDao.findAll().count()
            assertEquals(before + 1, after)
        }
    }

    @Test
    @IntegrationTest
    fun `addTextByIdEndpoint creates Text with given 'text'`() {
        val text = UUID.randomUUID().toString()
        mockMvc.put("/text-set/0/add") {
            auth(1)
            contentType = MediaType.APPLICATION_JSON
            content = addTextContent(text, null, 0)
        }.andExpect {
            assertNotNull(textDao.findAll().filter { it.text == text })
        }
    }

    @Test
    @IntegrationTest
    fun `addTextByIdEndpoint creates Text with expected date`() {
        val text = UUID.randomUUID().toString()
        val date = LocalDate.of(2005, 5, 5)
        mockMvc.put("/text-set/0/add") {
            auth(1)
            contentType = MediaType.APPLICATION_JSON
            content = addTextContent(text, date, 0)
        }.andExpect {
            val t1 = textDao.findAll().firstOrNull { it.text == text }
            assertEquals(date, t1?.date)
        }
    }

    @Test
    @IntegrationTest
    fun `readersIncludeUserEndpoint returns FORBIDDEN if unauthorized`() {
        mockMvc.get("/text-set/0/readers/include-users") {}.andExpect { status { isForbidden() } }
    }

    @Test
    @IntegrationTest
    fun `readersIncludeUserEndpoint returns BAD REQUEST if authenticated is another user`() {
        mockMvc.get("/text-set/0/readers/include-users") { auth(3) }.andExpect { status { isBadRequest() } }
    }

    @Test
    @IntegrationTest
    fun `readersIncludeUserEndpoint returns OK if valid data`() {
        mockMvc.get("/text-set/0/readers/include-users") { auth(1) }.andExpect { status { isOk() } }
    }

    @IntegrationTest
    @Test
    fun `allVisibleTextsEndpoint returns FORBIDDEN if unauthorized`() {
        mockMvc.get("/text-set/10/texts/all/visible").andExpect { status { isForbidden() } }
    }

    @IntegrationTest
    @Test
    fun `allVisibleTextsEndpoint returns OK if authorized with access to TextSet`() {
        mockMvc.get("/text-set/10/texts/all/visible") {auth(10)}.andExpect { status { isOk() } }
    }

    @IntegrationTest
    @Test
    fun `allVisibleTextsEndpoint returns BAD REQUEST if authorized without access to TextSet`() {
        mockMvc.get("/text-set/10/texts/all/visible"){auth(1)}.andExpect { status { isBadRequest() } }
    }

    @IntegrationTest
    @Test
    fun `allVisibleTextsEndpoint returns two records if authorized as owner`() {
        mockMvc.get("/text-set/10/texts/all/visible"){auth(10)}.andExpect {
            jsonPath("$.length()", `is`(2))
        }
    }

    @IntegrationTest
    @Test
    fun `allVisibleTextsEndpoint returns single record if authorized as reader`() {
        mockMvc.get("/text-set/10/texts/all/visible"){auth(11)}.andExpect {
            jsonPath("$.length()", `is`(1))
        }
    }

    @IntegrationTest
    @Test
    fun `allVisibleTextsEndpoint returns only records with shown not null if authorized as reader`() {
        mockMvc.get("/text-set/10/texts/all/visible"){auth(11)}.andExpect {
            jsonPath("$.[*].shown", notNullValue())
        }
    }

    @IntegrationTest
    @Test
    fun `deleteTextSetByIdEndpoint returns FORBIDDEN if unauthorized`() {
        mockMvc.delete("/text-set/10").andExpect { status { isForbidden() } }
    }

    @Test
    @IntegrationTest
    fun `deleteTextSetByIdEndpoint returns BAD REQUEST if authorized is not set owner`() {
        mockMvc.delete("/text-set/10") { auth(11) }.andExpect { status { isBadRequest() } }
    }

    @Test
    @IntegrationTest
    fun `deleteTextSetByIdEndpoint returns NO CONTENT if user authorized as a TextSet owner`() {
        mockMvc.delete("/text-set/10") { auth(10) }.andExpect { status { isNoContent() } }
    }

    @Test
    @IntegrationTest
    fun `deleteTextSetByIdEndpoint makes TextSet table shorter if returned NO_CONTENT`() {
        val was = textSetDao.findAll().count()

        mockMvc.delete("/text-set/10") { auth(10) }.andExpect {
            status { isNoContent() }
            assertEquals(was - 1, textSetDao.findAll().count())
        }
    }

    @Test
    @IntegrationTest
    fun `deleteTextSetByIdEndpoint dont affect TextSet the same if returned NO_CONTENT`() {
        val was = textSetDao.findAll().count()

        mockMvc.delete("/text-set/10") { auth(11) }.andExpect {
            status { isBadRequest() }
            assertEquals(was, textSetDao.findAll().count())
        }
    }

    @Test
    fun `deleteReaderByIdEndpoint returns FORBIDDEN if unauthorized`() {
        mockMvc.delete("/text-set/30/readers/31").andExpect { status { isForbidden() } }
    }

    @Test
    fun `deleteReaderByIdEndpoint returns BAD REQUEST if user is not owner of TextSet`() {
        mockMvc.delete("/text-set/30/readers/31") { auth(31) }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `deleteReaderByIdEndpoint returns BAD REQUEST if TextSet doesnt exist`() {
        mockMvc.delete("/text-set/30009/readers/31") { auth(30) }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `deleteReaderByIdEndpoint returns NO CONTENT if user is owner of TextSet and Reader exists`() {
        mockMvc.delete("/text-set/30/readers/31") { auth(30) }.andExpect { status { isNoContent() } }
    }

    @Test
    fun `deleteReaderByIdEndpoint returns BAD REQUEST if Reader doesnt exist`() {
        mockMvc.delete("/text-set/30/readers/30005") { auth(30) }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `deleteReaderByIdEndpoint returns BAD REQUEST if Reader exist in another TextSet`() {
        mockMvc.delete("/text-set/30/readers/1") { auth(30) }.andExpect { status { isBadRequest() } }
    }

    private fun hasReader(readerId: Int, textSetId: Int): Boolean {
        return readerDao.getByIdAndTextSet(readerId, TextSet().apply { id = textSetId }) != null
    }

    @Test
    fun `deleteReaderByIdEndpoint deletes reader from db and returns NO CONTENT`() {
        mockMvc.delete("/text-set/30/readers/31") { auth(30) }.andExpect {
            status { isNoContent() }
            assertFalse {  hasReader(31, 30) }
        }
    }

    @Test
    fun `deleteReaderByIdEndpoint makes Reader table shorter and returns NO CONTENT`() {
        val i0 = readerDao.findAll().count()
        mockMvc.delete("/text-set/30/readers/31") { auth(30) }.andExpect {
            status { isNoContent() }
            val i1 = readerDao.findAll().count()
            assertEquals(i0 - 1, i1)
        }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns FOBIDDEN if unauthorized`() {
        mockMvc.delete("/text-set/30/quit").andExpect { status { isForbidden() } }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns BAD REQUEST if authorized as a TextSet owner`() {
        mockMvc.delete("/text-set/30/quit") {auth(30)}.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns BAD REQUEST if TextSet doesn't exist`(){
        mockMvc.delete("/text-set/300003/quit") {auth(30)}.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns BAD REQUEST if user is not a reader`() {
        mockMvc.delete("/text-set/1/quit") {auth(30)}.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns NO CONTENT if TextSet exist and user is a Reader`() {
        mockMvc.delete("/text-set/30/quit"){auth(31)}
    }

    @Test
    fun `quitTextSetByIdEndpoint returns NO CONTENT and deletes Reader from TextSet`() {
        mockMvc.delete("/text-set/30/quit") {auth(31)}.andExpect {
            status { isNoContent() }
            assertFalse {hasReader(31, 30)}
        }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns NO CONTENT and makes Reader table shorter`() {
        val i0 = readerDao.findAll().count()
        mockMvc.delete("/text-set/30/quit") {auth(31)}.andExpect {
            status { isNoContent() }
            val i1 = readerDao.findAll().count()
            assertEquals(i0 - 1, i1)
        }
    }

    @Test
    @IntegrationTest
    fun `joinLinksEndpoint returns FORBIDDEN if unauthorized`() {
        mockMvc.get("/text-set/-50/join-links").andExpect { status { isForbidden() } }
    }

    @Test
    fun `joinLinksEndpoint returns BAD REQUEST if authorized user is not TextSet owner`() {
        mockMvc.get("/text-set/-50/join-links") { auth(-51) }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `joinLinksEndpoint returns OK if authorized as TextSet owner`() {
        mockMvc.get("/text-set/-50/join-links") { auth(-50) }.andExpect { status { isOk() } }
    }

    @Test
    fun `joinLinksEndpoint returns expected items count when OK`() {
        mockMvc.get("/text-set/-50/join-links") { auth(-50) }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.length()", equalTo(3))
            }
        }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns FOBIDDEN if unauthorized`() {
        mockMvc.delete("/text-set/30/quit").andExpect { status { isForbidden() } }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns BAD REQUEST if authorized as a TextSet owner`() {
        mockMvc.delete("/text-set/30/quit") {auth(30)}.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns BAD REQUEST if TextSet doesn't exist`(){
        mockMvc.delete("/text-set/300003/quit") {auth(30)}.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns BAD REQUEST if user is not a reader`() {
        mockMvc.delete("/text-set/1/quit") {auth(30)}.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns NO CONTENT if TextSet exist and user is a Reader`() {
        mockMvc.delete("/text-set/30/quit"){auth(31)}
    }

    private fun hasReader(readerId: Int, textSetId: Int): Boolean {
        return readerDao.getByIdAndTextSet(readerId, TextSet().apply { id = textSetId }) != null
    }

    @Test
    fun `quitTextSetByIdEndpoint returns NO CONTENT and deletes Reader from TextSet`() {
        mockMvc.delete("/text-set/30/quit") {auth(31)}.andExpect {
            status { isNoContent() }
            assertFalse {hasReader(31, 30)}
        }
    }

    @Test
    fun `quitTextSetByIdEndpoint returns NO CONTENT and makes Reader table shorter`() {
        val i0 = readerDao.findAll().count()
        mockMvc.delete("/text-set/30/quit") {auth(31)}.andExpect {
            status { isNoContent() }
            val i1 = readerDao.findAll().count()
            assertEquals(i0 - 1, i1)
        }
    }
}