package com.prueba.seti.api_test.infrastructure.adapter.in.rest;

import com.prueba.seti.api_test.application.usecase.CrearFranquiciaUseCase;
import com.prueba.seti.api_test.domain.dto.FranquiciaRequest;
import com.prueba.seti.api_test.domain.dto.FranquiciaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    public FranquiciaController(CrearFranquiciaUseCase crearFranquiciaUseCase) {
        this.crearFranquiciaUseCase = crearFranquiciaUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranquiciaResponse> crear(@Valid @RequestBody FranquiciaRequest request) {
        return crearFranquiciaUseCase.execute(request);
    }
}

