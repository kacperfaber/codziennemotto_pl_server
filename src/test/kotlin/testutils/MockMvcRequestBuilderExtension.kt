package testutils

import org.springframework.test.web.servlet.MockHttpServletRequestDsl

fun MockHttpServletRequestDsl.auth(userId: Int, email: String = "kacperf1234@gmail.com", name: String = "kacperfaber") {
    this.header("Authorization", "{\"userId\": \"$userId\", \"userName\": \"$name\", \"userEmail\": \"$email\"}")
}