package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import com.prueba.seti.api_test.domain.dto.ProductoResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ActualizarNombreProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;

    public ActualizarNombreProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public Mono<ProductoResponse> execute(Long productoId, ActualizarNombreRequest request) {
        return productoRepositoryPort.findById(productoId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto", productoId)))
            .flatMap(producto -> {
                Producto actualizado = new Producto(producto.getId(), request.getNombre(), producto.getStock(), producto.getSucursalId());
                return productoRepositoryPort.save(actualizado);
            })
            .map(saved -> new ProductoResponse(saved.getId(), saved.getNombre(), saved.getStock(), saved.getSucursalId()))
            .doOnNext(response -> log.info("Nombre de producto actualizado id={} nombre={}", response.getId(), response.getNombre()))
            .doOnError(error -> log.error("Error actualizando producto id={}: {}", productoId, error.getMessage(), error));
    }
}

