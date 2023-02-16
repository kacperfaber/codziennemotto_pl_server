package pl.codziennemotto.security

import org.springframework.security.access.prepost.PreAuthorize

@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("authentication.authenticated == true")
annotation class Authenticated
