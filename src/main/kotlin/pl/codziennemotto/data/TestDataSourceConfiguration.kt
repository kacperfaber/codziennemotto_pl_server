package pl.codziennemotto.data

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
@Profile("test")
class TestDataSourceConfiguration {
    @Bean
    fun testDataSource(): DataSource {
        val d = DriverManagerDataSource()
        d.setDriverClassName("org.h2.Driver")
        d.username = "sa"
        d.password = "sa"
        d.url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;CASE_INSENSITIVE_IDENTIFIERS=TRUE"
        return d
    }
}