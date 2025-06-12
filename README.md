# Identity Service

Servicio de autenticación y autorización multi-tenant con Spring Boot.

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/S-kkipie/identity-microservice-spring)
## 🚀 Características

- **Multi-Tenant**: Cada tenant tiene su propia base de datos
- **Autenticación JWT**: Tokens seguros para autenticación
- **Usuarios Principal y Tenant**: Dos tipos de usuarios diferentes

## ⚙️ Configuración

### application.yml
```yml
spring:
  application:
    name: IDENTITY-SERVICE
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
eureka:
  client:
    service-url:
      defaultZone : http://localhost:8761/eureka/

logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
    org.springframework.transaction: DEBUG

server:
  port: 9898
```

## 🏃‍♂️ Ejecutar

```bash
mvn spring-boot:run
```

El servicio estará disponible en `http://localhost:9898`

## 📡 API Endpoints

### Autenticación

**Login Usuario Principal**
```http
POST /login
{
  "username": "usuario@ejemplo.com",
  "password": "contraseña123"
}
```
**Login Usuario Tenant**
```http
POST /login
Headers: X-Tenant-ID: empresa1
{
  "username": "usuario@empresa1.com",
  "password": "contraseña123"
}
```

**Registrar Usuario Principal**
```http
POST /register
{
  "username": "admin@ejemplo.com",
  "password": "contraseña123",
  "firstName": "Admin",
  "lastName": "Usuario"
}
```

**Registrar Usuario Tenant**
```http
POST /register
Headers: X-Tenant-ID: empresa1
{
  "username": "user@empresa1.com",
  "password": "contraseña123",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

### Usuarios

**Obtener usuario actual**
```http
GET /profile
Headers: Authorization: Bearer tu-jwt-token
```

## 🗄️ Base de Datos

Crear las siguientes bases de datos:

```sql
CREATE DATABASE platform_db;
CREATE DATABASE tenant_db;
```
o usar el docker `docker-compose up`

## 🔧 Estructura del Proyecto

```
./src/main/java/unsa/sistemas/identityservice/
├── Config
│   ├── CurrentTenantIdentifierResolverImpl.java
│   ├── DataSourceBasedMultiTenantConnectionProviderImpl.java
│   ├── HibernateProperties.java
│   ├── MultiTenantConfig.java
│   ├── PrincipalDatabaseConfig.java
│   ├── SecurityConfig.java
│   └── TenantContext.java
├── Controller
│   └── IdentityController.java
├── Docs
│   ├── AuthResponse.java
│   ├── BadResponse.java
│   └── UserResponse.java
├── DTOs
│   ├── Auth.java
│   ├── AuthRequest.java
│   └── RegisterRequest.java
├── IdentityServiceApplication.java
├── Models
│   ├── AbstractUser.java
│   ├── Principal
│   │   └── PrincipalUser.java
│   ├── Role.java
│   └── Tenant
│       └── TenantUser.java
├── Repositories
│   ├── Principal
│   │   └── PrincipalUserRepository.java
│   └── Tenant
│       └── TenantUserRepository.java
├── Security
│   ├── JWTFilter.java
│   ├── SecurityPrincipal.java
│   └── TenantFilter.java
├── Services
│   ├── ComposeUserDetailService.java
│   ├── PrincipalUserService.java
│   └── TenantUserService.java
└── Utils
    ├── JWTUtil.java
    ├── ResponseHandler.java
    └── ResponseWrapper.java

```

## 🛠️ Tecnologías

- Spring Boot 3.x
- Spring Security + JWT
- JPA/Hibernate Multi-Tenant
- PostgreSQL
- Maven
