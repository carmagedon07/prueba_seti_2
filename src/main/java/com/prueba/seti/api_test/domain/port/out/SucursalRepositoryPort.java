package com.prueba.seti.api_test.domain.port.out;

import com.prueba.seti.api_test.domain.model.Sucursal;
import reactor.core.publisher.Mono;

public interface SucursalRepositoryPort {

    Mono<Sucursal> save(Sucursal sucursal);

    Mono<Boolean> existsById(Long id);

    Mono<Sucursal> findById(Long id);
}
