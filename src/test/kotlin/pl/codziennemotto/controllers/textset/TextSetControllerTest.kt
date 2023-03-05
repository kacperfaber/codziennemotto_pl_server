package pl.codziennemotto.controllers.textset

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.annotation.DirtiesContext
import pl.codziennemotto.data.dto.JoinLink
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User
import pl.codziennemotto.security.TokenAuthentication
import pl.codziennemotto.services.joinlink.JoinLinkService
import pl.codziennemotto.services.text.TextService
import pl.codziennemotto.services.user.UserService
import testutils.UnitTest
import kotlin.test.assertEquals

@SpringBootTest(properties = ["spring.profiles.active=test"])
@UnitTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TextSetControllerTest {
    @Autowired
    lateinit var textSetController: TextSetController

    @MockBean
    lateinit var textService: TextService

    @MockBean
    lateinit var userService: UserService
    @MockBean
    lateinit var joinLinkService: JoinLinkService

    private val user = User().apply {
        this.id = 0
        this.readers = mutableListOf()
        this.textSets = mutableListOf()
    }

    private val textSet = TextSet().apply {
        this.id = 0
        this.ownerId = 0
        this.owner = user
        this.readers = mutableListOf()
        this.texts = mutableListOf()
    }

    @Test
    fun `textSetByIdEndpoint returns ok if textService returned not null`() {
        `when`(userService.getUser(anyInt())).thenReturn(user)
        `when`(textService.getTextSet(anyInt(), org.mockito.kotlin.any())).thenReturn(textSet)
        assertEquals(HttpStatus.OK, textSetController.textSetByIdEndpoint(0).statusCode)
    }

    @Test
    fun `textSetByIdEndpoint returns badRequest if textService returned null`() {
        `when`(userService.getUser(anyInt())).thenReturn(user)
        `when`(textService.getTextSet(anyInt(), org.mockito.kotlin.any())).thenReturn(null)
        assertEquals(HttpStatus.BAD_REQUEST, textSetController.textSetByIdEndpoint(0).statusCode)
    }

    @Test
    fun `textSetByIdEndpoint returns TextSet what textSetDao returned`() {
        `when`(userService.getUser(anyInt())).thenReturn(user)
        `when`(textService.getTextSet(anyInt(), org.mockito.kotlin.any())).thenReturn(textSet)

        assertEquals(textSet, textSetController.textSetByIdEndpoint(0).body)
    }

    @BeforeEach
    fun beforeAll() {
        SecurityContextHolder.getContext().authentication = TokenAuthentication(0, "kacperf1234@gmail.com", "kacperfaber")
    }

    @Test
    fun `joinLinkEndpoint returns what joinLinkService returned`() {
        val joinLink = JoinLink()
        `when`(userService.getUser(anyInt())).thenReturn(user)
        `when`(joinLinkService.createJoinLink(anyInt(), any())).thenReturn(joinLink)
        assertEquals(joinLink, textSetController.joinLinkEndpoint(5).body)
    }

    @Test
    fun `joinLinkEndpoint calls joinLinkService with expected setId`() {
        `when`(joinLinkService.createJoinLink(anyInt(), any())).thenReturn(JoinLink())
        `when`(userService.getUser(anyInt())).thenReturn(user)
        textSetController.joinLinkEndpoint(5)
        verify(joinLinkService).createJoinLink(eq(5), eq(user))
    }
}