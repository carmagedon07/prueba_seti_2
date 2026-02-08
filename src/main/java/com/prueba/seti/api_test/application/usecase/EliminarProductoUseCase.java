package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EliminarProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;

    public EliminarProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public Mono<Void> execute(Long productoId) {
        return productoRepositoryPort.existsById(productoId)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new ResourceNotFoundException("Producto", productoId));
                }
                return productoRepositoryPort.deleteById(productoId);
            })
            .doOnSuccess(unused -> log.info("Producto eliminado id={}", productoId))
            .doOnError(error -> log.error("Error eliminando producto id={}: {}", productoId, error.getMessage(), error));
    }
}
