package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.franquicia.Franquicia;
import co.com.bancolombia.model.port.out.FranquiciaRepositoryPort;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActualizarNombreFranquiciaUseCaseTest {

    @Test
    void actualizaNombre() {
        FranquiciaRepositoryPort repositoryPort = mock(FranquiciaRepositoryPort.class);
        when(repositoryPort.findById(1L)).thenReturn(Mono.just(new Franquicia(1L, "Anterior")));
        when(repositoryPort.save(any(Franquicia.class))).thenReturn(Mono.just(new Franquicia(1L, "Nueva")));

        ActualizarNombreFranquiciaUseCase useCase = new ActualizarNombreFranquiciaUseCase(repositoryPort);

        StepVerifier.create(useCase.execute(1L, new ActualizarNombreRequest("Nueva")))
            .expectNextMatches(response -> response.getId() == 1L && "Nueva".equals(response.getNombre()))
            .verifyComplete();
    }

    @Test
    void fallaSiNoExiste() {
        FranquiciaRepositoryPort repositoryPort = mock(FranquiciaRepositoryPort.class);
        when(repositoryPort.findById(2L)).thenReturn(Mono.empty());

        ActualizarNombreFranquiciaUseCase useCase = new ActualizarNombreFranquiciaUseCase(repositoryPort);

        StepVerifier.create(useCase.execute(2L, new ActualizarNombreRequest("Nueva")))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }
}
