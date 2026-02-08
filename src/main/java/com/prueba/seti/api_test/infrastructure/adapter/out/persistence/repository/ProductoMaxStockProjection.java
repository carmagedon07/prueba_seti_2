package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository;

public interface ProductoMaxStockProjection {

    Long getProductoId();

    String getNombreProducto();

    Integer getStock();

    Long getSucursalId();

    String getNombreSucursal();
}

