package com.prueba.seti.api_test.infrastructure.adapter.in.rest;

import com.prueba.seti.api_test.application.usecase.AgregarSucursalUseCase;
import com.prueba.seti.api_test.domain.dto.SucursalRequest;
import com.prueba.seti.api_test.domain.dto.SucursalResponse;
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
@RequestMapping("/api/v1/sucursales")
public class SucursalController {

    private final AgregarSucursalUseCase agregarSucursalUseCase;

    public SucursalController(AgregarSucursalUseCase agregarSucursalUseCase) {
        this.agregarSucursalUseCase = agregarSucursalUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SucursalResponse> agregar(@Valid @RequestBody SucursalRequest request) {
        return agregarSucursalUseCase.execute(request);
    }
}

