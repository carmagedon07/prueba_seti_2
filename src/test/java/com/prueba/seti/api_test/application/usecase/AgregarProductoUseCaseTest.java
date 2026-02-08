package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ProductoRequest;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AgregarProductoUseCaseTest {

    @Test
    void creaProductoCuandoSucursalExiste() {
        ProductoRepositoryPort productoRepositoryPort = mock(ProductoRepositoryPort.class);
        SucursalRepositoryPort sucursalRepositoryPort = mock(SucursalRepositoryPort.class);
        when(sucursalRepositoryPort.existsById(2L)).thenReturn(Mono.just(true));
        when(productoRepositoryPort.save(any(Producto.class)))
            .thenReturn(Mono.just(new Producto(10L, "Producto A", 50, 2L)));

        AgregarProductoUseCase useCase = new AgregarProductoUseCase(productoRepositoryPort, sucursalRepositoryPort);

        StepVerifier.create(useCase.execute(new ProductoRequest("Producto A", 50, 2L)))
            .expectNextMatches(response -> response.getId() == 10L && response.getSucursalId() == 2L)
            .verifyComplete();
    }

    @Test
    void fallaSiSucursalNoExiste() {
        ProductoRepositoryPort productoRepositoryPort = mock(ProductoRepositoryPort.class);
        SucursalRepositoryPort sucursalRepositoryPort = mock(SucursalRepositoryPort.class);
        when(sucursalRepositoryPort.existsById(99L)).thenReturn(Mono.just(false));

        AgregarProductoUseCase useCase = new AgregarProductoUseCase(productoRepositoryPort, sucursalRepositoryPort);

        StepVerifier.create(useCase.execute(new ProductoRequest("Producto A", 50, 99L)))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }
}

