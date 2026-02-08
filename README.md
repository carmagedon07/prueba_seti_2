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

- **Java 17+**
  - Lenguaje y runtime. El proyecto está configurado para Java 17 en Maven, pero es compatible con versiones superiores (por ejemplo Java 21).

- **Spring Boot (WebFlux)**
  - Framework principal para construir la API.
  - Se usa **WebFlux** para un modelo **reactivo/no bloqueante** (ideal para I/O: llamadas a BD, alta concurrencia).

- **Spring Data R2DBC**
  - Capa de data access reactiva.
  - Permite repositorios reactivos (`ReactiveCrudRepository`) y consultas con `Mono`/`Flux`.

- **Driver R2DBC MySQL (io.asyncer:r2dbc-mysql)**
  - Conector reactivo específico para MySQL.
  - A diferencia de JDBC, evita bloquear hilos en operaciones de base de datos.

- **MySQL**
  - Base de datos relacional.
  - En este proyecto se inicializa el esquema con:
    - `create-database.sql` (creación de la base `franquicia`)
    - `schema.sql` (creación de tablas/índices al iniciar la app)

- **Maven + Maven Wrapper (`mvnw` / `mvnw.cmd`)**
  - Gestión de dependencias, build y ejecución.
  - El wrapper permite compilar/ejecutar sin instalar Maven global.

- **Lombok**
  - Reduce código repetitivo (getters/setters/builders/constructores) en modelos y DTOs.

- **JUnit 5 + Mockito + Reactor Test**
  - **JUnit 5**: framework de pruebas.
  - **Mockito**: mocks/stubs para aislar los casos de uso (mock de puertos/repos).
  - **Reactor Test**: `StepVerifier` para validar flujos reactivos (`Mono`/`Flux`).

- **JaCoCo**
  - Genera métricas de cobertura de pruebas.
  - El reporte HTML se genera con `mvn clean verify` en `target/site/jacoco/index.html`.

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

Este error significa que **tu máquina rechazó la conexión TCP** a `localhost:8082`. No es un error de JSON ni de Postman: es que **no hay nada escuchando** en ese puerto (o no está publicado).

Causas típicas:

- El contenedor **`my_api_app` no está corriendo** o está reiniciándose.
- El contenedor corre, pero **la API no está escuchando en 8082** (config de `server.port`).
- Estás ejecutando la API en Docker pero **no publicaste el puerto** (`ports: - "8082:8082"`).
- El contenedor levantó pero **falló al iniciar** (por ejemplo, no logra conectarse a MySQL) y se apaga.

Pasos de verificación (Docker Compose):

1. Ver estado de contenedores:
   ```powershell
   docker-compose ps
   ```
   - Debes ver `my_api_app` en estado **Up**.

2. Ver logs de la API:
   ```powershell
   docker-compose logs -f my_api_app
   ```
   - Busca un log tipo: “Started ... on port(s): 8082”, o errores de conexión a BD.

3. Verificar que el puerto esté publicado en Windows:
   ```powershell
   netstat -ano | findstr :8082
   ```
   - Debe aparecer `LISTENING`. Si no aparece, la API **no está exponiendo** el puerto.

4. Probar desde el navegador:
   - Abre `http://localhost:8082` o ejecuta un GET a cualquier endpoint.

> Nota: si estás usando Docker Compose, `localhost` solo aplica para el **host**. Dentro de contenedores, `localhost` apunta al contenedor mismo. Por eso, la API debe conectarse a MySQL usando el **nombre del servicio** (por ejemplo `my_api_mysql`).

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

## 🧾 ¿Por qué MySQL y Docker? (y alternativas)

Este proyecto podía usar distintos sistemas de persistencia como **Redis, MySQL, MongoDB o DynamoDB**. Se eligió **MySQL** (relacional) y contenedorización con **Docker** por estas razones:

### Por qué MySQL (modelo relacional)

- **Modelo de datos naturalmente relacional**: una **Franquicia** tiene muchas **Sucursales** y una **Sucursal** tiene muchos **Productos**. Esto encaja perfecto con:
  - llaves foráneas,
  - consistencia referencial,
  - consultas simples y claras.

- **Consistencia (ACID)**: para operaciones como actualizar stock o renombrar entidades, un motor relacional aporta garantías de consistencia y transacciones.

- **Consultas agregadas/ordenamiento**: el caso de uso “producto con mayor stock por sucursal” se resuelve eficientemente con SQL (ORDER BY + LIMIT) y/o índices.

- **Compatibilidad con R2DBC**: al usar WebFlux, es importante mantener el stack no-bloqueante; con R2DBC + MySQL se conserva el enfoque reactivo hasta la base de datos.

### Por qué Docker (despliegue reproducible)

- **Entorno local consistente**: evita “en mi máquina funciona” al fijar versión de MySQL e inicialización.
- **Red interna por DNS de servicios**: la API se conecta usando el hostname del servicio (por ejemplo `my_api_mysql`) dentro de la red del Compose.
- **Onboarding rápido**: con un solo `docker-compose up` se levanta DB + API.

### Alternativas y cuándo usarlas

- **Redis**
  - Excelente para **caché**, sesiones, colas simples o rate limiting.
  - No es la mejor opción como **fuente de verdad** (source of truth) para relaciones y consistencia fuerte.

- **MongoDB (documental)**
  - Útil cuando el modelo es más flexible y orientado a documentos.
  - En este dominio (relaciones 1:N claras, reglas de integridad) el modelo relacional es más directo.

- **DynamoDB (NoSQL administrado en AWS)**
  - Muy útil en AWS para alta escala, baja latencia y modelo key-value/document.
  - Implica diseño por patrones de acceso, y para dev local generalmente se usa DynamoDB Local.
  - Para una prueba técnica local, MySQL + Docker reduce complejidad operativa.

