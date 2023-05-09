package pl.codziennemotto.security

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletResponse

@WebFilter("*")
class CorsFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        (response as? HttpServletResponse)?.addHeader("Access-Control-Allow-Origin", "*")
        (response as? HttpServletResponse)?.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
        (response as? HttpServletResponse)?.addHeader("Access-Control-Allow-Headers", "*");
        chain?.doFilter(request, response)
    }
}
