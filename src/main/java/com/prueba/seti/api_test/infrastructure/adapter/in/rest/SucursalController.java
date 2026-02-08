package com.prueba.seti.api_test.infrastructure.adapter.in.rest;

import com.prueba.seti.api_test.application.usecase.AgregarSucursalUseCase;
import com.prueba.seti.api_test.application.usecase.ActualizarNombreSucursalUseCase;
import com.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import com.prueba.seti.api_test.domain.dto.SucursalRequest;
import com.prueba.seti.api_test.domain.dto.SucursalResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/sucursales")
public class SucursalController {

    private final AgregarSucursalUseCase agregarSucursalUseCase;
    private final ActualizarNombreSucursalUseCase actualizarNombreSucursalUseCase;

    public SucursalController(AgregarSucursalUseCase agregarSucursalUseCase,
                              ActualizarNombreSucursalUseCase actualizarNombreSucursalUseCase) {
        this.agregarSucursalUseCase = agregarSucursalUseCase;
        this.actualizarNombreSucursalUseCase = actualizarNombreSucursalUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SucursalResponse> agregar(@Valid @RequestBody SucursalRequest request) {
        return agregarSucursalUseCase.execute(request);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SucursalResponse> actualizarNombre(@PathVariable("id") Long id,
                                                   @Valid @RequestBody ActualizarNombreRequest request) {
        return actualizarNombreSucursalUseCase.execute(id, request);
    }
}
