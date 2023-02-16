package pl.codziennemotto.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import pl.codziennemotto.data.dto.User
import pl.codziennemotto.security.TokenAuthentication
import pl.codziennemotto.services.user.UserService

open class ControllerBase(protected val userService: UserService) {
    protected val userId: Int?
        get() = (SecurityContextHolder.getContext().authentication as? TokenAuthentication)?.userId

    protected fun <T> badRequest(): ResponseEntity<T> = ResponseEntity.badRequest().build()

    protected fun <T> noContent(): ResponseEntity<T> = ResponseEntity.noContent().build()

    protected fun <T> ok(t: T): ResponseEntity<T> = ResponseEntity.ok(t)

    protected fun <T> of(t: T?): ResponseEntity<T> = if (t != null) ok(t) else badRequest()

    protected fun ofBoolean(b: Boolean): ResponseEntity<Boolean> = if (b) noContent() else badRequest()

    protected val user: User?
        get() = if (userId == null) null else (userService.getUser(userId!!))
}