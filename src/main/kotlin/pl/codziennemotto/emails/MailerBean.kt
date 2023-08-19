package pl.codziennemotto.emails

import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.mailer.MailerBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("prod")
class MailerBean {
    @Value("\${mailer.smtp}")
    lateinit var smtp: String

    @Value("\${mailer.port}")
    var port: Int = -1

    @Value("\${mailer.username}")
    lateinit var username: String

    @Value("\${mailer.password}")
    lateinit var password: String

    @Bean
    fun mailer(): Mailer {
        return MailerBuilder.withSMTPServer(smtp, port)
                .withSMTPServerUsername(username)
                .withSMTPServerPassword(password)
                .buildMailer()
    }

}
