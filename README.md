# README

## 📌 Descripción

API REST reactiva para gestionar **franquicias**, **sucursales** y **productos**, construida con **Spring Boot WebFlux** y persistencia reactiva con **R2DBC (MySQL)**. El código sigue **Arquitectura Hexagonal (Ports & Adapters)** para mantener el dominio desacoplado de frameworks y detalles de infraestructura.

---

## 🧭 Contenido

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura y consideraciones de diseño](#-arquitectura-y-consideraciones-de-diseño)
- [Requisitos](#-requisitos)
- [Configuración](#-configuración)
- [Ejecución en local](#-ejecución-en-local)
  - [Opción A: Docker Compose (recomendado)](#opción-a-docker-compose-recomendado)
  - [Opción B: Local (Java + MySQL)](#opción-b-local-java--mysql)
- [Endpoints](#-endpoints)
- [Pruebas y cobertura](#-pruebas-y-cobertura)
- [Solución de problemas](#-solución-de-problemas)

---

## ✅ Características

- CRUD básico para Franquicias / Sucursales / Productos.
- Modificación parcial (PATCH) para **stock** y **nombre**.
- Consulta: **producto con mayor stock por sucursal** para una franquicia.
- Manejo de errores consistente (404/400/500).
- Validación con Jakarta Validation.

---

## 🚀 Tecnologías

- Java 17+
- Spring Boot (WebFlux)
- Spring Data R2DBC
- MySQL
- Maven
- Lombok
- JUnit 5, Mockito, Reactor Test
- JaCoCo

---

## 🏗️ Arquitectura y consideraciones de diseño

### Estructura del proyecto (detalle)

> Vista orientativa (los nombres pueden variar ligeramente según el paquete), pensada para ubicar rápidamente dónde vive cada responsabilidad.

```text
src/
├─ main/
│  ├─ java/
│  │  └─ com/prueba/seti/api_test/
│  │     ├─ ApiTestApplication.java
│  │     │
│  │     ├─ domain/                              # Núcleo: reglas/contratos (sin infraestructura)
│  │     │  ├─ model/                            # Entidades de negocio (Franquicia, Sucursal, Producto)
│  │     │  ├─ dto/                              # DTOs de entrada/salida (Request/Response)
│  │     │  ├─ exception/                        # Excepciones de dominio (404/validación negocio)
│  │     │  └─ port/
│  │     │     └─ out/                           # Puertos de salida (interfaces) hacia persistencia
│  │     │
│  │     ├─ application/
│  │     │  └─ usecase/                          # Casos de uso (orquestan dominio + puertos)
│  │     │
│  │     └─ infrastructure/
│  │        ├─ adapter/
│  │        │  ├─ in/
│  │        │  │  └─ rest/                        # Controllers WebFlux + validación @Valid
│  │        │  │     └─ exception/                # DTO/Advice para errores HTTP (GlobalExceptionHandler)
│  │        │  └─ out/
│  │        │     └─ persistence/                 # Implementación de puertos (MySQL/R2DBC)
│  │        │        ├─ entity/                   # Entidades R2DBC (@Table)
│  │        │        ├─ repository/               # Repos Spring Data R2DBC (ReactiveCrudRepository)
│  │        │        ├─ mapper/                   # Mappers Entity <-> Domain
│  │        │        └─ *RepositoryAdapter.java   # Adapters que implementan los ports
│  │        └─ config/                            # Configuración técnica (R2dbcConfig, init scripts)
│  │
│  └─ resources/
│     ├─ application.properties                   # Config (puerto, R2DBC, init SQL)
│     └─ schema.sql                               # DDL (tablas/índices) ejecutado al iniciar
│
└─ test/
   └─ java/
      └─ com/prueba/seti/api_test/                # Pruebas unitarias (use cases) y de controlador (WebFluxTest)
```

### Arquitectura Hexagonal (Ports & Adapters)

```
┌─────────────────────────────┐
│         REST (in)           │  Controllers
└──────────────┬──────────────┘
               │
┌──────────────▼──────────────┐
│        Application          │  Use cases (orquestación)
└──────────────┬──────────────┘
               │  Ports (interfaces)
┌──────────────▼──────────────┐
│           Domain            │  Model + reglas + excepciones
└──────────────┬──────────────┘
               │
┌──────────────▼──────────────┐
│      Persistence (out)      │  Adapters + R2DBC repositories
└─────────────────────────────┘
```

**Por qué esta arquitectura:**
- Facilita pruebas (casos de uso se testean mockeando puertos).
- Permite cambiar la persistencia (MySQL/R2DBC) sin tocar el dominio.
- Mantiene los controladores delgados: solo validan/transportan datos.

### Por qué WebFlux + R2DBC
- WebFlux es no bloqueante; R2DBC permite mantener el flujo reactivo hasta la BD.
- Evita JPA/Hibernate por su naturaleza principalmente bloqueante.

### Inicialización de BD
- `create-database.sql`: crea el **schema/base de datos** (ej. `franquicia`).
- `schema.sql`: crea las **tablas** al iniciar la app (por `spring.sql.init.*`).

---

## 📦 Requisitos

- Java 17+ (se recomienda Java 21 si lo tienes instalado)
- Docker Desktop (recomendado para ejecución con Compose)
- Maven (o usar `mvnw` / `mvnw.cmd`)

---

## 🔧 Configuración

La app usa variables de entorno (con valores por defecto):

```text
MY_API_DB_HOST=localhost
MY_API_DB_PORT=3306
MY_API_DB_NAME=franquicia
MY_API_DB_USER=root
MY_API_DB_PASSWORD=1234
MY_API_PORT=8082
```

En `src/main/resources/application.properties` se usan así:

```properties
spring.r2dbc.url=r2dbc:mysql://${MY_API_DB_HOST:localhost}:${MY_API_DB_PORT:3306}/${MY_API_DB_NAME:franquicia}?useSSL=false
spring.r2dbc.username=${MY_API_DB_USER:root}
spring.r2dbc.password=${MY_API_DB_PASSWORD:1234}

spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql

server.port=${MY_API_PORT:8082}
```

---

## ▶️ Ejecución en local

### Opción A: Docker Compose (recomendado)

Levanta MySQL + API en una red Docker (Compose crea la red automáticamente) y expone la API en **8082**.

```powershell
docker-compose up --build -d
```

Ver logs:

```powershell
docker-compose logs -f my_api_app
```

> Si todo está ok, deberías ver que la app inicia y queda escuchando en el puerto 8082.

### Opción B: Local (Java + MySQL)

1) Levanta MySQL localmente y crea la base de datos:

```sql
CREATE DATABASE franquicia;
```

O ejecuta el script:

```powershell
mysql -u root -p < create-database.sql
```

2) Ejecuta la app:

```powershell
.\mvnw.cmd spring-boot:run
```

---

## 📡 Endpoints

Base URL (local):

- `http://localhost:8082`

### Postman (colección de pruebas)

En el repositorio se incluye un **archivo JSON** con una colección lista para importar en Postman y ejecutar las pruebas manuales de los endpoints:

- `setic_api_collection.postman_collection.json`

**Importar en Postman:**

1. Postman → **Import**
2. Selecciona el archivo `setic_api_collection.postman_collection.json`
3. Ejecuta las requests en el orden sugerido (crear franquicia → crear sucursal → crear producto, etc.)

### Franquicias

- **Crear franquicia**
  - `POST /api/v1/franquicias`
  - Body:
    ```json
    { "nombre": "Franquicia ABC" }
    ```

- **Actualizar nombre**
  - `PATCH /api/v1/franquicias/{id}`
  - Body:
    ```json
    { "nombre": "Franquicia Actualizada" }
    ```

### Sucursales

- **Agregar sucursal**
  - `POST /api/v1/sucursales`
  - Body:
    ```json
    { "nombre": "Sucursal Centro", "franquiciaId": 1 }
    ```

- **Actualizar nombre**
  - `PATCH /api/v1/sucursales/{id}`
  - Body:
    ```json
    { "nombre": "Sucursal Actualizada" }
    ```

### Productos

- **Agregar producto**
  - `POST /api/v1/productos`
  - Body:
    ```json
    { "nombre": "Producto A", "stock": 100, "sucursalId": 1 }
    ```

- **Eliminar producto**
  - `DELETE /api/v1/productos/{id}`

- **Modificar stock**
  - `PATCH /api/v1/productos/{id}/stock`
  - Body:
    ```json
    { "nuevoStock": 150 }
    ```

- **Actualizar nombre**
  - `PATCH /api/v1/productos/{id}`
  - Body:
    ```json
    { "nombre": "Producto Actualizado" }
    ```

- **Producto con mayor stock por sucursal (por franquicia)**
  - `GET /api/v1/productos/max-stock/franquicia/{franquiciaId}`

---

## 🧪 Pruebas y cobertura

Ejecutar pruebas + cobertura:

```powershell
mvn clean verify
```

Reporte JaCoCo:

- `target/site/jacoco/index.html`

---

## 🛠️ Solución de problemas

### 1) Postman: `ECONNREFUSED 127.0.0.1:8082`

- Asegúrate de que el contenedor `my_api_app` esté **running**.
- Revisa logs:
  ```powershell
  docker-compose logs -f my_api_app
  ```

### 2) Error de DB/host (ej. `UnknownHostException my_api_mysql`)

- La app debe apuntar al **nombre del servicio** dentro de la red de Docker Compose.
- Si ejecutas todo con Compose, el hostname correcto suele ser el nombre del servicio (ej. `my_api_mysql`).

### 3) Re-crear todo desde cero

```powershell
docker-compose down -v
docker-compose up --build -d
```

---

## 🗄️ Estructura de base de datos (ERD)

La base de datos se compone de 3 tablas principales con relaciones 1:N:

- **Una franquicia** tiene **muchas sucursales**
- **Una sucursal** tiene **muchos productos**

```text
+-------------------+          +-------------------+          +-------------------+
|    franquicias    |          |     sucursales    |          |     productos     |
+-------------------+          +-------------------+          +-------------------+
| PK id  BIGINT     |<---+     | PK id BIGINT      |<---+     | PK id BIGINT      |
| nombre VARCHAR    |    |     | nombre VARCHAR    |    |     | nombre VARCHAR    |
+-------------------+    |     | FK franquicia_id  |    |     | stock INT         |
                         |     +-------------------+    |     | FK sucursal_id    |
                         |                              |     +-------------------+
                         +------------------------------+---------------------------
                                   1:N                            1:N

FK sucursales.franquicia_id -> franquicias.id
FK productos.sucursal_id    -> sucursales.id
```

### Consideraciones

- Las FKs están configuradas con **`ON DELETE CASCADE`** (al eliminar una franquicia se eliminan sus sucursales; al eliminar una sucursal se eliminan sus productos).
- Índices recomendados/creados (según `schema.sql`):
  - `sucursales(franquicia_id)`
  - `productos(sucursal_id)`
  - `productos(stock)`

---
