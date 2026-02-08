package com.prueba.seti.api_test.domain.port.out;

import com.prueba.seti.api_test.domain.model.Franquicia;
import reactor.core.publisher.Mono;

public interface FranquiciaRepositoryPort {

    Mono<Franquicia> save(Franquicia franquicia);

    Mono<Boolean> existsById(Long id);
}

