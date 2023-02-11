package pl.codziennemotto.controllers.authentication

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.codziennemotto.controllers.ControllerBase
import pl.codziennemotto.services.authentication.AuthenticationService
import pl.codziennemotto.services.token.TokenService
import pl.codziennemotto.services.user.UserService

@RestController
class AuthenticationController(userService: UserService, private val tokenService: TokenService, private val authenticationService: AuthenticationService) :
    ControllerBase(userService) {

    class AuthPayload {
        lateinit var login: String
        lateinit var password: String
    }

    data class AuthResponse(val userId: Int, val userEmail: String, val username: String, val token: String)

    @PostMapping("/auth")
    fun authEndpoint(@RequestBody authPayload: AuthPayload): ResponseEntity<AuthResponse> {
        val authenticatedUser = authenticationService.tryAuthenticate(authPayload.login, authPayload.password)
            ?: return badRequest()

        val token = tokenService.generateToken(authenticatedUser)

        return ok(AuthResponse(authenticatedUser.id!!, authenticatedUser.email, authenticatedUser.username, token))
    }
}