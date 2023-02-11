package pl.codziennemotto.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import pl.codziennemotto.security.TokenAuthentication

open class ControllerBase  {
    protected val userId: Int?
        get() = (SecurityContextHolder.getContext().authentication as? TokenAuthentication)?.userId

    protected fun <T> badRequest(): ResponseEntity<T> = ResponseEntity.badRequest().build()

    protected fun <T> noContent(): ResponseEntity<T> = ResponseEntity.noContent().build()

    protected fun <T> ok(t: T): ResponseEntity<T> = ResponseEntity.ok(t)
}