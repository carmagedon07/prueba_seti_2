package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import com.prueba.seti.api_test.domain.dto.FranquiciaResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Franquicia;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ActualizarNombreFranquiciaUseCase {

    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public ActualizarNombreFranquiciaUseCase(FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<FranquiciaResponse> execute(Long franquiciaId, ActualizarNombreRequest request) {
        return franquiciaRepositoryPort.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", franquiciaId)))
            .flatMap(franquicia -> {
                Franquicia actualizada = new Franquicia(franquicia.getId(), request.getNombre());
                return franquiciaRepositoryPort.save(actualizada);
            })
            .map(saved -> new FranquiciaResponse(saved.getId(), saved.getNombre()))
            .doOnNext(response -> log.info("Nombre de franquicia actualizado id={} nombre={}", response.getId(), response.getNombre()))
            .doOnError(error -> log.error("Error actualizando franquicia id={}: {}", franquiciaId, error.getMessage(), error));
    }
}

