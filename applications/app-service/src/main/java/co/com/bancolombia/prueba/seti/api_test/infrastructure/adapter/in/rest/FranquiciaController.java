package co.com.bancolombia.prueba.seti.api_test.infrastructure.adapter.in.rest;

import co.com.bancolombia.usecase.ActualizarNombreFranquiciaUseCase;
import co.com.bancolombia.usecase.CrearFranquiciaUseCase;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.FranquiciaRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.FranquiciaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/franquicias")
public class FranquiciaController {

    private final CrearFranquiciaUseCase crearFranquiciaUseCase;
    private final ActualizarNombreFranquiciaUseCase actualizarNombreFranquiciaUseCase;

    public FranquiciaController(CrearFranquiciaUseCase crearFranquiciaUseCase,
                                ActualizarNombreFranquiciaUseCase actualizarNombreFranquiciaUseCase) {
        this.crearFranquiciaUseCase = crearFranquiciaUseCase;
        this.actualizarNombreFranquiciaUseCase = actualizarNombreFranquiciaUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranquiciaResponse> crear(@Valid @RequestBody FranquiciaRequest request) {
        return crearFranquiciaUseCase.execute(request);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<FranquiciaResponse> actualizarNombre(@PathVariable("id") Long id,
                                                     @Valid @RequestBody ActualizarNombreRequest request) {
        return actualizarNombreFranquiciaUseCase.execute(id, request);
    }
}
