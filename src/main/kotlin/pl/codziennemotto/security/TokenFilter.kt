package pl.codziennemotto.security

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import pl.codziennemotto.services.token.TokenService

@Component
class TokenFilter(private val tokenService: TokenService) : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val authorizationHeader = (request as? HttpServletRequest)?.getHeader("Authorization")
        if (authorizationHeader != null) {
            val accessToken = tokenService.readToken(authorizationHeader)
            if (accessToken != null) {
                SecurityContextHolder.getContext().authentication =
                    TokenAuthentication(accessToken.userId, accessToken.userEmail, accessToken.userName)
            }
        }

        chain?.doFilter(request, response)
    }
}