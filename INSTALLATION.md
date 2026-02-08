# Guía de Instalación y Ejecución

## Requisitos Previos

1. **Java 21** o superior
2. **Maven 3.8+**
3. **MySQL 8.0+** instalado y ejecutándose
4. **Git** (opcional)

## Configuración de la Base de Datos

### 1. Conectarse a MySQL
```bash
mysql -u root -p
```

### 2. Crear la base de datos
```sql
CREATE DATABASE franquicia;
```

O ejecutar el script proporcionado:
```bash
mysql -u root -p < create-database.sql
```

### 3. Verificar credenciales
Asegúrate de que las credenciales en `src/main/resources/application.properties` coincidan con tu configuración de MySQL:

```properties
spring.r2dbc.url=r2dbc:mysql://localhost:3306/franquicia?useSSL=false
spring.r2dbc.username=root
spring.r2dbc.password=1234
```

## Compilación y Ejecución

### Windows (PowerShell)

1. **Compilar el proyecto:**
```powershell
.\mvnw.cmd clean install
```

2. **Ejecutar la aplicación:**
```powershell
.\mvnw.cmd spring-boot:run
```

### Linux/Mac

1. **Compilar el proyecto:**
```bash
./mvnw clean install
```

2. **Ejecutar la aplicación:**
```bash
./mvnw spring-boot:run
```

## Verificación

Una vez iniciada la aplicación, verás en los logs:
```
Started ApiTestApplication in X.XXX seconds
```

La API estará disponible en: `http://localhost:8080`

## Prueba de Endpoints

### 1. Crear una Franquicia
```bash
curl -X POST http://localhost:8080/api/v1/franquicias \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Franquicia Test"}'
```

### 2. Crear una Sucursal
```bash
curl -X POST http://localhost:8080/api/v1/sucursales \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Sucursal Centro", "franquiciaId": 1}'
```

### 3. Agregar un Producto
```bash
curl -X POST http://localhost:8080/api/v1/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Producto A", "stock": 100, "sucursalId": 1}'
```

### 4. Modificar Stock
```bash
curl -X PATCH http://localhost:8080/api/v1/productos/1/stock \
  -H "Content-Type: application/json" \
  -d '{"nuevoStock": 200}'
```

### 5. Obtener Productos con Mayor Stock
```bash
curl http://localhost:8080/api/v1/productos/max-stock/franquicia/1
```

### 6. Eliminar un Producto
```bash
curl -X DELETE http://localhost:8080/api/v1/productos/1
```

## Solución de Problemas

### Error: "No suitable driver found"
- Verifica que MySQL esté ejecutándose
- Verifica las credenciales en `application.properties`

### Error: "Port 8080 already in use"
- Cambia el puerto en `application.properties`:
  ```properties
  server.port=8081
  ```

### Error de compilación
- Asegúrate de tener Java 21 instalado:
  ```bash
  java -version
  ```

## Logs

Los logs se muestran en la consola con el siguiente formato:
```
2026-02-08 10:30:00 - Iniciando creación de franquicia: Franquicia Test
2026-02-08 10:30:01 - Franquicia guardada con ID: 1
```

Nivel de logging configurable en `application.properties`.

