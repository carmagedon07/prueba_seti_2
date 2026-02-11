package co.com.bancolombia.usecase;

import co.com.bancolombia.prueba.seti.api_test.domain.dto.FranquiciaRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.FranquiciaResponse;
import co.com.bancolombia.prueba.seti.api_test.domain.model.Franquicia;
import co.com.bancolombia.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import reactor.core.publisher.Mono;

public class CrearFranquiciaUseCase {

    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public CrearFranquiciaUseCase(FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<FranquiciaResponse> execute(FranquiciaRequest request) {
        return Mono.just(request)
            .flatMap(req -> {
                Franquicia franquicia = new Franquicia(null, req.getNombre());
                return Mono.zip(Mono.just(req), franquiciaRepositoryPort.save(franquicia));
            })
            .map(tuple -> new FranquiciaResponse(tuple.getT2().getId(), tuple.getT2().getNombre()));
    }
}
