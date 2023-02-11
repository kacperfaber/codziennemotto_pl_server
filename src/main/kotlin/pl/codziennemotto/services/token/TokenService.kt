package pl.codziennemotto.services.token

import org.springframework.stereotype.Component
import pl.codziennemotto.data.dto.User

@Component
class TokenService(private val accessTokenWriter: AccessTokenWriter, private val accessTokenReader: AccessTokenReader) {
    fun writeToken(accessToken: AccessToken) = accessTokenWriter.writeToken(accessToken)

    fun readToken(tokenHash: String): AccessToken? = accessTokenReader.readToken(tokenHash)

    private fun generateAccessToken(user: User): AccessToken = AccessToken(user.id!!, user.username, user.email)

    fun generateToken(user: User): String = writeToken(generateAccessToken(user))
}