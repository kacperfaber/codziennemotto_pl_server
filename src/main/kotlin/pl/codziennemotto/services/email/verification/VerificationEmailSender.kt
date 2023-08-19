package pl.codziennemotto.services.email.verification

import org.simplejavamail.api.mailer.Mailer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import pl.codziennemotto.emails.EmailClient

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
class ProdVerificationEmailSender(private val emailClient: EmailClient) : VerificationEmailSender {
    @Value("\${verification_email.subject}")
    lateinit var subject: String

    private fun createBody(code: String): String = """
        Hello,
        
        if you still want to register in codziennemotto.pl, 
        please use this code: $code
        
        If it's not you, please ignore this message.
        
    """.trimIndent()

    override fun send(emailAddress: String, code: String) {
        emailClient.sendTextMail(subject = subject, to = emailAddress, body = createBody(code))
    }
}