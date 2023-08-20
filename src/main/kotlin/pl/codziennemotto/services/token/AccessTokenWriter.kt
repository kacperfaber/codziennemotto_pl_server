package pl.codziennemotto.services.token

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.codec.Hex
import org.springframework.stereotype.Component
import pl.codziennemotto.services.token.AesUtils.Companion.getKeyBytes
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Cipher.SECRET_KEY
import javax.crypto.spec.SecretKeySpec


interface AccessTokenWriter {
    fun writeToken(accessToken: AccessToken): String
}

@Component
@Profile("test")
class TestAccessTokenWriter(private val gson: Gson) : AccessTokenWriter {
    override fun writeToken(accessToken: AccessToken): String = gson.toJson(accessToken)
}

@Component
@Profile("prod")
class ProdAccessTokenWriter(private val gson: Gson): AccessTokenWriter {
    @Value("\${token_hasher.secret}")
    lateinit var secret: String

    private val algorithm = "AES"

    override fun writeToken(accessToken: AccessToken): String {
        val secretKey = SecretKeySpec(getKeyBytes(secret), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val json = gson.toJson(accessToken)

        val encryptedBytes = cipher.doFinal(json.toByteArray())
        return String(Hex.encode(encryptedBytes))
    }
}