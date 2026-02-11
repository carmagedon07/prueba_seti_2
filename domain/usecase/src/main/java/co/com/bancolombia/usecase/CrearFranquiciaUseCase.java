package co.com.bancolombia.usecase;

import co.com.bancolombia.model.franquicia.Franquicia;
import co.com.bancolombia.model.port.out.FranquiciaRepositoryPort;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.FranquiciaRequest;
import reactor.core.publisher.Mono;

public class CrearFranquiciaUseCase {

    private final FranquiciaRepositoryPort franquiciaRepositoryPort;

    public CrearFranquiciaUseCase(FranquiciaRepositoryPort franquiciaRepositoryPort) {
        this.franquiciaRepositoryPort = franquiciaRepositoryPort;
    }

    public Mono<Franquicia> execute(FranquiciaRequest request) {
        Franquicia franquicia = new Franquicia(null, request.getNombre());
        return franquiciaRepositoryPort.save(franquicia);
    }
}
