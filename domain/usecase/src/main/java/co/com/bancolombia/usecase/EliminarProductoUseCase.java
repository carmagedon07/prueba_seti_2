package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import reactor.core.publisher.Mono;

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
            });
    }
}
