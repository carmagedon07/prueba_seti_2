package com.prueba.seti.api_test.domain.port.out;

import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.model.ProductoMaxStock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoRepositoryPort {

    Mono<Producto> save(Producto producto);

    Mono<Boolean> existsById(Long id);

    Mono<Producto> findById(Long id);

    Mono<Void> deleteById(Long id);

    Flux<ProductoMaxStock> findMaxStockByFranquiciaId(Long franquiciaId);
}
