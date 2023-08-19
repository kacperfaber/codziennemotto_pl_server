package pl.codziennemotto.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import pl.codziennemotto.data.dao.*
import pl.codziennemotto.data.dto.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Component
@Profile("test", "dev")
class SetupTestDb(private val joinLinkDao: JoinLinkDao, private val userDao: UserDao, private val textSetDao: TextSetDao, private val readerDao: ReaderDao, private val textDao: TextDao) {
    @Value("\${testing.initdb:false}")
    var initDb: Boolean = false

    private fun setupUser(): User {
        return userDao.save(User().apply {
            id = 0
            email = "kacperf1234@gmail.com"
            passwordHash = "HelloWorld123"
            username = "kacperf1234"
        })
    }

    private fun setupRandomUsers(): MutableList<User> {
        val users = mutableListOf<User>()

        repeat(10) { i ->
            val id = i + 100

            users.add(userDao.save(User().apply {
                this.id = id
                this.username = "User $id"
                this.email = "random-user-$id@gmail.com"
                this.passwordHash = "HelloWorld123"
            }))
        }
        return users
    }

    private fun randomTexts(textSet: TextSet) {
        for (i in 0..10) {
            textDao.save(Text().apply {
                id = i
                order = i
                text = "$i: Miej wyjebane a będzie ci dane"
                this.textSet = textSet
                this.date = LocalDate.of(2023, 8, i + 1)
            })
        }
    }

    private fun randomJoinLinks(textSet: TextSet) {
        for (i in 0..5) {
            joinLinkDao.save(JoinLink().apply {
                id = i
                activeUntil = LocalDateTime.now().plusDays(14)
                code = UUID.randomUUID().toString().take(4)
                this.textSet = textSet
            })
        }
    }

    private fun performSetup() {
        val kacper = setupUser()

        val firstTextSet = textSetDao.save(TextSet().apply {
            id = 0
            title = "First TextSet"
            description = "This is my first text set"
            owner = kacper
        })

        val randomUsers = setupRandomUsers()

        randomUsers.forEach { user ->
            readerDao.save(Reader().apply {
                id = user.id
                this.user = user
                this.textSet = firstTextSet
            })
        }

        randomTexts(firstTextSet)

        randomJoinLinks(firstTextSet)
    }

    @EventListener(ApplicationReadyEvent::class)
    fun setupTestDb() {
        if (initDb) performSetup()
    }
}