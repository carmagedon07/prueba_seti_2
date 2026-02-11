package co.com.bancolombia.usecase;

import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.sucursal.Sucursal;
import co.com.bancolombia.model.port.out.SucursalRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import reactor.core.publisher.Mono;

public class ActualizarNombreSucursalUseCase {

    private final SucursalRepositoryPort sucursalRepositoryPort;

    public ActualizarNombreSucursalUseCase(SucursalRepositoryPort sucursalRepositoryPort) {
        this.sucursalRepositoryPort = sucursalRepositoryPort;
    }

    public Mono<Sucursal> execute(Long sucursalId, ActualizarNombreRequest request) {
        return sucursalRepositoryPort.findById(sucursalId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal", sucursalId)))
            .flatMap(sucursal -> {
                Sucursal actualizada = new Sucursal(sucursal.getId(), request.getNombre(), sucursal.getFranquiciaId());
                return sucursalRepositoryPort.save(actualizada);
            });
    }
}
