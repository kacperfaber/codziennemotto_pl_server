package pl.codziennemotto.services.joinlink

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

interface JoinLinkCodeGenerator {
    fun generateCode(): String
}

@Component
@Profile("test")
class TestProdLinkCodeGenerator : JoinLinkCodeGenerator {
    override fun generateCode(): String = "ABC123"
}

@Profile("prod")
@Component
class ProdJoinLinkCodeGenerator : JoinLinkCodeGenerator {
    val chars = listOf(
        'a',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'r',
        'o',
        'p',
        's',
        'u',
        't',
        'w',
        'z',
        'x',
        'y',
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9'
    )

    val base = "xxxxxxxx"

    override fun generateCode(): String = base.replace(Regex.fromLiteral("x")) { chars.random().toString() }
}