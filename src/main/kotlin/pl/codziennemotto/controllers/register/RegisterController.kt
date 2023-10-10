package pl.codziennemotto.controllers.register

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.codziennemotto.controllers.ControllerBase
import pl.codziennemotto.services.user.UserService
import pl.codziennemotto.services.user.registration.RegisterResult
import pl.codziennemotto.services.user.registration.UserRegistrationService

fun RegisterResult.respond(): ResponseEntity<RegisterResult> {
    if (this == RegisterResult.Ok) return ResponseEntity.noContent().build()
    return ResponseEntity.badRequest().body(this)
}

@RestController
class RegisterController(userService: UserService, private val userRegistrationService: UserRegistrationService) : ControllerBase(userService), RegisterApi {
    @PostMapping("/register")
    override fun registerEndpoint(@RequestBody p: RegisterPayload): ResponseEntity<RegisterResult> {
        return userRegistrationService.register(p.username, p.emailAddress, p.password).respond()
    }

    @PostMapping("/register/confirm")
    override fun confirmEndpoint(@RequestBody p: ConfirmPayload): ResponseEntity<Boolean> {
        return ofBoolean(userRegistrationService.confirm(p.emailAddress, p.code))
    }
}