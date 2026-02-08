package com.prueba.seti.api_test.infrastructure.adapter.in.rest;

import com.prueba.seti.api_test.application.usecase.ActualizarNombreFranquiciaUseCase;
import com.prueba.seti.api_test.application.usecase.CrearFranquiciaUseCase;
import com.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import com.prueba.seti.api_test.domain.dto.FranquiciaRequest;
import com.prueba.seti.api_test.domain.dto.FranquiciaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
