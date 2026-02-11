package co.com.bancolombia.prueba.seti.api_test.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages =
    "co.com.bancolombia.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository")
public class R2dbcRepositoriesConfig {
}

