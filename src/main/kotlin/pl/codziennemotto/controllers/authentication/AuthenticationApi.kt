package pl.codziennemotto.controllers.authentication

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import pl.codziennemotto.data.dto.User

@Tag(name = "Authentication", description = "Authenticate yourself")
interface AuthenticationApi {
    @Operation(
            operationId = "AuthenticationController#authEndpoint",
            method = "POST",
            description = "Authenticates user by 'login' and 'password' and produces access token",
            security = [SecurityRequirement(name = "user")],
            responses = [
                ApiResponse(responseCode = "200", description = "Successful authentication"),
                ApiResponse(responseCode = "400", description = "Bad request - invalid login credentials"),
            ]
    )
    fun authEndpoint(@RequestBody authPayload: AuthenticationController.AuthPayload): ResponseEntity<AuthenticationController.AuthResponse>

    @Operation(
            operationId = "AuthenticationController#currentEndpoint",
            method = "GET",
            description = "Get current User",
            security = [SecurityRequirement(name = "user")],
            responses = [
                ApiResponse(responseCode = "200", description = "User's authenticated"),
                ApiResponse(responseCode = "400", description = "bad request - No User's authenticated"),
            ]
    )
    fun currentEndpoint(): ResponseEntity<User>
}