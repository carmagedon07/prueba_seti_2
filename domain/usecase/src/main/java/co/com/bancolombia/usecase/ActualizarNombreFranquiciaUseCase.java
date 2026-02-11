package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.FranquiciaResponse;
import co.com.bancolombia.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import co.com.bancolombia.prueba.seti.api_test.domain.model.Franquicia;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import reactor.core.publisher.Mono;

public class ActualizarNombreFranquiciaUseCase {

    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public ActualizarNombreFranquiciaUseCase(FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<FranquiciaResponse> execute(Long franquiciaId, ActualizarNombreRequest request) {
        return franquiciaRepositoryPort.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", franquiciaId)))
            .flatMap(franquicia -> {
                Franquicia actualizada = new Franquicia(franquicia.getId(), request.getNombre());
                return franquiciaRepositoryPort.save(actualizada);
            })
            .map(saved -> new FranquiciaResponse(saved.getId(), saved.getNombre()));
    }
}
