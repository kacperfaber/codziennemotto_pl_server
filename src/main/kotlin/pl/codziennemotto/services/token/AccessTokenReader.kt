package pl.codziennemotto.services.token

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.codec.Hex
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


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

@Component
@Profile("prod")
class ProdAccessTokenReader(private val gson: Gson) : AccessTokenReader {
    @Value("\${token_hasher.secret}")
    lateinit var secret: String

    private val algorithm = "AES"

    override fun readToken(tokenHash: String): AccessToken? = try {
        val secretKey = SecretKeySpec(AesUtils.getKeyBytes(secret), algorithm)
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)

        val encrypted = Hex.decode(tokenHash)
        val decryptedBytes = cipher.doFinal(encrypted)

        gson.fromJson(String(decryptedBytes), AccessToken::class.java)
    }
    catch (e: Exception) { null }
}