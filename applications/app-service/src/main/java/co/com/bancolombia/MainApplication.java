package co.com.bancolombia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = {"co.com.bancolombia", "co.com.bancolombia.prueba.seti.api_test"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "co\\.com\\.bancolombia\\.usecase\\..*")
        })
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
