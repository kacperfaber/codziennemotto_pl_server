package pl.codziennemotto.services.joinlink

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import testutils.UnitTest
import kotlin.test.assertTrue

@UnitTest
class ProdJoinLinkCodeGeneratorTest {
    private fun exec(): String = ProdJoinLinkCodeGenerator().generateCode()

    @Test
    fun `does not throw`() {
        assertDoesNotThrow { exec() }
    }

    @Test
    fun `returns another strings all the time`() {
        val strings = (0..100).map { exec() }
        for (s in strings) {
            assertTrue { strings.count { it == s} < 2 }
        }
    }
}