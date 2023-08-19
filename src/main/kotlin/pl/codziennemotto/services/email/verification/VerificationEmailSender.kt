package pl.codziennemotto.services.email.verification

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

interface VerificationEmailSender {
    fun send(emailAddress: String, code: String)
}

@Component
@Profile("dev", "test")
class DevVerificationEmailSender : VerificationEmailSender {
    override fun send(emailAddress: String, code: String) {
        val x = """
            
            SENDING VERIFICATION EMAIL:
            TO $emailAddress CODE IS $code
            
        """

        println(x)
    }
}

@Component
@Profile("prod")
class ProdVerificationEmailSender : VerificationEmailSender {
    override fun send(emailAddress: String, code: String) {
        // TODO Verification Email Sender not implemented yet..
        TODO()
    }
}