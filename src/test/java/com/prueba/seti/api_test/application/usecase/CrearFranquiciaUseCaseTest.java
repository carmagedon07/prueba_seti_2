package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.FranquiciaRequest;
import com.prueba.seti.api_test.domain.model.Franquicia;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CrearFranquiciaUseCaseTest {

    @Test
    void creaFranquicia() {
        FranquiciaRepositoryPort repositoryPort = mock(FranquiciaRepositoryPort.class);
        when(repositoryPort.save(any(Franquicia.class)))
            .thenReturn(Mono.just(new Franquicia(1L, "Franquicia Demo")));

        CrearFranquiciaUseCase useCase = new CrearFranquiciaUseCase(repositoryPort);

        StepVerifier.create(useCase.execute(new FranquiciaRequest("Franquicia Demo")))
            .expectNextMatches(response -> response.getId() == 1L
                && "Franquicia Demo".equals(response.getNombre()))
            .verifyComplete();
    }
}
