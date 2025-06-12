# Identity Service

Servicio de autenticación y autorización multi-tenant con Spring Boot y arquitectura de microservicios.

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/S-kkipie/identity-microservice-spring)

## Características

- **Multi-Tenant**: Cada tenant tiene su propia base de datos con gestión dinámica
- **Autenticación JWT**: Tokens seguros con refresh tokens para autenticación
- **Usuarios Principal y Tenant**: Dos tipos de usuarios con diferentes contextos
- **Gestión Dinámica de Bases de Datos**: Registro automático de nuevas organizaciones
- **Integración con RabbitMQ**: Mensajería asíncrona para eventos de tenant
- **Registro en Eureka**: Descubrimiento de servicios
- **Encriptación AES**: Seguridad adicional para datos sensibles

## Arquitectura

El servicio utiliza una arquitectura multi-tenant donde:
- **Base de Datos Principal** (`platform_db`): Almacena usuarios administradores y configuraciones globales
- **Bases de Datos Tenant** (`tenant_db`): Cada organización tiene su propia instancia

## ⚙️ Configuración

### Variables de Entorno

```properties
# Configuración de Base de Datos
connection.baseUrl=jdbc:postgresql://localhost:5432
connection.username=postgres
connection.password=1234
connection.driver_class=org.postgresql.Driver

# Bases de Datos
platform.database=platform_db
tenant.database=tenant_db

# JWT
jwt.secret=diahudhasuidhauisfgyigcnuwciansjdciohbepbhchpqonh
jwt.expiration=3000000

# AES Encryption
app.aes.secure-key=12345678901234567890123456789012

# Usuario Inicial
initial-user.username=admin
initial-user.password=admin123
```

### application.yml

```yml
spring:
  application:
    name: IDENTITY-SERVICE
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

app:
  rabbitmq:
    exchange: tenant.identity.exchange
    queue: tenant.database.queue
    routing-key: tenant.database.created

logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
    org.springframework.transaction: DEBUG
    unsa.sistemas.identityservice: DEBUG

server:
  port: 9898
```

## 🏃‍♂️ Ejecutar

### Prerrequisitos

1. **PostgreSQL** en `localhost:5432`
2. **RabbitMQ** en `localhost:5672`
3. **Eureka Server** en `localhost:8761`

### Usando Maven

```bash
mvn spring-boot:run
```

## 📡 API Endpoints

### 🔐 Autenticación

#### Login Usuario Principal
```http
POST /login
Content-Type: application/json

{
  "username": "admin@ejemplo.com",
  "password": "admin123"
}
```

#### Login Usuario Tenant
```http
POST /login
Content-Type: application/json
X-Org-Code: empresa1

{
  "username": "usuario@empresa1.com",
  "password": "contraseña123"
}
```

**Respuesta exitosa:**
```json
{
  "message": "User authentication successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refresh": "dGhpc19pc19hX3JlZnJlc2hfdG9rZW4uLi4="
  }
}
```

### 👤 Registro de Usuarios

#### Registrar Usuario Principal
```http
POST /register
Content-Type: application/json

{
  "username": "admin@ejemplo.com",
  "password": "contraseña123",
  "firstName": "Admin",
  "lastName": "Usuario",
  "phoneNumber": 987654321,
  "country": "Peru"
}
```

#### Registrar Usuario Tenant
```http
POST /register
Content-Type: application/json
X-Org-Code: empresa1

{
  "username": "user@empresa1.com",
  "password": "contraseña123",
  "firstName": "Juan",
  "lastName": "Pérez",
  "phoneNumber": 987654321,
  "country": "Peru"
}
```

### 🏢 Gestión de Organizaciones (Admin)

#### Registrar Nueva Base de Datos
```http
POST /admin/register-new
Content-Type: application/json

{
  "orgCode": "my-company",
  "username": "12312-1231-1212-3-213",
  "password": "MySecretPass123",
  "url": "jdbc:postgresql://localhost:5432/unsa_identity_db"
}
```

#### Registrar Base de Datos Existente
```http
POST /admin/register-existing
Content-Type: application/json

{
  "orgCode": "existing-company",
  "username": "existing-user-id",
  "password": "MySecretPass123",
  "url": "jdbc:postgresql://localhost:5432/existing_db"
}
```

### 👥 Perfil de Usuario

#### Obtener Perfil Actual
```http
GET /profile
Authorization: Bearer tu-jwt-token
```

**Respuesta:**
```json
{
  "message": "User profile generated",
  "data": {
    "id": "uuid-123",
    "username": "user@example.com",
    "role": "ROLE_USER",
    "firstName": "Juan",
    "lastName": "Pérez",
    "country": "Peru",
    "phoneNumber": 987654321,
    "enabled": true,
    "createdAt": "2025-01-01T00:00:00Z",
    "updatedAt": "2025-01-01T00:00:00Z"
  }
}
```

## 🗄️ Base de Datos

### Configuración Inicial
Bases de datos configuradas en el docker compose 
`docker-compose up`

### Estructura Multi-Tenant

- **platform_db**: Usuarios administradores, configuraciones globales
- **tenant_db**: Base de datos template para nuevos tenants
- **Bases dinámicas**: Cada organización registrada genera su propia BD

## 🔧 Estructura del Proyecto

```
./src/main/java/unsa/sistemas/identityservice/
├── Config
│   ├── Context
│   │   └── OrgContext.java
│   ├── HibernateProperties.java
│   ├── InitialUser
│   │   ├── InitialUserLoader.java
│   │   └── InitialUserProperties.java
│   ├── MultiTenantConfig.java
│   ├── MultiTenantImpl
│   │   ├── CurrentTenantIdentifierResolverImpl.java
│   │   └── DataSourceBasedMultiTenantConnectionProviderImpl.java
│   ├── PrincipalDatabaseConfig.java
│   ├── RabbitMQConfig.java
│   └── SecurityConfig.java
├── Controller
│   ├── DataBaseController.java
│   └── IdentityController.java
├── Docs
│   ├── AuthResponse.java
│   ├── BadResponse.java
│   ├── StringResponse.java
│   └── UserResponse.java
├── DTOs
│   ├── Auth.java
│   ├── AuthRequest.java
│   ├── CreateDataBaseEvent.java
│   ├── RegisterDataBaseDTO.java
│   └── RegisterRequest.java
├── IdentityServiceApplication.java
├── Messaging
│   └── IdentityEventListener.java
├── Models
│   ├── AbstractUser.java
│   ├── Principal
│   │   └── PrincipalUser.java
│   ├── Role.java
│   └── Tenant
│       └── EmployeeUser.java
├── Repositories
│   ├── Principal
│   │   └── PrincipalUserRepository.java
│   └── Tenant
│       └── EmployeeUserRepository.java
├── Security
│   ├── JWTFilter.java
│   ├── JWTUtil.java
│   ├── SecurityPrincipal.java
│   └── TenantFilter.java
├── Services
│   ├── ComposeUserDetailService.java
│   ├── EmployeeUserService.java
│   ├── PrincipalUserService.java
│   ├── RegisterDataBaseService.java
│   └── SchemaService.java
└── Utils
    ├── EncryptionUtil.java
    ├── ResponseHandler.java
    └── ResponseWrapper.java

```

## 🛠️ Tecnologías

- **Spring Boot 3.x** - Framework principal
- **Spring Security + JWT** - Autenticación y autorización
- **JPA/Hibernate Multi-Tenant** - Persistencia multi-tenant
- **PostgreSQL** - Base de datos relacional
- **RabbitMQ** - Mensajería asíncrona
- **Eureka Client** - Descubrimiento de servicios
- **Maven** - Gestión de dependencias

## 🔒 Seguridad

### Roles de Usuario
- `ROLE_SUPERADMIN`: Administrador global del sistema
- `ROLE_ADMIN`: Administrador de organización
- `ROLE_SUPERUSER`: Usuario con permisos extendidos
- `ROLE_USER`: Usuario estándar

### Headers Requeridos
- `Authorization: Bearer <token>` - Para endpoints protegidos
- `X-Org-Code: <org-code>` - Para operaciones específicas de tenant


## 📝 Logs y Monitoreo

El servicio incluye logging detallado para:
- Consultas SQL de Hibernate
- Transacciones de base de datos
- Operaciones de seguridad
- Eventos de tenant


