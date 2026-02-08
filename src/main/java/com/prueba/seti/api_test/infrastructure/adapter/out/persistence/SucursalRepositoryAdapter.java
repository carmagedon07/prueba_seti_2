package com.prueba.seti.api_test.infrastructure.adapter.out.persistence;

import com.prueba.seti.api_test.domain.model.Sucursal;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper.SucursalMapper;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository.SucursalR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SucursalRepositoryAdapter implements SucursalRepositoryPort {

    private final SucursalR2dbcRepository sucursalR2dbcRepository;

    public SucursalRepositoryAdapter(SucursalR2dbcRepository sucursalR2dbcRepository) {
        this.sucursalR2dbcRepository = sucursalR2dbcRepository;
    }

    @Override
    public Mono<Sucursal> save(Sucursal sucursal) {
        return sucursalR2dbcRepository.save(SucursalMapper.toEntity(sucursal))
            .map(SucursalMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return sucursalR2dbcRepository.existsById(id);
    }

    @Override
    public Mono<Sucursal> findById(Long id) {
        return sucursalR2dbcRepository.findById(id)
            .map(SucursalMapper::toDomain);
    }
}
