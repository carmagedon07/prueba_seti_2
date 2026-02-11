package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.sucursal.Sucursal;
import co.com.bancolombia.model.port.out.SucursalRepositoryPort;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActualizarNombreSucursalUseCaseTest {

    @Test
    void actualizaNombre() {
        SucursalRepositoryPort repositoryPort = mock(SucursalRepositoryPort.class);
        when(repositoryPort.findById(1L)).thenReturn(Mono.just(new Sucursal(1L, "Anterior", 9L)));
        when(repositoryPort.save(any(Sucursal.class))).thenReturn(Mono.just(new Sucursal(1L, "Nueva", 9L)));

        ActualizarNombreSucursalUseCase useCase = new ActualizarNombreSucursalUseCase(repositoryPort);

        StepVerifier.create(useCase.execute(1L, new ActualizarNombreRequest("Nueva")))
            .expectNextMatches(response -> response.getId() == 1L && "Nueva".equals(response.getNombre()))
            .verifyComplete();
    }

    @Test
    void fallaSiNoExiste() {
        SucursalRepositoryPort repositoryPort = mock(SucursalRepositoryPort.class);
        when(repositoryPort.findById(2L)).thenReturn(Mono.empty());

        ActualizarNombreSucursalUseCase useCase = new ActualizarNombreSucursalUseCase(repositoryPort);

        StepVerifier.create(useCase.execute(2L, new ActualizarNombreRequest("Nueva")))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }
}
