package pl.codziennemotto.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
@Profile("prod")
class ProdDataSourceConfiguration {
    @Value("\${db.username}")
    lateinit var username: String

    @Value("\${db.password}")
    lateinit var password: String

    // "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;CASE_INSENSITIVE_IDENTIFIERS=TRUE"
    @Value("\${db.url}")
    lateinit var url: String

    @Bean
    fun prodDataSource(): DataSource {
        val d = DriverManagerDataSource()
        d.setDriverClassName("com.mysql.cj.jdbc.Driver")
        d.username = username
        d.password = password
        d.url = url
        return d
    }
}