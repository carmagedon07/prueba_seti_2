package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository;

import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SucursalR2dbcRepository extends ReactiveCrudRepository<SucursalEntity, Long> {
}

