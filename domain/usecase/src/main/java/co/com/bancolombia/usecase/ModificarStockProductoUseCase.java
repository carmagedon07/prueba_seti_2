package co.com.bancolombia.usecase;

import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.producto.Producto;
import co.com.bancolombia.model.port.out.ProductoRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarStockRequest;
import reactor.core.publisher.Mono;

public class ModificarStockProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;

    public ModificarStockProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public Mono<Producto> execute(Long productoId, ActualizarStockRequest request) {
        return productoRepositoryPort.findById(productoId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto", productoId)))
            .flatMap(producto -> {
                Producto actualizado = new Producto(producto.getId(), producto.getNombre(), request.getNuevoStock(), producto.getSucursalId());
                return productoRepositoryPort.save(actualizado);
            });
    }
}
