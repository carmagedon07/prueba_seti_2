package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.FranquiciaRequest;
import com.prueba.seti.api_test.domain.dto.FranquiciaResponse;
import com.prueba.seti.api_test.domain.model.Franquicia;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import reactor.core.publisher.Mono;

public class CrearFranquiciaUseCase {

    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public CrearFranquiciaUseCase(FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<FranquiciaResponse> execute(FranquiciaRequest request) {
        Franquicia franquicia = new Franquicia(null, request.getNombre());
        return franquiciaRepositoryPort.save(franquicia)
            .map(saved -> new FranquiciaResponse(saved.getId(), saved.getNombre()));
    }
}

