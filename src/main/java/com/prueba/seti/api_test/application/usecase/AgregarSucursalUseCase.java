package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.SucursalRequest;
import com.prueba.seti.api_test.domain.dto.SucursalResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Sucursal;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import reactor.core.publisher.Mono;

public class AgregarSucursalUseCase {

    private final SucursalRepositoryPort sucursalRepositoryPort;
    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public AgregarSucursalUseCase(SucursalRepositoryPort sucursalRepositoryPort,
                                  FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.sucursalRepositoryPort = sucursalRepositoryPort;
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<SucursalResponse> execute(SucursalRequest request) {
        return franquiciaRepositoryPort.existsById(request.getFranquiciaId())
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new ResourceNotFoundException("Franquicia", request.getFranquiciaId()));
                }
                Sucursal sucursal = new Sucursal(null, request.getNombre(), request.getFranquiciaId());
                return sucursalRepositoryPort.save(sucursal)
                    .map(saved -> new SucursalResponse(saved.getId(), saved.getNombre(), saved.getFranquiciaId()));
            });
    }
}

