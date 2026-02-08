package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ProductoMaxStockResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import com.prueba.seti.api_test.domain.model.ProductoMaxStock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObtenerProductoMaxStockPorSucursalUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;
    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public ObtenerProductoMaxStockPorSucursalUseCase(ProductoRepositoryPort productoRepositoryPort,
                                                     FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Flux<ProductoMaxStockResponse> execute(Long franquiciaId) {
        return franquiciaRepositoryPort.existsById(franquiciaId)
            .flatMapMany(exists -> {
                if (!exists) {
                    return Flux.error(new ResourceNotFoundException("Franquicia", franquiciaId));
                }
                Flux<ProductoMaxStockResponse> productos = productoRepositoryPort.findMaxStockByFranquiciaId(franquiciaId)
                    .map(this::toResponse)
                    .doOnNext(response -> log.debug("Max stock sucursalId={} productoId={}", response.getSucursalId(), response.getProductoId()));
                Flux<ProductoMaxStockResponse> auditoria = Flux.<ProductoMaxStockResponse>empty()
                    .doOnSubscribe(sub -> log.info("Consultando max stock por sucursal franquiciaId={}", franquiciaId));
                return Flux.merge(productos, auditoria);
            })
            .doOnComplete(() -> log.info("Consulta max stock completada franquiciaId={}", franquiciaId))
            .doOnError(error -> log.error("Error consultando max stock franquiciaId={}: {}", franquiciaId, error.getMessage(), error));
    }

    private ProductoMaxStockResponse toResponse(ProductoMaxStock producto) {
        return new ProductoMaxStockResponse(
            producto.getProductoId(),
            producto.getNombreProducto(),
            producto.getStock(),
            producto.getSucursalId(),
            producto.getNombreSucursal()
        );
    }
}
