package pl.codziennemotto.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class TokenAuthentication(val userId: Int, val userEmail: String, val username: String) : Authentication {
    override fun getName(): String {
        return username
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getCredentials(): Any {
        return userEmail
    }

    override fun getDetails(): Any {
        return userId
    }

    override fun getPrincipal(): Any {
        return userId
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {}
}