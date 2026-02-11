package co.com.bancolombia.usecase;

import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.producto.Producto;
import co.com.bancolombia.model.port.out.ProductoRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import reactor.core.publisher.Mono;

public class ActualizarNombreProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;

    public ActualizarNombreProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public Mono<Producto> execute(Long productoId, ActualizarNombreRequest request) {
        return productoRepositoryPort.findById(productoId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto", productoId)))
            .flatMap(producto -> {
                Producto actualizado = new Producto(producto.getId(), request.getNombre(), producto.getStock(), producto.getSucursalId());
                return productoRepositoryPort.save(actualizado);
            });
    }
}
