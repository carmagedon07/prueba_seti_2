package co.com.bancolombia.config;

import co.com.bancolombia.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public FranquiciaRepositoryPort franquiciaRepositoryPort() {
            return mock(FranquiciaRepositoryPort.class);
        }

        @Bean
        public SucursalRepositoryPort sucursalRepositoryPort() {
            return mock(SucursalRepositoryPort.class);
        }

        @Bean
        public ProductoRepositoryPort productoRepositoryPort() {
            return mock(ProductoRepositoryPort.class);
        }

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}
