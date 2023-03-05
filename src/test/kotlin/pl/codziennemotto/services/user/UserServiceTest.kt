package pl.codziennemotto.services.user

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.`when`
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import pl.codziennemotto.data.dao.UserDao
import pl.codziennemotto.data.dto.User
import testutils.UnitTest
import kotlin.test.assertEquals

@SpringBootTest(properties = ["spring.profiles.active=test"])
@UnitTest
class UserServiceTest {
    @Autowired
    lateinit var userService: UserService

    @MockBean
    lateinit var userDao: UserDao

    @Test
    fun `getUser returns what userDao returned`() {
        val user = User()
        `when`(userDao.findByIdOrNull(anyInt())).thenReturn(user)
        assertEquals(user, userService.getUser(1))
    }

    @ParameterizedTest
    @ValueSource(ints = [5, 11, 10, 15, 24, 25, 69, 4, 0])

    fun `getUser calls userDao with expected id`(id: Int) {
        val user = User()
        `when`(userDao.findByIdOrNull(anyInt())).thenReturn(user)
        userService.getUser(id)
        verify(userDao).findByIdOrNull(eq(id))
    }
}