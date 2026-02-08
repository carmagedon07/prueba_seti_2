package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ProductoRequest;
import com.prueba.seti.api_test.domain.dto.ProductoResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import reactor.core.publisher.Mono;

public class AgregarProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;
    private final SucursalRepositoryPort sucursalRepositoryPort;

    public AgregarProductoUseCase(ProductoRepositoryPort productoRepositoryPort,
                                  SucursalRepositoryPort sucursalRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
        this.sucursalRepositoryPort = sucursalRepositoryPort;
    }

    public Mono<ProductoResponse> execute(ProductoRequest request) {
        return sucursalRepositoryPort.existsById(request.getSucursalId())
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new ResourceNotFoundException("Sucursal", request.getSucursalId()));
                }
                Producto producto = new Producto(null, request.getNombre(), request.getStock(), request.getSucursalId());
                return productoRepositoryPort.save(producto)
                    .map(saved -> new ProductoResponse(saved.getId(), saved.getNombre(), saved.getStock(), saved.getSucursalId()));
            });
    }
}

