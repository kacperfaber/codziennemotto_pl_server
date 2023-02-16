package pl.codziennemotto.controllers.server

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.codziennemotto.controllers.ControllerBase
import pl.codziennemotto.services.user.UserService

@RestController("server")
class ServerController(userService: UserService) : ControllerBase(userService) {
    @Value("\${spring.profiles.active}")
    lateinit var activeProfile: String

    data class ProfileResponse(val profile: String)

    @GetMapping("profile")
    fun versionEndpoint(): ResponseEntity<ProfileResponse> = ok(ProfileResponse(activeProfile))
}