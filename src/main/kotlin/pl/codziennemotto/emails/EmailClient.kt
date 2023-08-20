package pl.codziennemotto.emails

import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.email.EmailBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("prod")
class EmailClient(private val mailer: Mailer) {
    @Value("\${mailer.from}")
    lateinit var from: String

    fun sendTextMail(subject: String, to: String, body: String) {
        val mail = EmailBuilder
                .startingBlank()
                .to(to)
                .withSubject(subject)
                .withPlainText(body)
                .from(from)
                .buildEmail()

        mailer.sendMail(mail)
    }
}
