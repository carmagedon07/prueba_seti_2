package co.com.bancolombia.prueba.seti.api_test.infrastructure.adapter.in.rest;

import co.com.bancolombia.usecase.ActualizarNombreProductoUseCase;
import co.com.bancolombia.usecase.AgregarProductoUseCase;
import co.com.bancolombia.usecase.EliminarProductoUseCase;
import co.com.bancolombia.usecase.ModificarStockProductoUseCase;
import co.com.bancolombia.usecase.ObtenerProductoMaxStockPorSucursalUseCase;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarNombreRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ActualizarStockRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ProductoMaxStockResponse;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ProductoRequest;
import co.com.bancolombia.prueba.seti.api_test.domain.dto.ProductoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final AgregarProductoUseCase agregarProductoUseCase;
    private final EliminarProductoUseCase eliminarProductoUseCase;
    private final ModificarStockProductoUseCase modificarStockProductoUseCase;
    private final ObtenerProductoMaxStockPorSucursalUseCase obtenerProductoMaxStockPorSucursalUseCase;
    private final ActualizarNombreProductoUseCase actualizarNombreProductoUseCase;

    public ProductoController(AgregarProductoUseCase agregarProductoUseCase,
                              EliminarProductoUseCase eliminarProductoUseCase,
                              ModificarStockProductoUseCase modificarStockProductoUseCase,
                              ObtenerProductoMaxStockPorSucursalUseCase obtenerProductoMaxStockPorSucursalUseCase,
                              ActualizarNombreProductoUseCase actualizarNombreProductoUseCase) {
        this.agregarProductoUseCase = agregarProductoUseCase;
        this.eliminarProductoUseCase = eliminarProductoUseCase;
        this.modificarStockProductoUseCase = modificarStockProductoUseCase;
        this.obtenerProductoMaxStockPorSucursalUseCase = obtenerProductoMaxStockPorSucursalUseCase;
        this.actualizarNombreProductoUseCase = actualizarNombreProductoUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductoResponse> agregar(@Valid @RequestBody ProductoRequest request) {
        return agregarProductoUseCase.execute(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminar(@PathVariable("id") Long id) {
        return eliminarProductoUseCase.execute(id);
    }

    @PatchMapping(value = "/{id}/stock", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProductoResponse> modificarStock(@PathVariable("id") Long id,
                                                 @Valid @RequestBody ActualizarStockRequest request) {
        return modificarStockProductoUseCase.execute(id, request);
    }

    @GetMapping(value = "/max-stock/franquicia/{franquiciaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ProductoMaxStockResponse> obtenerMaxStock(@PathVariable("franquiciaId") Long franquiciaId) {
        return obtenerProductoMaxStockPorSucursalUseCase.execute(franquiciaId);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProductoResponse> actualizarNombre(@PathVariable("id") Long id,
                                                   @Valid @RequestBody ActualizarNombreRequest request) {
        return actualizarNombreProductoUseCase.execute(id, request);
    }
}

