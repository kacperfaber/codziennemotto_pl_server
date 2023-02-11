package pl.codziennemotto.services.token

import com.google.gson.Gson
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

interface AccessTokenWriter {
    fun writeToken(accessToken: AccessToken): String
}

@Component
@Profile("test")
class TestAccessTokenWriter(private val gson: Gson) : AccessTokenWriter {
    override fun writeToken(accessToken: AccessToken): String = gson.toJson(accessToken)
}