package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.FranquiciaRequest;
import com.prueba.seti.api_test.domain.dto.FranquiciaResponse;
import com.prueba.seti.api_test.domain.model.Franquicia;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrearFranquiciaUseCase {

    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public CrearFranquiciaUseCase(FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<FranquiciaResponse> execute(FranquiciaRequest request) {
        return Mono.just(request)
            .flatMap(req -> {
                Franquicia franquicia = new Franquicia(null, req.getNombre());
                return Mono.zip(Mono.just(req), franquiciaRepositoryPort.save(franquicia));
            })
            .map(tuple -> new FranquiciaResponse(tuple.getT2().getId(), tuple.getT2().getNombre()))
            .doOnNext(response -> log.info("Franquicia creada id={} nombre={}", response.getId(), response.getNombre()))
            .doOnError(error -> log.error("Error creando franquicia: {}", error.getMessage(), error))
            .doOnSuccess(response -> log.debug("Flujo completado para crear franquicia"));
    }
}
