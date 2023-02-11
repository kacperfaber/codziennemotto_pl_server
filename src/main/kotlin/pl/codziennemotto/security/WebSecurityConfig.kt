package pl.codziennemotto.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Configuration
class WebSecurityConfig : GlobalMethodSecurityConfiguration() {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http.run {
        csrf().disable()
        anonymous().disable()
        return build()
    }
}