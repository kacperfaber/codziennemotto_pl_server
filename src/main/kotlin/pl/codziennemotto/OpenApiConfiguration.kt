package pl.codziennemotto

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
        servers = [Server( url = "/api/")],
        info = Info(
                title = "codziennemotto.pl",
                description = "REST API",
                version = "1.0.0-beta.1"
        )
)
class OpenApiConfiguration