package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActualizarNombreProductoUseCaseTest {

    @Test
    void actualizaNombre() {
        ProductoRepositoryPort repositoryPort = mock(ProductoRepositoryPort.class);
        when(repositoryPort.findById(1L)).thenReturn(Mono.just(new Producto(1L, "Anterior", 10, 8L)));
        when(repositoryPort.save(any(Producto.class))).thenReturn(Mono.just(new Producto(1L, "Nueva", 10, 8L)));

        ActualizarNombreProductoUseCase useCase = new ActualizarNombreProductoUseCase(repositoryPort);

        StepVerifier.create(useCase.execute(1L, new ActualizarNombreRequest("Nueva")))
            .expectNextMatches(response -> response.getId() == 1L && "Nueva".equals(response.getNombre()))
            .verifyComplete();
    }

    @Test
    void fallaSiNoExiste() {
        ProductoRepositoryPort repositoryPort = mock(ProductoRepositoryPort.class);
        when(repositoryPort.findById(2L)).thenReturn(Mono.empty());

        ActualizarNombreProductoUseCase useCase = new ActualizarNombreProductoUseCase(repositoryPort);

        StepVerifier.create(useCase.execute(2L, new ActualizarNombreRequest("Nueva")))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }
}

