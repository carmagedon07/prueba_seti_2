package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.franquicia.Franquicia;
import co.com.bancolombia.model.port.out.FranquiciaRepositoryPort;
import reactor.core.publisher.Mono;

public class ActualizarNombreFranquiciaUseCase {

    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public ActualizarNombreFranquiciaUseCase(FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<Franquicia> execute(Long franquiciaId, ActualizarNombreRequest request) {
        return franquiciaRepositoryPort.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", franquiciaId)))
            .flatMap(franquicia -> {
                Franquicia actualizada = new Franquicia(franquicia.getId(), request.getNombre());
                return franquiciaRepositoryPort.save(actualizada);
            });
    }
}
