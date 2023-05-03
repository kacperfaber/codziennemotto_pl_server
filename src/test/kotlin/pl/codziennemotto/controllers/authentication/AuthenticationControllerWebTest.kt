package pl.codziennemotto.controllers.authentication

import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import pl.codziennemotto.data.dto.User
import pl.codziennemotto.security.TokenAuthentication
import pl.codziennemotto.services.authentication.AuthenticationService
import pl.codziennemotto.services.token.AccessToken
import pl.codziennemotto.services.token.TokenService
import pl.codziennemotto.services.user.UserService
import testutils.WebLayerTest
import testutils.auth
import java.util.*

@WebLayerTest
@SpringBootTest(properties = ["spring.profiles.active=test"])
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension::class)
class AuthenticationControllerWebTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var authenticationService: AuthenticationService

    @MockBean
    lateinit var tokenService: TokenService

    @MockBean
    lateinit var userService: UserService

    private fun makeBody(username: String, password: String): String = "{\"login\": \"$username\", \"password\": \"$password\"}"

    @Test
    fun `authEndpoint responds ok if authService returned not null`() {
        `when`(authenticationService.tryAuthenticate(anyString(), anyString()))
            .thenReturn(User().apply {
                this.username = ""
                this.email = ""
                this.passwordHash = ""
                this.readers = mutableListOf()
                this.textSets = mutableListOf()
                this.id = 1
            })

        `when`(tokenService.generateToken(org.mockito.kotlin.any())).thenReturn("")

        mockMvc.post("/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = makeBody("kacperfaber", "Test1235")
        }.andExpect { status { isOk() } }
    }

    @Test
    fun `authEndpoint responds badRequest if authService returned null`() {
        `when`(authenticationService.tryAuthenticate(anyString(), anyString()))
            .thenReturn(null)

        mockMvc.post("/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = makeBody("kacperfaber", "Test1235")
        }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `authEndpoint returns token if authService returned not null`() {
        `when`(authenticationService.tryAuthenticate(anyString(), anyString()))
            .thenReturn(User().apply {
                this.username = ""
                this.email = ""
                this.passwordHash = ""
                this.readers = mutableListOf()
                this.textSets = mutableListOf()
                this.id = 1
            })

        val expectedToken = UUID.randomUUID().toString()

        `when`(tokenService.generateToken(org.mockito.kotlin.any())).thenReturn(expectedToken)

        mockMvc.post("/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = makeBody("kacperfaber", "Test1235")
        }.andExpect { jsonPath("$.token", `is`(expectedToken)) }
    }

    @Test
    fun `authEndpoint returns userEmail, userId and username if authService returned not null`() {
        val user = User().apply {
            this.username = ""
            this.email = ""
            this.passwordHash = ""
            this.readers = mutableListOf()
            this.textSets = mutableListOf()
            this.id = 1
        }

        `when`(authenticationService.tryAuthenticate(anyString(), anyString()))
            .thenReturn(user)

        `when`(tokenService.generateToken(org.mockito.kotlin.any())).thenReturn("")

        mockMvc.post("/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = makeBody("kacperfaber", "Test1235")
        }.andExpect {
            jsonPath("$.userEmail", `is`(user.email))
            jsonPath("$.username", `is`(user.username))
            jsonPath("$.userId", `is`(user.id))
        }
    }

    @Test
    fun `currentEndpoint returns badRequest if not authorized`() {
        mockMvc.get("/current").andExpect { status { isBadRequest() } }
    }

    @Test
    fun `currentEndpoint returns expected data from database mock if authorized`() {
        val email = "kacperf1234@gmail.com"
        val username = "kacperfaber"

        `when`(tokenService.readToken(anyString())).thenReturn(AccessToken(1, email, username))

        `when`(userService.getUser(anyInt())).thenReturn(User().apply {
            this.id = 1
            this.username = username
            this.email = email
        })

        mockMvc.get("/current") { auth(1) }.andExpect {
            jsonPath("$.email", `is`(email))
            jsonPath("$.username", `is`(username))
            jsonPath("$.id", `is`(1))
        }
    }

    @Test
    fun `currentEndpoint returns no 'passwordHash' property`() {
        val email = "kacperf1234@gmail.com"
        val username = "kacperfaber"

        `when`(tokenService.readToken(anyString())).thenReturn(AccessToken(1, email, username))

        `when`(userService.getUser(anyInt())).thenReturn(User().apply {
            this.id = 1
            this.username = username
            this.email = email
            this.passwordHash = "Secret"
        })

        mockMvc.get("/current") {auth(1)}.andExpect {
            jsonPath("$", Matchers.not(hasItem("passwordHash")))
        }
    }
}