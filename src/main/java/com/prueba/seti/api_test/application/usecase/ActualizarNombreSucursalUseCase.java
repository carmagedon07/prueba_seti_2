package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import com.prueba.seti.api_test.domain.dto.SucursalResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Sucursal;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ActualizarNombreSucursalUseCase {

    private final SucursalRepositoryPort sucursalRepositoryPort;

    public ActualizarNombreSucursalUseCase(SucursalRepositoryPort sucursalRepositoryPort) {
        this.sucursalRepositoryPort = sucursalRepositoryPort;
    }

    public Mono<SucursalResponse> execute(Long sucursalId, ActualizarNombreRequest request) {
        return sucursalRepositoryPort.findById(sucursalId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal", sucursalId)))
            .flatMap(sucursal -> {
                Sucursal actualizada = new Sucursal(sucursal.getId(), request.getNombre(), sucursal.getFranquiciaId());
                return sucursalRepositoryPort.save(actualizada);
            })
            .map(saved -> new SucursalResponse(saved.getId(), saved.getNombre(), saved.getFranquiciaId()))
            .doOnNext(response -> log.info("Nombre de sucursal actualizado id={} nombre={}", response.getId(), response.getNombre()))
            .doOnError(error -> log.error("Error actualizando sucursal id={}: {}", sucursalId, error.getMessage(), error));
    }
}

