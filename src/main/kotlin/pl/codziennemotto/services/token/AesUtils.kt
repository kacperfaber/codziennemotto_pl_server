package pl.codziennemotto.services.token

import java.nio.charset.Charset
import java.security.MessageDigest

class AesUtils {
    companion object {
        fun getKeyBytes(key: String): ByteArray {
            val sha = MessageDigest.getInstance("SHA-256")
            val keyBytes = sha.digest(key.toByteArray(Charset.defaultCharset()))
            return keyBytes.copyOf(16)
        }
    }
}