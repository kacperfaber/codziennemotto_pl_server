package pl.codziennemotto.services.token

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import testutils.UnitTest
import java.util.*
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.assertEquals
import kotlin.test.assertNull

@UnitTest
class ProdAccessTokenReaderTest {

    private fun read(hash: String, secret: String): AccessToken? {
        return ProdAccessTokenReader(Gson()).apply { this.secret = secret }.readToken(hash)
    }

    private fun write(accessToken: AccessToken, secret: String): String {
        return ProdAccessTokenWriter(Gson()).apply { this.secret = secret }.writeToken(accessToken)
    }

    @Test
    fun `read - does not throw`() {
        val secret = UUID.randomUUID().toString() + "${Random.nextUInt()}"
        val hash = write(AccessToken(0, "kacperfaber", "kacperf1234@gmail.com"), secret)
        assertDoesNotThrow { read(hash, secret) }
    }

    @Test
    fun `write - does not throw`() {
        assertDoesNotThrow { write(AccessToken(0, "kacperfaber", "kacperf1234@gmail.com"), "abc") }
    }

    @Test
    fun `read&write - returns expected data`() {
        val secret = "${UUID.randomUUID().toString()}${UUID.randomUUID().toString()}"
        val hash = write(AccessToken(5, "kacperfaber", "kacperf1234@gmail.com"), secret)
        val r = read(hash, secret)

        assertEquals("kacperfaber", r?.userName)
        assertEquals("kacperf1234@gmail.com", r?.userEmail)
        assertEquals(5, r?.userId)
    }

    @Test
    fun `read&write - read returns null when is bad password`() {
        val secret = UUID.randomUUID().toString()
        val hash = write(AccessToken(5, "kacperfaber", "kacperf1234@gmail.com"), secret)
        val r = read(hash, UUID.randomUUID().toString() + "!")
        assertNull(r)
    }
}