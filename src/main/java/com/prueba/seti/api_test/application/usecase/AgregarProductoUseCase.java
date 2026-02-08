package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ProductoRequest;
import com.prueba.seti.api_test.domain.dto.ProductoResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class AgregarProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;
    private final SucursalRepositoryPort sucursalRepositoryPort;

    public AgregarProductoUseCase(ProductoRepositoryPort productoRepositoryPort,
                                  SucursalRepositoryPort sucursalRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
        this.sucursalRepositoryPort = sucursalRepositoryPort;
    }

    public Mono<ProductoResponse> execute(ProductoRequest request) {
        return Mono.zip(Mono.just(request), sucursalRepositoryPort.existsById(request.getSucursalId()))
            .flatMap(tuple -> {
                ProductoRequest req = tuple.getT1();
                Boolean exists = tuple.getT2();
                if (!exists) {
                    return Mono.error(new ResourceNotFoundException("Sucursal", req.getSucursalId()));
                }
                Producto producto = new Producto(null, req.getNombre(), req.getStock(), req.getSucursalId());
                return productoRepositoryPort.save(producto)
                    .map(saved -> new ProductoResponse(saved.getId(), saved.getNombre(), saved.getStock(), saved.getSucursalId()));
            })
            .doOnNext(response -> log.info("Producto creado id={} sucursalId={}", response.getId(), response.getSucursalId()))
            .doOnError(error -> log.error("Error creando producto: {}", error.getMessage(), error))
            .doOnSuccess(response -> log.debug("Flujo completado para crear producto"));
    }
}
