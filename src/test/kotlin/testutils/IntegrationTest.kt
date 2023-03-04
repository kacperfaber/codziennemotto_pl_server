package testutils

import org.junit.jupiter.api.Tag

@Retention(AnnotationRetention.RUNTIME)
@Tag("IntegrationTest")
annotation class IntegrationTest