package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import co.com.bancolombia.prueba.seti.api_test.domain.model.ProductoMaxStock;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ObtenerProductoMaxStockPorSucursalUseCaseTest {

    @Test
    void retornaMaxStockPorSucursal() {
        ProductoRepositoryPort productoRepositoryPort = mock(ProductoRepositoryPort.class);
        FranquiciaRepositoryPort franquiciaRepositoryPort = mock(FranquiciaRepositoryPort.class);
        when(franquiciaRepositoryPort.existsById(1L)).thenReturn(Mono.just(true));
        when(productoRepositoryPort.findMaxStockByFranquiciaId(1L))
            .thenReturn(Flux.just(
                new ProductoMaxStock(1L, "Producto A", 50, 10L, "Sucursal Centro"),
                new ProductoMaxStock(2L, "Producto B", 50, 11L, "Sucursal Norte")
            ));

        ObtenerProductoMaxStockPorSucursalUseCase useCase = new ObtenerProductoMaxStockPorSucursalUseCase(
            productoRepositoryPort, franquiciaRepositoryPort);

        StepVerifier.create(useCase.execute(1L))
            .expectNextCount(2)
            .verifyComplete();
    }

    @Test
    void fallaSiFranquiciaNoExiste() {
        ProductoRepositoryPort productoRepositoryPort = mock(ProductoRepositoryPort.class);
        FranquiciaRepositoryPort franquiciaRepositoryPort = mock(FranquiciaRepositoryPort.class);
        when(franquiciaRepositoryPort.existsById(99L)).thenReturn(Mono.just(false));

        ObtenerProductoMaxStockPorSucursalUseCase useCase = new ObtenerProductoMaxStockPorSucursalUseCase(
            productoRepositoryPort, franquiciaRepositoryPort);

        StepVerifier.create(useCase.execute(99L))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }
}

