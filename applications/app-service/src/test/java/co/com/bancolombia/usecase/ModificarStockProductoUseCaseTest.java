package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarStockRequest;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.producto.Producto;
import co.com.bancolombia.model.port.out.ProductoRepositoryPort;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ModificarStockProductoUseCaseTest {

    @Test
    void actualizaStockProducto() {
        ProductoRepositoryPort productoRepositoryPort = mock(ProductoRepositoryPort.class);
        when(productoRepositoryPort.findById(1L))
            .thenReturn(Mono.just(new Producto(1L, "Producto A", 10, 2L)));
        when(productoRepositoryPort.save(any(Producto.class)))
            .thenReturn(Mono.just(new Producto(1L, "Producto A", 99, 2L)));

        ModificarStockProductoUseCase useCase = new ModificarStockProductoUseCase(productoRepositoryPort);

        StepVerifier.create(useCase.execute(1L, new ActualizarStockRequest(99)))
            .expectNextMatches(response -> response.getId() == 1L && response.getStock() == 99)
            .verifyComplete();
    }

    @Test
    void fallaSiProductoNoExiste() {
        ProductoRepositoryPort productoRepositoryPort = mock(ProductoRepositoryPort.class);
        when(productoRepositoryPort.findById(22L)).thenReturn(Mono.empty());

        ModificarStockProductoUseCase useCase = new ModificarStockProductoUseCase(productoRepositoryPort);

        StepVerifier.create(useCase.execute(22L, new ActualizarStockRequest(10)))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }
}
