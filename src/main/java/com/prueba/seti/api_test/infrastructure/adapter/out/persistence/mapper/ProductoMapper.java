package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper;

import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.ProductoEntity;

public final class ProductoMapper {

    private ProductoMapper() {
    }

    public static ProductoEntity toEntity(Producto producto) {
        if (producto == null) {
            return null;
        }
        return new ProductoEntity(producto.getId(), producto.getNombre(), producto.getStock(), producto.getSucursalId());
    }

    public static Producto toDomain(ProductoEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Producto(entity.getId(), entity.getNombre(), entity.getStock(), entity.getSucursalId());
    }
}

