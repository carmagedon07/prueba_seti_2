package com.prueba.seti.api_test.infrastructure.adapter.out.persistence;

import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.model.ProductoMaxStock;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper.ProductoMapper;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository.ProductoMaxStockProjection;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository.ProductoR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {

    private final ProductoR2dbcRepository productoR2dbcRepository;

    public ProductoRepositoryAdapter(ProductoR2dbcRepository productoR2dbcRepository) {
        this.productoR2dbcRepository = productoR2dbcRepository;
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return productoR2dbcRepository.save(ProductoMapper.toEntity(producto))
            .map(ProductoMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return productoR2dbcRepository.existsById(id);
    }

    @Override
    public Mono<Producto> findById(Long id) {
        return productoR2dbcRepository.findById(id)
            .map(ProductoMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return productoR2dbcRepository.deleteById(id);
    }

    @Override
    public Flux<ProductoMaxStock> findMaxStockByFranquiciaId(Long franquiciaId) {
        return productoR2dbcRepository.findMaxStockByFranquiciaId(franquiciaId)
            .map(this::toMaxStockDomain);
    }

    private ProductoMaxStock toMaxStockDomain(ProductoMaxStockProjection projection) {
        return new ProductoMaxStock(
            projection.getProductoId(),
            projection.getNombreProducto(),
            projection.getStock(),
            projection.getSucursalId(),
            projection.getNombreSucursal()
        );
    }
}

