package co.com.bancolombia.usecase;

import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.sucursal.Sucursal;
import co.com.bancolombia.model.port.out.FranquiciaRepositoryPort;
import co.com.bancolombia.model.port.out.SucursalRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.SucursalRequest;
import reactor.core.publisher.Mono;

public class AgregarSucursalUseCase {

    private final SucursalRepositoryPort sucursalRepositoryPort;
    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public AgregarSucursalUseCase(SucursalRepositoryPort sucursalRepositoryPort,
                                  FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.sucursalRepositoryPort = sucursalRepositoryPort;
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<Sucursal> execute(SucursalRequest request) {
        return franquiciaRepositoryPort.existsById(request.getFranquiciaId())
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new ResourceNotFoundException("Franquicia", request.getFranquiciaId()));
                }
                Sucursal sucursal = new Sucursal(null, request.getNombre(), request.getFranquiciaId());
                return sucursalRepositoryPort.save(sucursal);
            });
    }
}
