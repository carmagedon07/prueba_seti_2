package co.com.bancolombia.usecase;

import co.com.bancolombia.model.dto.AgregarProductoRequest;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.producto.Producto;
import co.com.bancolombia.model.port.out.ProductoRepositoryPort;
import co.com.bancolombia.model.port.out.SucursalRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ProductoRequest;
import reactor.core.publisher.Mono;

public class AgregarProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;
    private final SucursalRepositoryPort sucursalRepositoryPort;

    public AgregarProductoUseCase(ProductoRepositoryPort productoRepositoryPort,
                                  SucursalRepositoryPort sucursalRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
        this.sucursalRepositoryPort = sucursalRepositoryPort;
    }

    public Mono<Producto> execute(ProductoRequest request) {
        return sucursalRepositoryPort.existsById(request.getSucursalId())
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new ResourceNotFoundException("Sucursal", request.getSucursalId()));
                }
                Producto producto = new Producto(null, request.getNombre(), request.getStock(), request.getSucursalId());
                return productoRepositoryPort.save(producto);
            });
    }
}
