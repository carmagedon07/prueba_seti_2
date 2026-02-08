package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository;

import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductoR2dbcRepository extends ReactiveCrudRepository<ProductoEntity, Long> {

    @Query("""
        SELECT p.id AS productoId,
               p.nombre AS nombreProducto,
               p.stock AS stock,
               s.id AS sucursalId,
               s.nombre AS nombreSucursal
        FROM productos p
        INNER JOIN sucursales s ON s.id = p.sucursal_id
        INNER JOIN franquicias f ON f.id = s.franquicia_id
        INNER JOIN (
            SELECT s2.id AS sucursalId,
                   MAX(p2.stock) AS max_stock
            FROM sucursales s2
            INNER JOIN productos p2 ON p2.sucursal_id = s2.id
            WHERE s2.franquicia_id = :franquiciaId
            GROUP BY s2.id
        ) max_por_sucursal
            ON max_por_sucursal.sucursalId = s.id AND p.stock = max_por_sucursal.max_stock
        WHERE f.id = :franquiciaId
        """)
    Flux<ProductoMaxStockProjection> findMaxStockByFranquiciaId(Long franquiciaId);
}
