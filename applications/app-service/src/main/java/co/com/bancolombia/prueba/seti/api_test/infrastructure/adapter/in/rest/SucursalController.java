package co.com.bancolombia.prueba.seti.api_test.infrastructure.adapter.in.rest;

import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.SucursalRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.SucursalResponse;
import co.com.bancolombia.model.sucursal.Sucursal;
import co.com.bancolombia.usecase.ActualizarNombreSucursalUseCase;
import co.com.bancolombia.usecase.AgregarSucursalUseCase;
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
        return agregarSucursalUseCase.execute(request)
            .map(this::toResponse);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SucursalResponse> actualizarNombre(@PathVariable("id") Long id,
                                                   @Valid @RequestBody ActualizarNombreRequest request) {
        return actualizarNombreSucursalUseCase.execute(id, request)
            .map(this::toResponse);
    }

    private SucursalResponse toResponse(Sucursal sucursal) {
        return new SucursalResponse(sucursal.getId(), sucursal.getNombre(), sucursal.getFranquiciaId());
    }
}
