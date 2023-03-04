package pl.codziennemotto.services.authentication

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import pl.codziennemotto.data.dao.UserDao
import pl.codziennemotto.data.dto.User
import pl.codziennemotto.security.PasswordTool
import testutils.UnitTest
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(properties = ["spring.profiles.active=test"])
class AuthenticationServiceTest {
    @Autowired
    lateinit var authenticationService: AuthenticationService

    @MockBean
    lateinit var userDao: UserDao

    @MockBean
    lateinit var passwordTool: PasswordTool

    companion object {
        @JvmStatic
        fun valueSource(): Stream<Arguments> = Stream.of(
            Arguments.of("", ""),
            Arguments.of("kacperf1234@gmail.com", "test12345"),
            Arguments.of("kacperf1234@gmail.com", "TEST12345"),
            Arguments.of("tata_com@gmail.com", "TEST_-123$%^"),
            Arguments.of("tata_com@gmail.com", ""),
            Arguments.of("", "HelloWorld123!")
        )
    }

    @Test
    @UnitTest
    fun `tryAuthenticate does not throw`() {
        assertDoesNotThrow { authenticationService.tryAuthenticate("", "") }
        assertDoesNotThrow { authenticationService.tryAuthenticate("kacperfaber@gmail.com", "Test12345%%!@#") }
        assertDoesNotThrow { authenticationService.tryAuthenticate("kacperf1234@gmail.com", "") }
        assertDoesNotThrow { authenticationService.tryAuthenticate("test.@gmail.com", "test") }
        assertDoesNotThrow { authenticationService.tryAuthenticate("jebacDisa", "test") }
    }

    @ParameterizedTest
    @MethodSource("valueSource")
    @UnitTest
    fun `tryAuthenticate returns null if userDao returns null`(username: String, password: String) {
        `when`(userDao.getByUsernameOrEmail(anyString())).thenReturn(null)
        assertNull(authenticationService.tryAuthenticate(username, password))
    }

    @ParameterizedTest
    @MethodSource("valueSource")
    @UnitTest
    fun `tryAuthenticate returns null if passwordTool returns false`(username: String, password: String) {
        `when`(passwordTool.matches(anyString(), anyString())).thenReturn(false)
        assertNull(authenticationService.tryAuthenticate(username, password))
    }

    @ParameterizedTest
    @MethodSource("valueSource")
    @UnitTest
    fun `tryAuthenticate returns object returned by userDao if passwordTool returned true`(username: String, password: String) {
        val user = User().apply {
            this.passwordHash = "test"
            this.email = "kacper"
        }
        `when`(userDao.getByUsernameOrEmail(anyString())).thenReturn(user)
        `when`(passwordTool.matches(anyString(), anyString())).thenReturn(true)
        val result = authenticationService.tryAuthenticate(username, password)
        assertNotNull(result)
        assertEquals(user, result)
    }
}