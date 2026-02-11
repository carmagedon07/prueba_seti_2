package co.com.bancolombia.usecase;

import co.com.bancolombia.model.dto.ProductoMaxStock;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ProductoMaxStockResponse;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.port.out.FranquiciaRepositoryPort;
import co.com.bancolombia.model.port.out.ProductoRepositoryPort;
import reactor.core.publisher.Flux;

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
                if (Boolean.FALSE.equals(exists)) {
                    return Flux.error(new ResourceNotFoundException("Franquicia", franquiciaId));
                }
                return productoRepositoryPort.findMaxStockByFranquiciaId(franquiciaId)
                    .map(this::toResponse);
            });
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
