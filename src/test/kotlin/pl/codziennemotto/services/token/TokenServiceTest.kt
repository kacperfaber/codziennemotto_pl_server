package pl.codziennemotto.services.token

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.internal.verification.Times
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.DirtiesContext
import testutils.UnitTest
import kotlin.test.assertEquals

@SpringBootTest(properties = ["spring.profiles.active=test"])
class TokenServiceTest {
    @Autowired
    lateinit var tokenService: TokenService

    @MockBean
    lateinit var tokenWriter: AccessTokenWriter

    @Test
    @UnitTest
    fun `writeToken returns what 'tokenWriter' returned`() {
        val accessToken = AccessToken(0, "test", "test")
        Mockito.`when`(tokenWriter.writeToken(accessToken)).thenReturn("test")
        assertEquals("test", tokenService.writeToken(accessToken))
    }

    @Test
    @UnitTest
    fun `writeToken calls 'tokenWriter' with expected AccessToken instance`() {
        val accessToken = AccessToken(0, "test", "test")
        Mockito.`when`(tokenWriter.writeToken(any())).thenReturn("test")
        tokenService.writeToken(accessToken)
        Mockito.verify(tokenWriter, Times(1)).writeToken(eq(accessToken))
    }
}