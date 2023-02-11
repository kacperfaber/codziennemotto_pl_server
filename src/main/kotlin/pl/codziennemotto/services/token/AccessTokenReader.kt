package pl.codziennemotto.services.token

import com.google.gson.Gson
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

interface AccessTokenReader {
    fun readToken(tokenHash: String): AccessToken?
}

@Component
@Profile("test")
class TestAccessTokenReader(private val gson: Gson) : AccessTokenReader {
    override fun readToken(tokenHash: String): AccessToken? = try {
        gson.fromJson(tokenHash, AccessToken::class.java)
    } catch (e: Exception) {
        null
    }
}