package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository;

import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.FranquiciaEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranquiciaR2dbcRepository extends ReactiveCrudRepository<FranquiciaEntity, Long> {
}

