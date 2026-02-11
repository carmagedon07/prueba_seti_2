package co.com.bancolombia.usecase;

import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.sucursal.Sucursal;
import co.com.bancolombia.model.port.out.FranquiciaRepositoryPort;
import co.com.bancolombia.model.port.out.SucursalRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.SucursalRequest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AgregarSucursalUseCaseTest {

    @Test
    void creaSucursalCuandoFranquiciaExiste() {
        SucursalRepositoryPort sucursalRepositoryPort = mock(SucursalRepositoryPort.class);
        FranquiciaRepositoryPort franquiciaRepositoryPort = mock(FranquiciaRepositoryPort.class);
        when(franquiciaRepositoryPort.existsById(1L)).thenReturn(Mono.just(true));
        when(sucursalRepositoryPort.save(any(Sucursal.class)))
            .thenReturn(Mono.just(new Sucursal(5L, "Sucursal Centro", 1L)));

        AgregarSucursalUseCase useCase = new AgregarSucursalUseCase(sucursalRepositoryPort, franquiciaRepositoryPort);

        StepVerifier.create(useCase.execute(new SucursalRequest("Sucursal Centro", 1L)))
            .expectNextMatches(response -> response.getId() == 5L && response.getFranquiciaId() == 1L)
            .verifyComplete();
    }

    @Test
    void fallaSiFranquiciaNoExiste() {
        SucursalRepositoryPort sucursalRepositoryPort = mock(SucursalRepositoryPort.class);
        FranquiciaRepositoryPort franquiciaRepositoryPort = mock(FranquiciaRepositoryPort.class);
        when(franquiciaRepositoryPort.existsById(9L)).thenReturn(Mono.just(false));

        AgregarSucursalUseCase useCase = new AgregarSucursalUseCase(sucursalRepositoryPort, franquiciaRepositoryPort);

        StepVerifier.create(useCase.execute(new SucursalRequest("Sucursal Centro", 9L)))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }
}
