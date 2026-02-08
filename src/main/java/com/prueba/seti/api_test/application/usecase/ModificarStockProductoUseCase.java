package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ActualizarStockRequest;
import com.prueba.seti.api_test.domain.dto.ProductoResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ModificarStockProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;

    public ModificarStockProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public Mono<ProductoResponse> execute(Long productoId, ActualizarStockRequest request) {
        return productoRepositoryPort.findById(productoId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto", productoId)))
            .flatMap(producto -> {
                Producto actualizado = new Producto(producto.getId(), producto.getNombre(), request.getNuevoStock(), producto.getSucursalId());
                return productoRepositoryPort.save(actualizado);
            })
            .map(saved -> new ProductoResponse(saved.getId(), saved.getNombre(), saved.getStock(), saved.getSucursalId()))
            .doOnNext(response -> log.info("Stock actualizado productoId={} nuevoStock={}", response.getId(), response.getStock()))
            .doOnError(error -> log.error("Error actualizando stock productoId={}: {}", productoId, error.getMessage(), error))
            .doOnSuccess(response -> log.debug("Flujo completado para actualizar stock"));
    }
}
