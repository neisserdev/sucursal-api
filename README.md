# 🏢 Sucursal API - Gestión de Sucursales, Productos y Ventas

API REST desarrollada con **Spring Boot 4** (v4.0.6) para gestionar sucursales, productos, registrar ventas y obtener estadísticas con PostgreSQL, validación y paginación.

## 🛠️ Tecnologías

- **Java 25** + **Spring Boot 4**
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL 12+** - Base de datos
- **Spring Validation** - Validación de requests
- **Spring Cache + Caffeine** - Cacheo de respuestas GET
- **Springdoc OpenAPI (Swagger UI)** - Documentación de la API
- **Spring Boot Actuator** - Endpoints de monitoreo
- **MapStruct 1.6.3** - Mapeo Entity ⇄ DTO
- **Lombok 1.18.44** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias

## 📋 Requisitos

- Java 17+
- Maven 3.6+
- PostgreSQL 12+

## 🗄️ Configuración de Base de Datos

### PostgreSQL Local

**1. Crear base de datos y usuario:**
```bash
# Conectar a PostgreSQL (Linux/macOS)
sudo -u postgres psql

# O en Windows (abre psql directamente)
psql -U postgres
```

**2. Ejecutar en la terminal de PostgreSQL:**
```sql
CREATE DATABASE sucursal_db;
CREATE USER userxxx WITH PASSWORD '12345678';
GRANT ALL PRIVILEGES ON DATABASE sucursal_db TO userxxx;
```

**3. Verificar configuración en [src/main/resources/application.properties](src/main/resources/application.properties):**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sucursal_db
spring.datasource.username=userxxx
spring.datasource.password=12345678
spring.jpa.hibernate.ddl-auto=update
```

Las tablas se crean automáticamente al iniciar la aplicación.

## 📚 Documentación OpenAPI

- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

## 🚀 Ejecutar la Aplicación

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

La app estará disponible en `http://localhost:8080`

---

## 🔌 Endpoints de la API

Base URL: `http://localhost:8080/api`

### 📍 BRANCHES (Sucursales)

#### Listar sucursales (paginado)
`GET /branches?page=0&size=10&sort=id,asc`

**Parámetros:** `page`, `size`, `sort` (Spring Pageable)

**Respuesta `200 OK`:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Sucursal Bogotá",
      "address": "Carrera 7 No. 45-89, Centro Empresarial, Bogotá D.C."
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 1,
  "totalPages": 1,
  "isFirst": true,
  "isLast": true,
  "hasNext": false,
  "hasPrevious": false
}
```

#### Obtener sucursal por ID
`GET /branches/{id}`

**Respuesta `200 OK`:**
```json
{
  "id": 1,
  "name": "Sucursal Bogotá",
  "address": "Carrera 7 No. 45-89, Centro Empresarial, Bogotá D.C."
}
```

#### Crear sucursal
`POST /branches` → `201 Created`

**Body:**
```json
{
  "name": "Sucursal Bogotá",
  "address": "Carrera 7 No. 45-89, Centro Empresarial, Bogotá D.C."
}
```

**Validaciones:**
- `name`: obligatorio
- `address`: obligatorio, mínimo 10 caracteres

#### Actualizar sucursal
`PUT /branches/{id}` → `200 OK`

**Body:** igual a creación

#### Eliminar sucursal
`DELETE /branches/{id}` → `204 No Content`

---

### 🛍️ PRODUCTS (Productos)

#### Listar productos (paginado)
`GET /products?page=0&size=10&sort=id,asc`

**Respuesta `200 OK`:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Laptop Profesional Dell XPS 15",
      "description": "Computadora portátil de alto rendimiento con procesador Intel i7, 16GB RAM y pantalla 4K para trabajo profesional",
      "price": 2500.00
    },
    {
      "id": 2,
      "name": "Monitor LG UltraWide 34\"",
      "description": "Monitor ultraancho 34 pulgadas con resolución 3440x1440, ideal para diseño gráfico y análisis de datos",
      "price": 650.50
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 2,
  "totalPages": 1,
  "isFirst": true,
  "isLast": true,
  "hasNext": false,
  "hasPrevious": false
}
```

#### Obtener producto por ID
`GET /products/{id}`

**Respuesta `200 OK`:**
```json
{
  "id": 1,
  "name": "Laptop Profesional Dell XPS 15",
  "description": "Computadora portátil de alto rendimiento con procesador Intel i7, 16GB RAM y pantalla 4K para trabajo profesional",
  "price": 2500.00
}
```

#### Crear producto
`POST /products` → `201 Created`

**Body:**
```json
{
  "name": "Laptop Profesional Dell XPS 15",
  "description": "Computadora portátil de alto rendimiento con procesador Intel i7, 16GB RAM y pantalla 4K para trabajo profesional",
  "price": 2500.00
}
```

**Validaciones:**
- `name`: obligatorio
- `description`: mínimo 10 caracteres, máximo 250
- `price`: obligatorio, mayor que 0

#### Actualizar producto
`PUT /products/{id}` → `200 OK`

**Body:** igual a creación

#### Eliminar producto
`DELETE /products/{id}` → `204 No Content`

---

### 💰 SALES (Ventas)

#### Crear venta
`POST /sales` → `201 Created`

**Body:**
```json
{
  "branchId": 1,
  "products": [
    {
      "productId": 1,
      "quantity": 2,
      "unitPrice": 2500.00
    },
    {
      "productId": 2,
      "quantity": 1,
      "unitPrice": 650.50
    }
  ]
}
```

**Respuesta `201 Created`:**
```json
{
  "saleId": 1001,
  "time": "2026-05-01T14:30:45.123456",
  "total": 5650.50
}
```

#### Listar ventas de una sucursal (paginado)
`GET /branches/{id}/sales?page=0&size=10&sort=time,desc`

**Respuesta `200 OK`:**
```json
{
  "content": [
    {
      "saleId": 1001,
      "time": "2026-05-01T14:30:45.123456",
      "total": 5650.50
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 1,
  "totalPages": 1,
  "isFirst": true,
  "isLast": true,
  "hasNext": false,
  "hasPrevious": false
}
```

---

### 📊 STATISTICS (Estadísticas)

#### Mejor producto vendido
`GET /statistics/best-selling-product`

**Respuesta `200 OK`:**
```json
{
  "id": 1,
  "name": "Laptop Profesional Dell XPS 15",
  "description": "Computadora portátil de alto rendimiento con procesador Intel i7, 16GB RAM y pantalla 4K para trabajo profesional",
  "price": 2500.00
}
```

**Respuesta `204 No Content`** (sin ventas)

#### Ingresos totales de sucursal
`GET /statistics/branches/{id}/revenue`

**Respuesta `200 OK`:**
```
5650.50
```

(Devuelve un `BigDecimal` con los ingresos totales)

---

## ⚠️ Manejo de Errores

La API implementa **RFC 9457 (Problem Details for HTTP APIs)** usando `ProblemDetail` de Spring.

### Validación (400 Bad Request)

```json
{
  "type": "https://example.com/problems/VALIDATION_ERROR",
  "title": "Validation error",
  "status": 400,
  "detail": "Los datos enviados no son válidos",
  "instance": "/api/products",
  "timestamp": "2026-05-01T14:30:45.123456Z",
  "code": "VALIDATION_ERROR",
  "errors": [
    "name: el nombre del producto es requerido",
    "price: el precio debe ser mayor que 0"
  ],
  "counts": 2
}
```

### Recurso no encontrado (404 Not Found)

```json
{
  "type": "https://example.com/problems/RESOURCE_NOT_FOUND",
  "title": "Resource not found",
  "status": 404,
  "detail": "Sucursal no encontrada con ID: 999",
  "instance": "/api/branches/999",
  "timestamp": "2026-05-01T14:30:45.123456Z",
  "code": "RESOURCE_NOT_FOUND"
}
```

### Error interno (500 Internal Server Error)

```json
{
  "type": "https://example.com/problems/INTERNAL_ERROR",
  "title": "Internal server error",
  "status": 500,
  "detail": "Ha ocurrido un error interno durante el procesamiento",
  "instance": "/api/sales",
  "timestamp": "2026-05-01T14:30:45.123456Z",
  "code": "INTERNAL_ERROR"
}
```

---

## 🧪 Ejemplos con curl

```bash
# Crear sucursal
curl -X POST http://localhost:8080/api/branches \
  -H "Content-Type: application/json" \
  -d '{"name":"Sucursal Bogotá","address":"Carrera 7 No. 45-89, Centro Empresarial, Bogotá D.C."}'

# Crear producto
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop Profesional Dell XPS 15","description":"Computadora portátil de alto rendimiento con procesador Intel i7, 16GB RAM y pantalla 4K","price":2500.00}'

# Crear venta
curl -X POST http://localhost:8080/api/sales \
  -H "Content-Type: application/json" \
  -d '{
    "branchId":1,
    "products":[
      {"productId":1,"quantity":2,"unitPrice":2500.00},
      {"productId":2,"quantity":1,"unitPrice":650.50}
    ]
  }'

# Listar sucursales
curl http://localhost:8080/api/branches?page=0&size=10

# Ver ingresos de sucursal 1
curl http://localhost:8080/api/statistics/branches/1/revenue

# Mejor producto vendido
curl http://localhost:8080/api/statistics/best-selling-product
```

---

## 📝 Notas Técnicas

- **Tablas:** Se crean automáticamente con `hibernate.ddl-auto=update`
- **Paginación:** Usa `page` (0-based), `size` (default 20) y `sort`
- **Cacheo:** Los GET están cacheados con Caffeine (10 minutos de expiración)
- **Validación:** RequestDTO con anotaciones Jakarta Validation
- **Serialización:** BigDecimal para precios, LocalDateTime para timestamps

## 👨‍💻 Autor

**Neisser Torres Peña** - [@neisserdev](https://github.com/neisserdev)