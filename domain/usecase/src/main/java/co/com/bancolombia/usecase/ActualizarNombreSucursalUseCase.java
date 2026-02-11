package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.SucursalResponse;
import co.com.bancolombia.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import co.com.bancolombia.prueba.seti.api_test.domain.model.Sucursal;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import reactor.core.publisher.Mono;

public class ActualizarNombreSucursalUseCase {

    private final SucursalRepositoryPort sucursalRepositoryPort;

    public ActualizarNombreSucursalUseCase(SucursalRepositoryPort sucursalRepositoryPort) {
        this.sucursalRepositoryPort = sucursalRepositoryPort;
    }

    public Mono<SucursalResponse> execute(Long sucursalId, ActualizarNombreRequest request) {
        return sucursalRepositoryPort.findById(sucursalId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal", sucursalId)))
            .flatMap(sucursal -> {
                Sucursal actualizada = new Sucursal(sucursal.getId(), request.getNombre(), sucursal.getFranquiciaId());
                return sucursalRepositoryPort.save(actualizada);
            })
            .map(saved -> new SucursalResponse(saved.getId(), saved.getNombre(), saved.getFranquiciaId()));
    }
}
