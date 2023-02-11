package pl.codziennemotto.data

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories("pl.codziennemotto.data.dao")
@Configuration
class DataJpaConfiguration