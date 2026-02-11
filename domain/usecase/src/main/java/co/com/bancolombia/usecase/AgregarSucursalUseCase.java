package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.dto.SucursalRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.SucursalResponse;
import co.com.bancolombia.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import co.com.bancolombia.prueba.seti.api_test.domain.model.Sucursal;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import reactor.core.publisher.Mono;

public class AgregarSucursalUseCase {

    private final SucursalRepositoryPort sucursalRepositoryPort;
    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public AgregarSucursalUseCase(SucursalRepositoryPort sucursalRepositoryPort,
                                  FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.sucursalRepositoryPort = sucursalRepositoryPort;
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<SucursalResponse> execute(SucursalRequest request) {
        return Mono.zip(Mono.just(request), franquiciaRepositoryPort.existsById(request.getFranquiciaId()))
            .flatMap(tuple -> {
                SucursalRequest req = tuple.getT1();
                Boolean exists = tuple.getT2();
                if (!exists) {
                    return Mono.error(new ResourceNotFoundException("Franquicia", req.getFranquiciaId()));
                }
                Sucursal sucursal = new Sucursal(null, req.getNombre(), req.getFranquiciaId());
                return sucursalRepositoryPort.save(sucursal)
                    .map(saved -> new SucursalResponse(saved.getId(), saved.getNombre(), saved.getFranquiciaId()));
            });
    }
}
