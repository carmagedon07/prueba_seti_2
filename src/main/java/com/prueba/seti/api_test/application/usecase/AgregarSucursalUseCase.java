package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.SucursalRequest;
import com.prueba.seti.api_test.domain.dto.SucursalResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Sucursal;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class AgregarSucursalUseCase {

    private final SucursalRepositoryPort sucursalRepositoryPort;
    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public AgregarSucursalUseCase(SucursalRepositoryPort sucursalRepositoryPort,
                                  FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.sucursalRepositoryPort = sucursalRepositoryPort;
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<SucursalResponse> execute(SucursalRequest request) {
        return Mono.zip(Mono.just(request), franquiciaRepositoryPort.existsById(request.getFranquiciaId()))
            .flatMap(tuple -> {
                SucursalRequest req = tuple.getT1();
                Boolean exists = tuple.getT2();
                if (!exists) {
                    return Mono.error(new ResourceNotFoundException("Franquicia", req.getFranquiciaId()));
                }
                Sucursal sucursal = new Sucursal(null, req.getNombre(), req.getFranquiciaId());
                return sucursalRepositoryPort.save(sucursal)
                    .map(saved -> new SucursalResponse(saved.getId(), saved.getNombre(), saved.getFranquiciaId()));
            })
            .doOnNext(response -> log.info("Sucursal creada id={} franquiciaId={}", response.getId(), response.getFranquiciaId()))
            .doOnError(error -> log.error("Error creando sucursal: {}", error.getMessage(), error))
            .doOnSuccess(response -> log.debug("Flujo completado para crear sucursal"));
    }
}
