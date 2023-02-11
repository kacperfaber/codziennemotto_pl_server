package pl.codziennemotto.services.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pl.codziennemotto.data.dao.UserDao
import pl.codziennemotto.data.dto.User

@Component
class UserService(private val userDao: UserDao) {
    fun getUser(id: Int): User? {
        return userDao.findByIdOrNull(id)
    }
}