# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyecto y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por ultimo el inicio y configuracion de la aplicacion.

Lee el articulo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## 🏗️ Arquitectura y consideraciones de diseño

### Estructura del proyecto (detalle)

> Vista orientativa (los nombres pueden variar ligeramente segun el paquete), pensada para ubicar rapidamente donde vive cada responsabilidad.

```text
Prueba_setic_2/
├─ domain/
│  ├─ model/
│  │  └─ src/main/java/
│  │     └─ co/com/bancolombia/prueba/seti/api_test/domain/
│  │        ├─ model/                        # Entidades de negocio
│  │        ├─ dto/                          # DTOs de entrada/salida
│  │        ├─ exception/                    # Excepciones de dominio
│  │        └─ port/out/                     # Puertos de salida
│  └─ usecase/
│     └─ src/main/java/
│        └─ co/com/bancolombia/usecase/      # Casos de uso
│
├─ applications/
│  └─ app-service/
│     ├─ src/main/java/
│     │  └─ co/com/bancolombia/
│     │     ├─ MainApplication.java          # Punto de entrada
│     │     ├─ config/                       # Configuracion Spring
│     │     └─ prueba/seti/api_test/infrastructure/
│     │        ├─ adapter/in/rest/           # Controllers WebFlux
│     │        ├─ adapter/out/persistence/   # Adapters R2DBC
│     │        │  ├─ entity/                 # Entidades R2DBC
│     │        │  ├─ repository/             # Repositorios R2DBC
│     │        │  └─ mapper/                 # Mappers Entity <-> Domain
│     │        └─ config/                    # Configuracion tecnica
│     └─ src/main/resources/
│        ├─ application.yaml                # Config app
│        └─ schema.sql                      # DDL
│
└─ applications/app-service/src/test/java/   # Pruebas unitarias
```

---

# API Reactiva - Franquicias / Sucursales / Productos

## Descripcion

API REST reactiva para gestionar franquicias, sucursales y productos, construida con Spring Boot WebFlux y persistencia reactiva con R2DBC (MySQL). El codigo sigue Arquitectura Hexagonal (Ports & Adapters) para mantener el dominio desacoplado de frameworks y detalles de infraestructura.

## Caracteristicas

- CRUD basico para Franquicias / Sucursales / Productos.
- Modificacion parcial (PATCH) para stock y nombre.
- Consulta: producto con mayor stock por sucursal para una franquicia.
- Manejo de errores consistente (404/400/500).
- Validacion con Jakarta Validation.

## Requisitos

- Java 21
- Docker Desktop (recomendado para ejecucion con Compose)
- Gradle (o usar ./gradlew.bat)

## Configuracion

La app usa variables de entorno (con valores por defecto):

```
MY_API_DB_HOST=localhost
MY_API_DB_PORT=3306
MY_API_DB_NAME=franquicia
MY_API_DB_USER=root
MY_API_DB_PASSWORD=1234
MY_API_PORT=8082
```

En `applications/app-service/src/main/resources/application.yaml`:

```
spring:
  r2dbc:
    url: r2dbc:mysql://${MY_API_DB_HOST:localhost}:${MY_API_DB_PORT:3306}/${MY_API_DB_NAME:franquicia}?useSSL=false
    username: ${MY_API_DB_USER:root}
    password: ${MY_API_DB_PASSWORD:1234}
  sql:
    init:
      mode: always
      schema-locations: classpath:schema.sql

server:
  port: ${MY_API_PORT:8082}
```

## Ejecucion en local

### Opcion A: Docker Compose (recomendado)

```
docker-compose up --build -d
```

Ver logs:

```
docker-compose logs -f my_api_app
```

### Opcion B: Local (Java + MySQL)

1) Levanta MySQL localmente y crea la base de datos:

```
CREATE DATABASE franquicia;
```

2) Ejecuta la app:

```
./gradlew.bat bootRun
```

## Endpoints

Base URL (local): `http://localhost:8082`

### Franquicias

- POST `/api/v1/franquicias`
- PATCH `/api/v1/franquicias/{id}`

### Sucursales

- POST `/api/v1/sucursales`
- PATCH `/api/v1/sucursales/{id}`

### Productos

- POST `/api/v1/productos`
- DELETE `/api/v1/productos/{id}`
- PATCH `/api/v1/productos/{id}/stock`
- PATCH `/api/v1/productos/{id}`
- GET `/api/v1/productos/max-stock/franquicia/{franquiciaId}`

## Colecciones Postman

En el repositorio se incluyen colecciones listas para importar en Postman:

- **Local (Docker/PC):** `setic_api_collection.postman_collection.json`
- **Servidor en Amazon (AWS EC2):** `setic_api_collection_ec2.postman_collection.json`

La coleccion de **AWS EC2** esta preconfigurada para apuntar a un despliegue remoto usando el DNS publico:

- `http://ec2-54-242-195-142.compute-1.amazonaws.com:8082`

Ademas, usa la variable `{{baseUrl}}` para que puedas cambiar facilmente el host/puerto si el despliegue cambia (por ejemplo si luego usas un ALB, HTTPS o un dominio propio).

### Importar en Postman

1. Postman → **Import**
2. Selecciona el archivo `.json` de la coleccion
3. (Opcional) Ajusta `baseUrl` dentro de la coleccion si cambias de ambiente
4. Ejecuta las requests en el orden sugerido (crear franquicia → crear sucursal → crear producto, etc.)

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
./gradlew.bat clean test
```

Reporte JaCoCo:

- `build/reports/jacocoMergedReport/jacocoMergedReport.xml`
- `build/reports/jacocoMergedReport/html/index.html`

---

## 🛠️ Solucion de problemas

### 1) Postman: `ECONNREFUSED 127.0.0.1:8082`

Este error significa que tu maquina rechazo la conexion TCP a `localhost:8082`. No es un error de JSON ni de Postman: es que no hay nada escuchando en ese puerto (o no esta publicado).

Causas tipicas:

- El contenedor **`my_api_app` no esta corriendo** o esta reiniciandose.
- El contenedor corre, pero **la API no esta escuchando en 8082** (config de `server.port`).
- Estas ejecutando la API en Docker pero **no publicaste el puerto** (`ports: - "8082:8082"`).
- El contenedor levanto pero **fallo al iniciar** (por ejemplo, no logra conectarse a MySQL) y se apaga.

Pasos de verificacion (Docker Compose):

1. Ver estado de contenedores:
   ```powershell
   docker-compose ps
   ```
   - Debes ver `my_api_app` en estado **Up**.
2. Ver logs de la API:
   ```powershell
   docker-compose logs -f my_api_app
   ```
   - Busca un log tipo: “Started ... on port(s): 8082”, o errores de conexion a BD.
3. Verificar que el puerto este publicado en Windows:
   ```powershell
   netstat -ano | findstr :8082
   ```
   - Debe aparecer `LISTENING`. Si no aparece, la API **no esta exponiendo** el puerto.
4. Probar desde el navegador:
   - Abre `http://localhost:8082` o ejecuta un GET a cualquier endpoint.

> Nota: si estas usando Docker Compose, `localhost` solo aplica para el **host**. Dentro de contenedores, `localhost` apunta al contenedor mismo. Por eso, la API debe conectarse a MySQL usando el **nombre del servicio** (por ejemplo `my_api_mysql`).

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

Este proyecto podia usar distintos sistemas de persistencia como **Redis, MySQL, MongoDB o DynamoDB**. Se eligió **MySQL** (relacional) y contenedorización con **Docker** por estas razones:

### Por qué MySQL (modelo relacional)

- **Modelo de datos naturalmente relacional**: una **Franquicia** tiene muchas **Sucursales** y una **Sucursal** tiene muchos **Productos**. Esto encaja perfecto con:
  - llaves foráneas,
  - consistencia referencial,
  - consultas simples y claras.
- **Consistencia (ACID)**: para operaciones como actualizar stock o renombrar entidades, un motor relacional aporta garantías de consistencia y transacciones.
- **Consultas agregadas/ordenamiento**: el caso de uso “producto con mayor stock por sucursal” se resuelve eficientemente con SQL (ORDER BY + LIMIT) y/o índices.
- **Compatibilidad con R2DBC**: al usar WebFlux, es importante mantener el stack no-bloqueante; con R2DBC + MySQL se conserva el enfoque reactivo hasta la base de datos.

### Por que Docker (despliegue reproducible)

- **Entorno local consistente**: fija version de MySQL e inicializacion.
- **Red interna por DNS de servicios**: la API se conecta usando el hostname del servicio (ej. `my_api_mysql`).
- **Onboarding rapido**: con un solo `docker-compose up` se levanta DB + API.

### Alternativas y cuando usarlas

- **Redis**
  - Excelente para cache, sesiones o rate limiting.
  - No es la mejor opcion como fuente de verdad para relaciones complejas.
- **MongoDB (documental)**
  - Util cuando el modelo es mas flexible y orientado a documentos.
  - En este dominio relacional, SQL es mas directo.
- **DynamoDB (NoSQL en AWS)**
  - Util para alta escala y baja latencia.
  - Requiere diseno por patrones de acceso y suele usar DynamoDB Local en dev.
  - Para una prueba tecnica local, MySQL + Docker reduce complejidad operativa.
