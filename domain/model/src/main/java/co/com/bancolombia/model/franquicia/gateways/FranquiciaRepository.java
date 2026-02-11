package co.com.bancolombia.model.franquicia.gateways;

import co.com.bancolombia.model.franquicia.Franquicia;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository {
    Mono<Franquicia> save(Franquicia franquicia);
}
