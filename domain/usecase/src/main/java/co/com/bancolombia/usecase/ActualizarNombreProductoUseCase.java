package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ProductoResponse;
import co.com.bancolombia.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import co.com.bancolombia.prueba.seti.api_test.domain.model.Producto;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import reactor.core.publisher.Mono;

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
            .map(saved -> new ProductoResponse(saved.getId(), saved.getNombre(), saved.getStock(), saved.getSucursalId()));
    }
}
