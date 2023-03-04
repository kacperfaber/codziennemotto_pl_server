package pl.codziennemotto.controllers.authentication

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import testutils.IntegrationTest
import kotlin.test.assertEquals

@SpringBootTest(properties = ["spring.profiles.active=test"])
@IntegrationTest
class AuthenticationControllerIntegrationTest {
    @Autowired
    lateinit var authenticationController: AuthenticationController

    private fun makePayload(username: String, password: String): AuthenticationController.AuthPayload {
        return AuthenticationController.AuthPayload().apply {
            this.login = username
            this.password = password
        }
    }

    @Test
    fun `authEndpoint returns ok if login and password is in database`() {
        val r = authenticationController.authEndpoint(makePayload("kacperf1234@gmail.com", "HelloWorld123"))
        assertEquals(HttpStatus.OK, r.statusCode as HttpStatus)
    }

    @Test
    fun `authEndpoint returns badRequest if login is not in database`() {
        val r = authenticationController.authEndpoint(makePayload("test@gmail.com", "HelloWorld123"))
        assertEquals(HttpStatus.BAD_REQUEST, r.statusCode as HttpStatus)
    }

    @Test
    fun `authEndpoint returns badRequest if login is in database but password is incorrect`() {
        val r = authenticationController.authEndpoint(makePayload("kacperf1234@gmail.com", "WrongPassword"))
        assertEquals(HttpStatus.BAD_REQUEST, r.statusCode as HttpStatus)
    }

    @Test
    fun `authEndpoint returns expected user data like userEmail, userId and username`() {
        val userEmail = "kacperf1234@gmail.com"
        val userId = 1
        val username = "kacperfaber"
        val r = authenticationController.authEndpoint(makePayload(userEmail, "HelloWorld123"))

        assertEquals(userEmail, r.body?.userEmail)
        assertEquals(username, r.body?.username)
        assertEquals(userId, r.body?.userId)
    }

    @Test
    fun `authEndpoint returns ok by username instead email`() {
        val r = authenticationController.authEndpoint(makePayload("kacperfaber", "HelloWorld123"))
        assertEquals(HttpStatus.OK, r.statusCode as HttpStatus)
    }
}