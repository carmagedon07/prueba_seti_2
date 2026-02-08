package com.prueba.seti.api_test.infrastructure.adapter.out.persistence;

import com.prueba.seti.api_test.domain.model.Franquicia;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper.FranquiciaMapper;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository.FranquiciaR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FranquiciaRepositoryAdapter implements FranquiciaRepositoryPort {

    private final FranquiciaR2dbcRepository franquiciaR2dbcRepository;

    public FranquiciaRepositoryAdapter(FranquiciaR2dbcRepository franquiciaR2dbcRepository) {
        this.franquiciaR2dbcRepository = franquiciaR2dbcRepository;
    }

    @Override
    public Mono<Franquicia> save(Franquicia franquicia) {
        return franquiciaR2dbcRepository.save(FranquiciaMapper.toEntity(franquicia))
            .map(FranquiciaMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return franquiciaR2dbcRepository.existsById(id);
    }
}

