package pl.codziennemotto.services.user.registration

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import kotlin.random.Random
import kotlin.random.nextUInt

interface VerificationCodeGenerator {
    fun generate(): String
}

@Profile("dev", "test")
@Component
class DevVerificationCodeGenerator : VerificationCodeGenerator {
    override fun generate(): String {
        return "ABCDE"
    }
}

@Profile("prod")
@Component
class ProdVerificationCodeGenerator : VerificationCodeGenerator {
    override fun generate(): String {
        return Random.nextUInt().toString().take(5)
    }
}