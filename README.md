# API REST Reactiva - Gestión de Franquicias

## 📋 Descripción

API RESTful reactiva para gestionar franquicias, sucursales y productos utilizando **Arquitectura Hexagonal** (Ports & Adapters), **Spring Boot WebFlux** y **R2DBC** para MySQL.

## 🏗️ Arquitectura Hexagonal

El proyecto sigue la arquitectura hexagonal con las siguientes capas:

```
├── domain/                          # Capa de Dominio (Núcleo)
│   ├── model/                       # Entidades de negocio
│   │   ├── Franquicia.java
│   │   ├── Sucursal.java
│   │   └── Producto.java
│   ├── dto/                         # DTOs de entrada/salida
│   │   ├── FranquiciaRequest/Response
│   │   ├── SucursalRequest/Response
│   │   ├── ProductoRequest/Response
│   │   ├── ActualizarStockRequest
│   │   └── ProductoMaxStockResponse
│   ├── port/                        # Puertos (Interfaces)
│   │   └── out/                     # Puertos de salida
│   │       ├── FranquiciaRepositoryPort
│   │       ├── SucursalRepositoryPort
│   │       └── ProductoRepositoryPort
│   └── exception/                   # Excepciones de dominio
│       ├── ResourceNotFoundException
│       └── BusinessValidationException
│
├── application/                     # Capa de Aplicación
│   └── usecase/                     # Casos de uso
│       ├── CrearFranquiciaUseCase
│       ├── AgregarSucursalUseCase
│       ├── AgregarProductoUseCase
│       ├── EliminarProductoUseCase
│       ├── ModificarStockProductoUseCase
│       └── ObtenerProductoMaxStockPorSucursalUseCase
│
└── infrastructure/                  # Capa de Infraestructura (Adaptadores)
    ├── adapter/
    │   ├── in/                      # Adaptadores de entrada
    │   │   └── rest/                # Controllers REST
    │   │       ├── FranquiciaController
    │   │       ├── SucursalController
    │   │       ├── ProductoController
    │   │       └── exception/       # Manejo global de errores
    │   │           ├── ErrorResponse
    │   │           └── GlobalExceptionHandler
    │   └── out/                     # Adaptadores de salida
    │       └── persistence/         # Persistencia R2DBC
    │           ├── entity/          # Entidades R2DBC
    │           ├── repository/      # Repositorios Spring Data
    │           ├── mapper/          # Mappers Entity <-> Domain
    │           └── *RepositoryAdapter # Implementación de puertos
    └── config/                      # Configuración
        └── R2dbcConfig
```

## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 4.0.2**
- **Spring WebFlux** (Programación reactiva)
- **R2DBC** (Reactive Relational Database Connectivity)
- **MySQL** (Base de datos)
- **Lombok** (Reducción de boilerplate)
- **SLF4J + Logback** (Logging)
- **Jakarta Validation** (Validación de DTOs)
- **Maven** (Gestión de dependencias)

## 📡 Endpoints RESTful

### 1. Crear Franquicia
```http
POST /api/v1/franquicias
Content-Type: application/json

{
  "nombre": "Franquicia ABC"
}
```

**Respuesta (201 CREATED):**
```json
{
  "id": 1,
  "nombre": "Franquicia ABC"
}
```

### 2. Agregar Sucursal
```http
POST /api/v1/sucursales
Content-Type: application/json

{
  "nombre": "Sucursal Centro",
  "franquiciaId": 1
}
```

**Respuesta (201 CREATED):**
```json
{
  "id": 1,
  "nombre": "Sucursal Centro",
  "franquiciaId": 1
}
```

### 3. Agregar Producto
```http
POST /api/v1/productos
Content-Type: application/json

{
  "nombre": "Producto A",
  "stock": 100,
  "sucursalId": 1
}
```

**Respuesta (201 CREATED):**
```json
{
  "id": 1,
  "nombre": "Producto A",
  "stock": 100,
  "sucursalId": 1
}
```

### 4. Eliminar Producto
```http
DELETE /api/v1/productos/{id}
```

**Respuesta (204 NO CONTENT)**

### 5. Modificar Stock
```http
PATCH /api/v1/productos/{id}/stock
Content-Type: application/json

{
  "nuevoStock": 150
}
```

**Respuesta (200 OK):**
```json
{
  "id": 1,
  "nombre": "Producto A",
  "stock": 150,
  "sucursalId": 1
}
```

### 6. Productos con Mayor Stock por Franquicia
```http
GET /api/v1/productos/max-stock/franquicia/{franquiciaId}
```

**Respuesta (200 OK):**
```json
[
  {
    "productoId": 1,
    "nombreProducto": "Producto A",
    "stock": 150,
    "sucursalId": 1,
    "nombreSucursal": "Sucursal Centro"
  },
  {
    "productoId": 5,
    "nombreProducto": "Producto B",
    "stock": 200,
    "sucursalId": 2,
    "nombreSucursal": "Sucursal Norte"
  }
]
```

## 🔄 Flujo Reactivo

El código utiliza **operadores reactivos** de Project Reactor:

- **`map`**: Transformación de datos
- **`flatMap`**: Operaciones asíncronas con aplanamiento
- **`switchIfEmpty`**: Manejo de valores vacíos
- **`doOnNext`**: Efectos secundarios (logging)
- **`doOnError`**: Manejo de errores
- **`doOnSuccess`**: Acciones al completar exitosamente
- **`doOnComplete`**: Acciones al terminar el flujo

### Ejemplo de flujo reactivo:
```java
return sucursalRepository.existsById(request.getSucursalId())
    .flatMap(exists -> {
        if (!exists) {
            return Mono.error(new ResourceNotFoundException("Sucursal", request.getSucursalId()));
        }
        return Mono.just(exists);
    })
    .flatMap(valid -> Mono.just(request)
        .map(req -> Producto.builder()
            .nombre(req.getNombre())
            .stock(req.getStock())
            .sucursalId(req.getSucursalId())
            .build()))
    .flatMap(productoRepository::save)
    .map(this::toResponse);
```

## ✅ Validaciones

- Validación de campos con `@Valid` y Jakarta Validation
- Stock no puede ser negativo (`@Min(0)`)
- Verificación de existencia de entidades relacionadas antes de operaciones
- Manejo de errores con excepciones personalizadas

## 📊 Modelo de Datos

```sql
franquicias
├── id (PK)
└── nombre

sucursales
├── id (PK)
├── nombre
└── franquicia_id (FK → franquicias.id)

productos
├── id (PK)
├── nombre
├── stock
└── sucursal_id (FK → sucursales.id)
```

## 🔧 Configuración

### application.properties
```properties
spring.application.name=api_test
spring.r2dbc.url=r2dbc:mysql://localhost:3306/franquicia
spring.r2dbc.username=root
spring.r2dbc.password=1234
server.port=8080
```

### Base de datos MySQL
1. Crear la base de datos:
```sql
CREATE DATABASE franquicia;
```

2. El esquema se crea automáticamente al iniciar la aplicación

## 🏃 Ejecución

### Compilar el proyecto
```bash
mvnw clean install
```

### Ejecutar la aplicación
```bash
mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 🧪 Señales Reactivas

El código implementa correctamente las señales reactivas:

- **`onNext`**: Emisión de elementos (ej: productos encontrados)
- **`onError`**: Propagación de errores con logging
- **`onComplete`**: Finalización del flujo reactivo

## 📝 Logging

Se utiliza **SLF4J** con Logback para logging en múltiples niveles:

- `INFO`: Operaciones principales
- `DEBUG`: Detalles de flujo y transformaciones
- `WARN`: Recursos no encontrados
- `ERROR`: Errores de sistema

Ejemplo:
```java
log.info("Iniciando creación de producto: {}", request.getNombre());
log.debug("Producto mapeado: {}", producto);
log.warn("Producto con ID {} no encontrado", productoId);
log.error("Error al crear producto: {}", error.getMessage());
```

## 🎯 Decisiones de Diseño

### ¿Por qué R2DBC en lugar de JPA?
- **R2DBC** es completamente reactivo y no bloqueante
- JPA/Hibernate **NO** son compatibles con programación reactiva
- R2DBC permite aprovechar al máximo WebFlux para alta concurrencia

### ¿Por qué Arquitectura Hexagonal?
- **Separación de responsabilidades**: Dominio independiente de infraestructura
- **Testabilidad**: Fácil mockear dependencias
- **Flexibilidad**: Cambiar tecnologías sin afectar la lógica de negocio

### ¿Por qué DTOs separados?
- **Seguridad**: No exponer entidades de dominio directamente
- **Flexibilidad**: El contrato API puede diferir del modelo interno
- **Validación**: Validaciones específicas por endpoint

## 🛡️ Manejo de Errores

Respuestas de error estandarizadas:

```json
{
  "timestamp": "2026-02-08T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Franquicia con ID 999 no encontrado",
  "path": "/api/v1/sucursales"
}
```

## 📚 Referencias

- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [R2DBC Documentation](https://r2dbc.io/)
- [Project Reactor](https://projectreactor.io/)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)

---

**Desarrollado con ❤️ usando Arquitectura Hexagonal y Programación Reactiva**


