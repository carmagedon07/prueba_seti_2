package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EliminarProductoUseCaseTest {

    @Test
    void eliminaProductoExistente() {
        ProductoRepositoryPort productoRepositoryPort = mock(ProductoRepositoryPort.class);
        when(productoRepositoryPort.existsById(3L)).thenReturn(Mono.just(true));
        when(productoRepositoryPort.deleteById(3L)).thenReturn(Mono.empty());

        EliminarProductoUseCase useCase = new EliminarProductoUseCase(productoRepositoryPort);

        StepVerifier.create(useCase.execute(3L))
            .verifyComplete();
    }

    @Test
    void fallaSiProductoNoExiste() {
        ProductoRepositoryPort productoRepositoryPort = mock(ProductoRepositoryPort.class);
        when(productoRepositoryPort.existsById(404L)).thenReturn(Mono.just(false));

        EliminarProductoUseCase useCase = new EliminarProductoUseCase(productoRepositoryPort);

        StepVerifier.create(useCase.execute(404L))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }
}

