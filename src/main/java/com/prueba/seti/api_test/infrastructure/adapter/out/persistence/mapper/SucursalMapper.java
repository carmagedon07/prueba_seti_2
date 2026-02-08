package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper;

import com.prueba.seti.api_test.domain.model.Sucursal;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.SucursalEntity;

public final class SucursalMapper {

    private SucursalMapper() {
    }

    public static SucursalEntity toEntity(Sucursal sucursal) {
        if (sucursal == null) {
            return null;
        }
        return new SucursalEntity(sucursal.getId(), sucursal.getNombre(), sucursal.getFranquiciaId());
    }

    public static Sucursal toDomain(SucursalEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Sucursal(entity.getId(), entity.getNombre(), entity.getFranquiciaId());
    }
}

