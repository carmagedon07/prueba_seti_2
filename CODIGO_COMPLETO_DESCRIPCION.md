# DESCRIPCIÓN COMPLETA DEL CÓDIGO - API REST REACTIVA FRANQUICIAS

Este documento describe la implementación completa de la API siguiendo Arquitectura Hexagonal con Spring Boot WebFlux y R2DBC.

## ESTRUCTURA DEL PROYECTO

```
src/main/java/com/prueba/seti/api_test/
├── ApiTestApplication.java (CLASE PRINCIPAL)
├── domain/ (CAPA DE DOMINIO)
│   ├── model/ (ENTIDADES DE NEGOCIO)
│   │   ├── Franquicia.java
│   │   ├── Sucursal.java
│   │   └── Producto.java
│   ├── dto/ (DATA TRANSFER OBJECTS)
│   │   ├── FranquiciaRequest.java
│   │   ├── FranquiciaResponse.java
│   │   ├── SucursalRequest.java
│   │   ├── SucursalResponse.java
│   │   ├── ProductoRequest.java
│   │   ├── ProductoResponse.java
│   │   ├── ActualizarStockRequest.java
│   │   └── ProductoMaxStockResponse.java
│   ├── port/out/ (PUERTOS DE SALIDA)
│   │   ├── FranquiciaRepositoryPort.java
│   │   ├── SucursalRepositoryPort.java
│   │   └── ProductoRepositoryPort.java
│   └── exception/ (EXCEPCIONES DE DOMINIO)
│       ├── ResourceNotFoundException.java
│       └── BusinessValidationException.java
├── application/usecase/ (CAPA DE APLICACIÓN - CASOS DE USO)
│   ├── CrearFranquiciaUseCase.java
│   ├── AgregarSucursalUseCase.java
│   ├── AgregarProductoUseCase.java
│   ├── EliminarProductoUseCase.java
│   ├── ModificarStockProductoUseCase.java
│   └── ObtenerProductoMaxStockPorSucursalUseCase.java
└── infrastructure/ (CAPA DE INFRAESTRUCTURA)
    ├── config/
    │   └── R2dbcConfig.java
    ├── adapter/in/rest/ (CONTROLLERS REST)
    │   ├── FranquiciaController.java
    │   ├── SucursalController.java
    │   ├── ProductoController.java
    │   └── exception/
    │       ├── ErrorResponse.java
    │       └── GlobalExceptionHandler.java
    └── adapter/out/persistence/ (ADAPTADORES DE PERSISTENCIA)
        ├── entity/
        │   ├── FranquiciaEntity.java
        │   ├── SucursalEntity.java
        │   └── ProductoEntity.java
        ├── repository/
        │   ├── FranquiciaR2dbcRepository.java
        │   ├── SucursalR2dbcRepository.java
        │   └── ProductoR2dbcRepository.java
        ├── mapper/
        │   ├── FranquiciaMapper.java
        │   ├── SucursalMapper.java
        │   └── ProductoMapper.java
        └── (Adaptadores)
            ├── FranquiciaRepositoryAdapter.java
            ├── SucursalRepositoryAdapter.java
            └── ProductoRepositoryAdapter.java
```

---

## 1. CAPA DE DOMINIO - ENTIDADES DE NEGOCIO

### 1.1 Franquicia.java
**Propósito**: Entidad de dominio que representa una franquicia
**Ubicación**: `domain/model/Franquicia.java`

```java
package com.prueba.seti.api_test.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Franquicia {
    private Long id;
    private String nombre;
}
```

**Características**:
- Usa Lombok para reducir boilerplate (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
- Entidad POJO simple sin dependencias de frameworks
- Representa el concepto de negocio puro

### 1.2 Sucursal.java
**Propósito**: Entidad que representa una sucursal perteneciente a una franquicia
**Ubicación**: `domain/model/Sucursal.java`

```java
package com.prueba.seti.api_test.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {
    private Long id;
    private String nombre;
    private Long franquiciaId;
}
```

**Relación**: Cada sucursal pertenece a una franquicia (franquiciaId)

### 1.3 Producto.java
**Propósito**: Entidad que representa un producto ofertado en una sucursal
**Ubicación**: `domain/model/Producto.java`

```java
package com.prueba.seti.api_test.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId;
}
```

**Relación**: Cada producto pertenece a una sucursal (sucursalId) y tiene un stock

---

## 2. CAPA DE DOMINIO - DTOs (REQUEST/RESPONSE)

### 2.1 FranquiciaRequest.java
**Propósito**: DTO para crear una franquicia
**Validaciones**: nombre es obligatorio (@NotBlank)

```java
package com.prueba.seti.api_test.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranquiciaRequest {
    @NotBlank(message = "El nombre de la franquicia es obligatorio")
    private String nombre;
}
```

### 2.2 FranquiciaResponse.java
**Propósito**: DTO de respuesta para franquicia

```java
package com.prueba.seti.api_test.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranquiciaResponse {
    private Long id;
    private String nombre;
}
```

### 2.3 SucursalRequest.java
**Propósito**: DTO para agregar sucursal a franquicia
**Validaciones**: 
- nombre obligatorio
- franquiciaId obligatorio

```java
package com.prueba.seti.api_test.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalRequest {
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombre;
    
    @NotNull(message = "El ID de la franquicia es obligatorio")
    private Long franquiciaId;
}
```

### 2.4 SucursalResponse.java
```java
package com.prueba.seti.api_test.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalResponse {
    private Long id;
    private String nombre;
    private Long franquiciaId;
}
```

### 2.5 ProductoRequest.java
**Validaciones**:
- nombre obligatorio
- stock >= 0 (no negativo)
- sucursalId obligatorio

```java
package com.prueba.seti.api_test.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequest {
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    @NotNull(message = "El ID de la sucursal es obligatorio")
    private Long sucursalId;
}
```

### 2.6 ProductoResponse.java
```java
package com.prueba.seti.api_test.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {
    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId;
}
```

### 2.7 ActualizarStockRequest.java
**Validación**: stock >= 0

```java
package com.prueba.seti.api_test.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarStockRequest {
    @NotNull(message = "El nuevo stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer nuevoStock;
}
```

### 2.8 ProductoMaxStockResponse.java
**Propósito**: Retorna producto con mayor stock indicando la sucursal

```java
package com.prueba.seti.api_test.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMaxStockResponse {
    private Long productoId;
    private String nombreProducto;
    private Integer stock;
    private Long sucursalId;
    private String nombreSucursal;
}
```

---

## 3. CAPA DE DOMINIO - EXCEPCIONES

### 3.1 ResourceNotFoundException.java
**Propósito**: Excepción cuando no se encuentra un recurso (404)

```java
package com.prueba.seti.api_test.domain.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s con ID %d no encontrado", resourceName, id));
    }
}
```

### 3.2 BusinessValidationException.java
**Propósito**: Excepción de validación de negocio (400)

```java
package com.prueba.seti.api_test.domain.exception;

public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
    }
}
```

---

## 4. CAPA DE DOMINIO - PUERTOS (INTERFACES)

Los puertos definen contratos sin implementación. La implementación estará en la capa de infraestructura.

### 4.1 FranquiciaRepositoryPort.java
```java
package com.prueba.seti.api_test.domain.port.out;

import com.prueba.seti.api_test.domain.model.Franquicia;
import reactor.core.publisher.Mono;

public interface FranquiciaRepositoryPort {
    Mono<Franquicia> save(Franquicia franquicia);
    Mono<Franquicia> findById(Long id);
    Mono<Boolean> existsById(Long id);
}
```

**Operadores reactivos**: Mono (0 o 1 elemento)

### 4.2 SucursalRepositoryPort.java
```java
package com.prueba.seti.api_test.domain.port.out;

import com.prueba.seti.api_test.domain.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalRepositoryPort {
    Mono<Sucursal> save(Sucursal sucursal);
    Mono<Sucursal> findById(Long id);
    Mono<Boolean> existsById(Long id);
    Flux<Sucursal> findByFranquiciaId(Long franquiciaId);
}
```

**Operadores reactivos**: Flux (0 a N elementos), Mono

### 4.3 ProductoRepositoryPort.java
```java
package com.prueba.seti.api_test.domain.port.out;

import com.prueba.seti.api_test.domain.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoRepositoryPort {
    Mono<Producto> save(Producto producto);
    Mono<Producto> findById(Long id);
    Mono<Boolean> existsById(Long id);
    Mono<Void> deleteById(Long id);
    Flux<Producto> findBySucursalId(Long sucursalId);
    Mono<Producto> findTopBySucursalIdOrderByStockDesc(Long sucursalId);
}
```

---

## 5. CAPA DE APLICACIÓN - CASOS DE USO

Los casos de uso contienen la lógica de negocio y orquestan las operaciones.

### 5.1 CrearFranquiciaUseCase.java

**FLUJO REACTIVO**:
1. Recibe FranquiciaRequest
2. Transforma a entidad Franquicia (map)
3. Persiste (flatMap)
4. Transforma a FranquiciaResponse (map)

**OPERADORES USADOS**: map, flatMap, doOnNext, doOnError, doOnSuccess

**SEÑALES**: onNext (emisión), onError (errores), onComplete (finalización)

**LOGGING**: INFO, DEBUG, ERROR con SLF4J

```java
package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.FranquiciaRequest;
import com.prueba.seti.api_test.domain.dto.FranquiciaResponse;
import com.prueba.seti.api_test.domain.model.Franquicia;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrearFranquiciaUseCase {
    
    private final FranquiciaRepositoryPort franquiciaRepository;
    
    public Mono<FranquiciaResponse> execute(FranquiciaRequest request) {
        log.info("Iniciando creacion de franquicia: {}", request.getNombre());
        
        return Mono.just(request)
                .map(req -> Franquicia.builder()
                        .nombre(req.getNombre())
                        .build())
                .doOnNext(franquicia -> log.debug("Franquicia mapeada: {}", franquicia))
                .flatMap(franquiciaRepository::save)
                .doOnNext(saved -> log.info("Franquicia guardada con ID: {}", saved.getId()))
                .map(this::toResponse)
                .doOnError(error -> log.error("Error al crear franquicia: {}", error.getMessage()))
                .doOnSuccess(response -> log.info("Franquicia creada exitosamente: {}", response));
    }
    
    private FranquiciaResponse toResponse(Franquicia franquicia) {
        return FranquiciaResponse.builder()
                .id(franquicia.getId())
                .nombre(franquicia.getNombre())
                .build();
    }
}
```

### 5.2 AgregarSucursalUseCase.java

**FLUJO REACTIVO**:
1. Valida que franquicia existe (existsById + flatMap)
2. Si no existe → Mono.error (señal onError)
3. Si existe → crea sucursal
4. Persiste y retorna

**OPERADORES**: flatMap (encadenamiento), map, doOnNext, switchIfEmpty

```java
package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.SucursalRequest;
import com.prueba.seti.api_test.domain.dto.SucursalResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Sucursal;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgregarSucursalUseCase {
    
    private final SucursalRepositoryPort sucursalRepository;
    private final FranquiciaRepositoryPort franquiciaRepository;
    
    public Mono<SucursalResponse> execute(SucursalRequest request) {
        log.info("Iniciando creacion de sucursal: {} para franquicia ID: {}", 
                request.getNombre(), request.getFranquiciaId());
        
        return franquiciaRepository.existsById(request.getFranquiciaId())
                .flatMap(exists -> {
                    if (!exists) {
                        log.warn("Franquicia con ID {} no encontrada", request.getFranquiciaId());
                        return Mono.error(new ResourceNotFoundException(
                                "Franquicia", request.getFranquiciaId()));
                    }
                    log.debug("Franquicia validada exitosamente");
                    return Mono.just(exists);
                })
                .flatMap(valid -> Mono.just(request)
                        .map(req -> Sucursal.builder()
                                .nombre(req.getNombre())
                                .franquiciaId(req.getFranquiciaId())
                                .build()))
                .doOnNext(sucursal -> log.debug("Sucursal mapeada: {}", sucursal))
                .flatMap(sucursalRepository::save)
                .doOnNext(saved -> log.info("Sucursal guardada con ID: {}", saved.getId()))
                .map(this::toResponse)
                .doOnError(error -> log.error("Error al crear sucursal: {}", error.getMessage()))
                .doOnSuccess(response -> log.info("Sucursal creada exitosamente: {}", response));
    }
    
    private SucursalResponse toResponse(Sucursal sucursal) {
        return SucursalResponse.builder()
                .id(sucursal.getId())
                .nombre(sucursal.getNombre())
                .franquiciaId(sucursal.getFranquiciaId())
                .build();
    }
}
```

### 5.3 AgregarProductoUseCase.java

Similar a AgregarSucursalUseCase pero valida que la sucursal existe.

```java
package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ProductoRequest;
import com.prueba.seti.api_test.domain.dto.ProductoResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgregarProductoUseCase {
    
    private final ProductoRepositoryPort productoRepository;
    private final SucursalRepositoryPort sucursalRepository;
    
    public Mono<ProductoResponse> execute(ProductoRequest request) {
        log.info("Iniciando creacion de producto: {} para sucursal ID: {}", 
                request.getNombre(), request.getSucursalId());
        
        return sucursalRepository.existsById(request.getSucursalId())
                .flatMap(exists -> {
                    if (!exists) {
                        log.warn("Sucursal con ID {} no encontrada", request.getSucursalId());
                        return Mono.error(new ResourceNotFoundException(
                                "Sucursal", request.getSucursalId()));
                    }
                    log.debug("Sucursal validada exitosamente");
                    return Mono.just(exists);
                })
                .flatMap(valid -> Mono.just(request)
                        .map(req -> Producto.builder()
                                .nombre(req.getNombre())
                                .stock(req.getStock())
                                .sucursalId(req.getSucursalId())
                                .build()))
                .doOnNext(producto -> log.debug("Producto mapeado: {}", producto))
                .flatMap(productoRepository::save)
                .doOnNext(saved -> log.info("Producto guardado con ID: {}", saved.getId()))
                .map(this::toResponse)
                .doOnError(error -> log.error("Error al crear producto: {}", error.getMessage()))
                .doOnSuccess(response -> log.info("Producto creado exitosamente: {}", response));
    }
    
    private ProductoResponse toResponse(Producto producto) {
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .stock(producto.getStock())
                .sucursalId(producto.getSucursalId())
                .build();
    }
}
```

### 5.4 EliminarProductoUseCase.java

**FLUJO**: Valida existencia → Elimina

```java
package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EliminarProductoUseCase {
    
    private final ProductoRepositoryPort productoRepository;
    
    public Mono<Void> execute(Long productoId) {
        log.info("Iniciando eliminacion de producto ID: {}", productoId);
        
        return productoRepository.existsById(productoId)
                .flatMap(exists -> {
                    if (!exists) {
                        log.warn("Producto con ID {} no encontrado", productoId);
                        return Mono.error(new ResourceNotFoundException("Producto", productoId));
                    }
                    log.debug("Producto validado exitosamente");
                    return Mono.just(exists);
                })
                .flatMap(valid -> productoRepository.deleteById(productoId))
                .doOnSuccess(v -> log.info("Producto ID: {} eliminado exitosamente", productoId))
                .doOnError(error -> log.error("Error al eliminar producto: {}", error.getMessage()));
    }
}
```

### 5.5 ModificarStockProductoUseCase.java

**FLUJO**: 
1. Busca producto (findById)
2. Si no existe → switchIfEmpty + error
3. Actualiza stock (map)
4. Persiste (flatMap)

**OPERADOR CLAVE**: switchIfEmpty (manejo de valores vacíos)

```java
package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ActualizarStockRequest;
import com.prueba.seti.api_test.domain.dto.ProductoResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModificarStockProductoUseCase {
    
    private final ProductoRepositoryPort productoRepository;
    
    public Mono<ProductoResponse> execute(Long productoId, ActualizarStockRequest request) {
        log.info("Iniciando modificacion de stock para producto ID: {} - Nuevo stock: {}", 
                productoId, request.getNuevoStock());
        
        return productoRepository.findById(productoId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Producto con ID {} no encontrado", productoId);
                    return Mono.error(new ResourceNotFoundException("Producto", productoId));
                }))
                .doOnNext(producto -> log.debug("Producto encontrado: {} - Stock actual: {}", 
                        producto.getNombre(), producto.getStock()))
                .map(producto -> {
                    producto.setStock(request.getNuevoStock());
                    return producto;
                })
                .doOnNext(producto -> log.debug("Stock actualizado a: {}", producto.getStock()))
                .flatMap(productoRepository::save)
                .doOnNext(saved -> log.info("Stock actualizado exitosamente para producto ID: {}", saved.getId()))
                .map(this::toResponse)
                .doOnError(error -> log.error("Error al modificar stock: {}", error.getMessage()))
                .doOnSuccess(response -> log.info("Stock modificado exitosamente: {}", response));
    }
    
    private ProductoResponse toResponse(Producto producto) {
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .stock(producto.getStock())
                .sucursalId(producto.getSucursalId())
                .build();
    }
}
```

### 5.6 ObtenerProductoMaxStockPorSucursalUseCase.java

**CASO DE USO MÁS COMPLEJO**

**FLUJO**:
1. Valida franquicia existe
2. Obtiene todas las sucursales (flatMapMany - Mono → Flux)
3. Para cada sucursal, obtiene producto con mayor stock (flatMap)
4. Si no hay productos, continúa sin error (switchIfEmpty)

**OPERADORES**: flatMapMany, flatMap, map, switchIfEmpty, doOnComplete

```java
package com.prueba.seti.api_test.application.usecase;

import com.prueba.seti.api_test.domain.dto.ProductoMaxStockResponse;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObtenerProductoMaxStockPorSucursalUseCase {
    
    private final FranquiciaRepositoryPort franquiciaRepository;
    private final SucursalRepositoryPort sucursalRepository;
    private final ProductoRepositoryPort productoRepository;
    
    public Flux<ProductoMaxStockResponse> execute(Long franquiciaId) {
        log.info("Iniciando busqueda de productos con maximo stock para franquicia ID: {}", franquiciaId);
        
        return franquiciaRepository.existsById(franquiciaId)
                .flatMap(exists -> {
                    if (!exists) {
                        log.warn("Franquicia con ID {} no encontrada", franquiciaId);
                        return Mono.error(new ResourceNotFoundException("Franquicia", franquiciaId));
                    }
                    log.debug("Franquicia validada exitosamente");
                    return Mono.just(exists);
                })
                .flatMapMany(valid -> sucursalRepository.findByFranquiciaId(franquiciaId))
                .doOnNext(sucursal -> log.debug("Procesando sucursal: {} (ID: {})", 
                        sucursal.getNombre(), sucursal.getId()))
                .flatMap(sucursal -> 
                    productoRepository.findTopBySucursalIdOrderByStockDesc(sucursal.getId())
                            .map(producto -> ProductoMaxStockResponse.builder()
                                    .productoId(producto.getId())
                                    .nombreProducto(producto.getNombre())
                                    .stock(producto.getStock())
                                    .sucursalId(sucursal.getId())
                                    .nombreSucursal(sucursal.getNombre())
                                    .build())
                            .doOnNext(response -> log.debug("Producto con mayor stock en sucursal {}: {} (stock: {})",
                                    sucursal.getNombre(), response.getNombreProducto(), response.getStock()))
                            .switchIfEmpty(Mono.defer(() -> {
                                log.debug("No hay productos en sucursal: {}", sucursal.getNombre());
                                return Mono.empty();
                            }))
                )
                .doOnComplete(() -> log.info("Busqueda completada para franquicia ID: {}", franquiciaId))
                .doOnError(error -> log.error("Error al buscar productos con maximo stock: {}", error.getMessage()));
    }
}
```

---

## 6. CAPA DE INFRAESTRUCTURA - ENTIDADES R2DBC

Las entidades R2DBC representan las tablas en MySQL.

### 6.1 FranquiciaEntity.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("franquicias")
public class FranquiciaEntity {
    @Id
    private Long id;
    private String nombre;
}
```

### 6.2 SucursalEntity.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("sucursales")
public class SucursalEntity {
    @Id
    private Long id;
    private String nombre;
    private Long franquiciaId;
}
```

### 6.3 ProductoEntity.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("productos")
public class ProductoEntity {
    @Id
    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId;
}
```

---

## 7. CAPA DE INFRAESTRUCTURA - REPOSITORIOS R2DBC

Repositorios reactivos usando Spring Data R2DBC.

### 7.1 FranquiciaR2dbcRepository.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository;

import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.FranquiciaEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranquiciaR2dbcRepository extends ReactiveCrudRepository<FranquiciaEntity, Long> {
}
```

### 7.2 SucursalR2dbcRepository.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository;

import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SucursalR2dbcRepository extends ReactiveCrudRepository<SucursalEntity, Long> {
    Flux<SucursalEntity> findByFranquiciaId(Long franquiciaId);
}
```

### 7.3 ProductoR2dbcRepository.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository;

import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductoR2dbcRepository extends ReactiveCrudRepository<ProductoEntity, Long> {
    Flux<ProductoEntity> findBySucursalId(Long sucursalId);
    
    @Query("SELECT * FROM productos WHERE sucursal_id = :sucursalId ORDER BY stock DESC LIMIT 1")
    Mono<ProductoEntity> findTopBySucursalIdOrderByStockDesc(Long sucursalId);
}
```

**NOTA**: Usa @Query para consultas personalizadas en SQL

---

## 8. MAPPERS (ENTITY ↔ DOMAIN)

Los mappers transforman entre entidades de persistencia y entidades de dominio.

### 8.1 FranquiciaMapper.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper;

import com.prueba.seti.api_test.domain.model.Franquicia;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.FranquiciaEntity;
import org.springframework.stereotype.Component;

@Component
public class FranquiciaMapper {
    public FranquiciaEntity toEntity(Franquicia franquicia) {
        return FranquiciaEntity.builder()
                .id(franquicia.getId())
                .nombre(franquicia.getNombre())
                .build();
    }
    
    public Franquicia toDomain(FranquiciaEntity entity) {
        return Franquicia.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }
}
```

### 8.2 SucursalMapper.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper;

import com.prueba.seti.api_test.domain.model.Sucursal;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {
    public SucursalEntity toEntity(Sucursal sucursal) {
        return SucursalEntity.builder()
                .id(sucursal.getId())
                .nombre(sucursal.getNombre())
                .franquiciaId(sucursal.getFranquiciaId())
                .build();
    }
    
    public Sucursal toDomain(SucursalEntity entity) {
        return Sucursal.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .franquiciaId(entity.getFranquiciaId())
                .build();
    }
}
```

### 8.3 ProductoMapper.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper;

import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    public ProductoEntity toEntity(Producto producto) {
        return ProductoEntity.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .stock(producto.getStock())
                .sucursalId(producto.getSucursalId())
                .build();
    }
    
    public Producto toDomain(ProductoEntity entity) {
        return Producto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .stock(entity.getStock())
                .sucursalId(entity.getSucursalId())
                .build();
    }
}
```

---

## 9. ADAPTADORES DE REPOSITORIO (IMPLEMENTAN PUERTOS)

Los adaptadores implementan los puertos definidos en el dominio.

### 9.1 FranquiciaRepositoryAdapter.java

**FLUJO REACTIVO**:
- toEntity (map)
- save en BD (flatMap)
- toDomain (map)

```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence;

import com.prueba.seti.api_test.domain.model.Franquicia;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper.FranquiciaMapper;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository.FranquiciaR2dbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FranquiciaRepositoryAdapter implements FranquiciaRepositoryPort {
    
    private final FranquiciaR2dbcRepository r2dbcRepository;
    private final FranquiciaMapper mapper;
    
    @Override
    public Mono<Franquicia> save(Franquicia franquicia) {
        log.debug("Guardando franquicia en BD: {}", franquicia.getNombre());
        return Mono.just(franquicia)
                .map(mapper::toEntity)
                .flatMap(r2dbcRepository::save)
                .map(mapper::toDomain)
                .doOnNext(saved -> log.debug("Franquicia guardada: ID={}", saved.getId()));
    }
    
    @Override
    public Mono<Franquicia> findById(Long id) {
        log.debug("Buscando franquicia por ID: {}", id);
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Boolean> existsById(Long id) {
        log.debug("Verificando existencia de franquicia ID: {}", id);
        return r2dbcRepository.existsById(id);
    }
}
```

### 9.2 SucursalRepositoryAdapter.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence;

import com.prueba.seti.api_test.domain.model.Sucursal;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper.SucursalMapper;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository.SucursalR2dbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SucursalRepositoryAdapter implements SucursalRepositoryPort {
    
    private final SucursalR2dbcRepository r2dbcRepository;
    private final SucursalMapper mapper;
    
    @Override
    public Mono<Sucursal> save(Sucursal sucursal) {
        log.debug("Guardando sucursal en BD: {}", sucursal.getNombre());
        return Mono.just(sucursal)
                .map(mapper::toEntity)
                .flatMap(r2dbcRepository::save)
                .map(mapper::toDomain)
                .doOnNext(saved -> log.debug("Sucursal guardada: ID={}", saved.getId()));
    }
    
    @Override
    public Mono<Sucursal> findById(Long id) {
        log.debug("Buscando sucursal por ID: {}", id);
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Boolean> existsById(Long id) {
        log.debug("Verificando existencia de sucursal ID: {}", id);
        return r2dbcRepository.existsById(id);
    }
    
    @Override
    public Flux<Sucursal> findByFranquiciaId(Long franquiciaId) {
        log.debug("Buscando sucursales de franquicia ID: {}", franquiciaId);
        return r2dbcRepository.findByFranquiciaId(franquiciaId)
                .map(mapper::toDomain);
    }
}
```

### 9.3 ProductoRepositoryAdapter.java
```java
package com.prueba.seti.api_test.infrastructure.adapter.out.persistence;

import com.prueba.seti.api_test.domain.model.Producto;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.mapper.ProductoMapper;
import com.prueba.seti.api_test.infrastructure.adapter.out.persistence.repository.ProductoR2dbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {
    
    private final ProductoR2dbcRepository r2dbcRepository;
    private final ProductoMapper mapper;
    
    @Override
    public Mono<Producto> save(Producto producto) {
        log.debug("Guardando producto en BD: {}", producto.getNombre());
        return Mono.just(producto)
                .map(mapper::toEntity)
                .flatMap(r2dbcRepository::save)
                .map(mapper::toDomain)
                .doOnNext(saved -> log.debug("Producto guardado: ID={}", saved.getId()));
    }
    
    @Override
    public Mono<Producto> findById(Long id) {
        log.debug("Buscando producto por ID: {}", id);
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Boolean> existsById(Long id) {
        log.debug("Verificando existencia de producto ID: {}", id);
        return r2dbcRepository.existsById(id);
    }
    
    @Override
    public Mono<Void> deleteById(Long id) {
        log.debug("Eliminando producto ID: {}", id);
        return r2dbcRepository.deleteById(id);
    }
    
    @Override
    public Flux<Producto> findBySucursalId(Long sucursalId) {
        log.debug("Buscando productos de sucursal ID: {}", sucursalId);
        return r2dbcRepository.findBySucursalId(sucursalId)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Producto> findTopBySucursalIdOrderByStockDesc(Long sucursalId) {
        log.debug("Buscando producto con mayor stock en sucursal ID: {}", sucursalId);
        return r2dbcRepository.findTopBySucursalIdOrderByStockDesc(sucursalId)
                .map(mapper::toDomain);
    }
}
```

---

## 10. CONTROLLERS REST (RESTful API)

Los controllers exponen los endpoints HTTP y delegan a los casos de uso.

### 10.1 FranquiciaController.java

**ENDPOINT**: POST /api/v1/franquicias
**STATUS**: 201 CREATED
**VALIDACIÓN**: @Valid en RequestBody

```java
package com.prueba.seti.api_test.infrastructure.adapter.in.rest;

import com.prueba.seti.api_test.application.usecase.CrearFranquiciaUseCase;
import com.prueba.seti.api_test.domain.dto.FranquiciaRequest;
import com.prueba.seti.api_test.domain.dto.FranquiciaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/franquicias")
@RequiredArgsConstructor
public class FranquiciaController {
    
    private final CrearFranquiciaUseCase crearFranquiciaUseCase;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranquiciaResponse> crearFranquicia(@Valid @RequestBody FranquiciaRequest request) {
        log.info("REST: Solicitud para crear franquicia: {}", request.getNombre());
        return crearFranquiciaUseCase.execute(request)
                .doOnNext(response -> log.info("REST: Franquicia creada: {}", response))
                .doOnError(error -> log.error("REST: Error al crear franquicia", error));
    }
}
```

### 10.2 SucursalController.java

**ENDPOINT**: POST /api/v1/sucursales

```java
package com.prueba.seti.api_test.infrastructure.adapter.in.rest;

import com.prueba.seti.api_test.application.usecase.AgregarSucursalUseCase;
import com.prueba.seti.api_test.domain.dto.SucursalRequest;
import com.prueba.seti.api_test.domain.dto.SucursalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
public class SucursalController {
    
    private final AgregarSucursalUseCase agregarSucursalUseCase;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SucursalResponse> agregarSucursal(@Valid @RequestBody SucursalRequest request) {
        log.info("REST: Solicitud para crear sucursal: {} en franquicia ID: {}", 
                request.getNombre(), request.getFranquiciaId());
        return agregarSucursalUseCase.execute(request)
                .doOnNext(response -> log.info("REST: Sucursal creada: {}", response))
                .doOnError(error -> log.error("REST: Error al crear sucursal", error));
    }
}
```

### 10.3 ProductoController.java

**ENDPOINTS MÚLTIPLES**:
- POST /api/v1/productos (201)
- DELETE /api/v1/productos/{id} (204)
- PATCH /api/v1/productos/{id}/stock (200)
- GET /api/v1/productos/max-stock/franquicia/{franquiciaId} (200)

```java
package com.prueba.seti.api_test.infrastructure.adapter.in.rest;

import com.prueba.seti.api_test.application.usecase.*;
import com.prueba.seti.api_test.domain.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {
    
    private final AgregarProductoUseCase agregarProductoUseCase;
    private final EliminarProductoUseCase eliminarProductoUseCase;
    private final ModificarStockProductoUseCase modificarStockProductoUseCase;
    private final ObtenerProductoMaxStockPorSucursalUseCase obtenerProductoMaxStockUseCase;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductoResponse> agregarProducto(@Valid @RequestBody ProductoRequest request) {
        log.info("REST: Solicitud para crear producto: {} en sucursal ID: {}", 
                request.getNombre(), request.getSucursalId());
        return agregarProductoUseCase.execute(request)
                .doOnNext(response -> log.info("REST: Producto creado: {}", response))
                .doOnError(error -> log.error("REST: Error al crear producto", error));
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarProducto(@PathVariable Long id) {
        log.info("REST: Solicitud para eliminar producto ID: {}", id);
        return eliminarProductoUseCase.execute(id)
                .doOnSuccess(v -> log.info("REST: Producto eliminado ID: {}", id))
                .doOnError(error -> log.error("REST: Error al eliminar producto", error));
    }
    
    @PatchMapping("/{id}/stock")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductoResponse> modificarStock(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarStockRequest request) {
        log.info("REST: Solicitud para modificar stock de producto ID: {} - Nuevo stock: {}", 
                id, request.getNuevoStock());
        return modificarStockProductoUseCase.execute(id, request)
                .doOnNext(response -> log.info("REST: Stock modificado: {}", response))
                .doOnError(error -> log.error("REST: Error al modificar stock", error));
    }
    
    @GetMapping("/max-stock/franquicia/{franquiciaId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductoMaxStockResponse> obtenerProductosMaxStockPorFranquicia(
            @PathVariable Long franquiciaId) {
        log.info("REST: Solicitud para obtener productos con mayor stock de franquicia ID: {}", franquiciaId);
        return obtenerProductoMaxStockUseCase.execute(franquiciaId)
                .doOnNext(response -> log.debug("REST: Producto max stock: {}", response))
                .doOnComplete(() -> log.info("REST: Consulta completada para franquicia ID: {}", franquiciaId))
                .doOnError(error -> log.error("REST: Error al obtener productos con max stock", error));
    }
}
```

**VERBOS HTTP CORRECTOS (RESTful)**:
- POST: crear recursos
- DELETE: eliminar
- PATCH: actualización parcial
- GET: consultar

---

## 11. MANEJO GLOBAL DE ERRORES

### 11.1 ErrorResponse.java (DTO de error)
```java
package com.prueba.seti.api_test.infrastructure.adapter.in.rest.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
```

### 11.2 GlobalExceptionHandler.java

**MANEJA**:
- ResourceNotFoundException → 404
- BusinessValidationException → 400
- WebExchangeBindException (validaciones @Valid) → 400
- Exception genérica → 500

```java
package com.prueba.seti.api_test.infrastructure.adapter.in.rest.exception;

import com.prueba.seti.api_test.domain.exception.BusinessValidationException;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            ServerWebExchange exchange) {
        
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();
        
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse));
    }
    
    @ExceptionHandler(BusinessValidationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBusinessValidationException(
            BusinessValidationException ex,
            ServerWebExchange exchange) {
        
        log.warn("Error de validacion de negocio: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();
        
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse));
    }
    
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(
            WebExchangeBindException ex,
            ServerWebExchange exchange) {
        
        String errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        return ((FieldError) error).getField() + ": " + error.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining(", "));
        
        log.warn("Error de validacion: {}", errorMessages);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message(errorMessages)
                .path(exchange.getRequest().getPath().value())
                .build();
        
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse));
    }
    
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(
            Exception ex,
            ServerWebExchange exchange) {
        
        log.error("Error interno del servidor", ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Ha ocurrido un error interno. Por favor contacte al administrador.")
                .path(exchange.getRequest().getPath().value())
                .build();
        
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse));
    }
}
```

---

## 12. CONFIGURACIÓN

### 12.1 R2dbcConfig.java

**PROPÓSITO**: Inicializa el esquema de BD al arrancar

```java
package com.prueba.seti.api_test.infrastructure.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Slf4j
@Configuration
public class R2dbcConfig {
    
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        log.info("Inicializando configuracion de R2DBC");
        
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        
        initializer.setDatabasePopulator(populator);
        
        return initializer;
    }
}
```

### 12.2 ApiTestApplication.java (CLASE PRINCIPAL)

```java
package com.prueba.seti.api_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories
public class ApiTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiTestApplication.class, args);
    }
}
```

---

## 13. RECURSOS (src/main/resources)

### 13.1 application.properties

```properties
spring.application.name=api_test

# R2DBC MySQL Configuration
spring.r2dbc.url=r2dbc:mysql://localhost:3306/franquicia?useSSL=false
spring.r2dbc.username=root
spring.r2dbc.password=1234

# Logging Configuration
logging.level.root=INFO
logging.level.com.prueba.seti.api_test=DEBUG
logging.level.org.springframework.r2dbc=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# Server Configuration
server.port=8080
```

### 13.2 schema.sql

```sql
CREATE TABLE IF NOT EXISTS franquicias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS sucursales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    franquicia_id BIGINT NOT NULL,
    FOREIGN KEY (franquicia_id) REFERENCES franquicias(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    sucursal_id BIGINT NOT NULL,
    FOREIGN KEY (sucursal_id) REFERENCES sucursales(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_sucursales_franquicia_id ON sucursales(franquicia_id);
CREATE INDEX IF NOT EXISTS idx_productos_sucursal_id ON productos(sucursal_id);
CREATE INDEX IF NOT EXISTS idx_productos_stock ON productos(stock);
```

---

## RESUMEN DE CONCEPTOS REACTIVOS APLICADOS

### OPERADORES REACTOR USADOS:

1. **map**: Transforma datos de forma síncrona
2. **flatMap**: Encadena operaciones asíncronas (Mono → Mono)
3. **flatMapMany**: Convierte Mono en Flux
4. **switchIfEmpty**: Maneja valores vacíos con alternativa
5. **doOnNext**: Efectos secundarios (logging) cuando hay emisión
6. **doOnError**: Logging de errores
7. **doOnSuccess**: Acciones al completar con éxito
8. **doOnComplete**: Acciones al finalizar el flujo

### SEÑALES REACTIVAS:

- **onNext**: Emisión de cada elemento (ej: producto encontrado)
- **onError**: Error en el flujo (ej: ResourceNotFoundException)
- **onComplete**: Finalización exitosa del flujo

### TIPOS REACTIVOS:

- **Mono<T>**: 0 o 1 elemento (save, findById, existsById)
- **Flux<T>**: 0 a N elementos (findAll, findByFranquiciaId)

---

## DECISIONES DE DISEÑO EXPLICADAS

### 1. ¿Por qué R2DBC en lugar de JPA/Hibernate?

**PROBLEMA**: JPA/Hibernate son BLOQUEANTES. Incompatibles con WebFlux.

**SOLUCIÓN**: R2DBC es completamente reactivo y no bloqueante.

**VENTAJAS**:
- Alta concurrencia sin bloquear threads
- Compatible con programación reactiva
- Mejor rendimiento en I/O intensivo

**DESVENTAJAS**:
- Menos maduro que JPA
- Sin lazy loading
- Relaciones deben manejarse manualmente

### 2. ¿Por qué Arquitectura Hexagonal?

**VENTAJAS**:
- **Independencia de frameworks**: El dominio no depende de Spring, R2DBC, etc.
- **Testabilidad**: Fácil crear mocks de puertos
- **Flexibilidad**: Cambiar de MySQL a PostgreSQL sin tocar el dominio
- **Mantenibilidad**: Cambios en UI/BD no afectan lógica de negocio

**CAPAS**:
- **Domain**: Lógica de negocio pura
- **Application**: Casos de uso (orquestación)
- **Infrastructure**: Detalles técnicos (BD, REST, etc.)

### 3. ¿Por qué DTOs separados de Entidades?

**SEGURIDAD**: No exponer estructura interna

**VALIDACIÓN**: Validaciones específicas por endpoint

**FLEXIBILIDAD**: API puede evolucionar independiente del modelo

### 4. Uso de Logging

**SLF4J**: Abstracción de logging

**NIVELES**:
- INFO: Operaciones principales
- DEBUG: Detalles de flujo
- WARN: Recursos no encontrados
- ERROR: Errores del sistema

---

## ENDPOINTS RESTFUL (RESUMEN)

| Método | Endpoint                                         | Función                           | Status |
|--------|--------------------------------------------------|-----------------------------------|--------|
| POST   | /api/v1/franquicias                              | Crear franquicia                  | 201    |
| POST   | /api/v1/sucursales                               | Agregar sucursal                  | 201    |
| POST   | /api/v1/productos                                | Agregar producto                  | 201    |
| DELETE | /api/v1/productos/{id}                           | Eliminar producto                 | 204    |
| PATCH  | /api/v1/productos/{id}/stock                     | Modificar stock                   | 200    |
| GET    | /api/v1/productos/max-stock/franquicia/{id}      | Productos con mayor stock         | 200    |

---

## FLUJO COMPLETO DE UNA PETICIÓN (EJEMPLO)

**REQUEST**: POST /api/v1/productos

```
1. ProductoController.agregarProducto()
   ↓
2. AgregarProductoUseCase.execute()
   ↓
3. Validar sucursal existe (SucursalRepositoryPort)
   ↓
4. SucursalRepositoryAdapter → R2DBC
   ↓
5. MySQL (SELECT)
   ↓
6. Si existe → Crear Producto
   ↓
7. ProductoRepositoryAdapter.save()
   ↓
8. MySQL (INSERT)
   ↓
9. Retornar ProductoResponse
   ↓
10. HTTP 201 CREATED
```

**SEÑALES EMITIDAS**:
- onNext: en cada paso de transformación
- onError: si sucursal no existe o error BD
- onComplete: al finalizar con éxito

---

## ESTE CÓDIGO CUMPLE CON TODOS LOS REQUISITOS:

✅ **Arquitectura Hexagonal** (Domain, Application, Infrastructure)
✅ **Spring Boot WebFlux** (Reactivo)
✅ **R2DBC MySQL** (Persistencia reactiva)
✅ **Operadores reactivos** (map, flatMap, switchIfEmpty, etc.)
✅ **Señales correctas** (onNext, onError, onComplete)
✅ **Logging** (SLF4J)
✅ **DTOs separados**
✅ **Validaciones** (@Valid, @Min, @NotBlank)
✅ **RESTful** (POST, GET, PATCH, DELETE con status correctos)
✅ **6 endpoints funcionales**
✅ **Manejo de errores global**

---

FIN DEL DOCUMENTO

