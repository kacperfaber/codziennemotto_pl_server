package pl.codziennemotto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@PropertySource("classpath:application.\${spring.profiles.active}.properties")
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}