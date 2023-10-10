package pl.codziennemotto.controllers.server

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Server", description = "Basic server informations")
interface ServerApi {
    @Operation(
            operationId = "ServerController#profileEndpoint",
            description = "Get server profile",
            responses = [
                ApiResponse(responseCode = "200", description = "Success")
            ]
    )
    fun profileEndpoint(): ResponseEntity<ServerController.ProfileResponse>
}