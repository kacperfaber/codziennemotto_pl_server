package testutils

import org.junit.jupiter.api.Tag

@Retention(AnnotationRetention.RUNTIME)
@Tag("UnitTest")
annotation class UnitTest()
