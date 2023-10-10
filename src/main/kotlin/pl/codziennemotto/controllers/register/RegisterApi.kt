package pl.codziennemotto.controllers.register

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import pl.codziennemotto.services.user.registration.RegisterResult

@Tag(name = "Registration", description = "Register or confirm new user.")
interface RegisterApi {
    @Operation(
            operationId = "RegisterController#registerEndpoint",
            method = "POST",
            description = "Register new user",
            security = [SecurityRequirement(name = "accessToken")],
            responses = [
                ApiResponse(responseCode = "204", description = "OK - Verification email is sent"),
                ApiResponse(responseCode = "400", description = "Bad Request - See body to understand the error, potentially returned the \"EmailOrUsernameIsTaken\".")
            ]
    )
    fun registerEndpoint(@RequestBody p: RegisterPayload): ResponseEntity<RegisterResult>

    @Operation(
            operationId = "RegisterController#confirmEndpoint",
            method = "POST",
            description = "Confirm new user",
            security = [SecurityRequirement(name = "accessToken")],
            responses = [
                ApiResponse(responseCode = "204", description = "OK - User created"),
                ApiResponse(responseCode = "400", description = "Bad Request - Something wrong, probably there's bad email or verification code.")
            ]
    )
    fun confirmEndpoint(@RequestBody p: ConfirmPayload): ResponseEntity<Boolean>
}