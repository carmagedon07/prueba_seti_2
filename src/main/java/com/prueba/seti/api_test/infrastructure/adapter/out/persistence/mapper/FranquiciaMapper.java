package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper;

import com.prueba.seti.api_test.domain.model.Franquicia;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.FranquiciaEntity;

public final class FranquiciaMapper {

    private FranquiciaMapper() {
    }

    public static FranquiciaEntity toEntity(Franquicia franquicia) {
        if (franquicia == null) {
            return null;
        }
        return new FranquiciaEntity(franquicia.getId(), franquicia.getNombre());
    }

    public static Franquicia toDomain(FranquiciaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Franquicia(entity.getId(), entity.getNombre());
    }
}

