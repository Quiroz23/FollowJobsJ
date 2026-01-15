# FollowJobs

Sistema de seguimiento de postulaciones laborales construido con Spring Boot.

> **Nota:** Este es un proyecto personal de aprendizaje para practicar Java y Spring Boot. No está diseñado para producción.

## Tecnologías

- **Java 21** - Última versión LTS
- **Spring Boot 3.2** - Framework principal
- **Spring Data JPA** - Acceso a datos
- **H2 Database** - Base de datos en memoria (desarrollo)
- **Lombok** - Reducción de código repetitivo
- **Swagger/OpenAPI** - Documentación de API

## Arquitectura

El proyecto sigue una arquitectura de 3 capas:

```
┌─────────────────┐
│   Controller    │  ← Recibe peticiones HTTP
├─────────────────┤
│    Service      │  ← Lógica de negocio
├─────────────────┤
│   Repository    │  ← Acceso a base de datos
└─────────────────┘
```

### Estructura del proyecto

```
backend/
├── src/main/java/com/followjobs/
│   ├── FollowJobsApplication.java    # Punto de entrada
│   ├── config/                       # Configuraciones
│   ├── controller/                   # REST endpoints
│   ├── dto/                          # Objetos de transferencia
│   ├── entity/                       # Entidades JPA
│   ├── exception/                    # Manejo de errores
│   ├── repository/                   # Acceso a datos
│   └── service/                      # Lógica de negocio
└── src/main/resources/
    └── application.yml               # Configuración
```

## Cómo ejecutar

### Requisitos

- Java 21
- Maven 3.8+

### Pasos

1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/FollowJobs.git
cd FollowJobs/backend
```

2. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

3. Acceder a:
   - **API:** http://localhost:8080/api/applications
   - **Swagger UI:** http://localhost:8080/swagger-ui.html
   - **H2 Console:** http://localhost:8080/h2-console

## API Endpoints

### Postulaciones

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/applications` | Obtener todas las postulaciones |
| GET | `/api/applications/{id}` | Obtener por ID |
| GET | `/api/applications/portal/{portal}` | Filtrar por portal |
| GET | `/api/applications/status/{status}` | Filtrar por estado |
| GET | `/api/applications/search?company=` | Buscar por empresa |
| POST | `/api/applications` | Crear nueva postulación |
| PUT | `/api/applications/{id}` | Actualizar postulación |
| PATCH | `/api/applications/{id}/status` | Actualizar solo estado |
| DELETE | `/api/applications/{id}` | Eliminar postulación |
| POST | `/api/applications/clean` | Limpiar datos inválidos |

### Estados

- `SENT` - Enviado
- `REJECTED` - Rechazado
- `ACCEPTED` - Aceptado
- `INTERVIEW` - Entrevista
- `NO_RESPONSE` - Sin respuesta

### Portales soportados

- LinkedIn
- Indeed
- Computrabajo
- ChileTrabajos

## Ejemplo de uso

### Crear una postulación

```bash
curl -X POST http://localhost:8080/api/applications \
  -H "Content-Type: application/json" \
  -d '{
    "company": "Google",
    "position": "Software Engineer",
    "portal": "LinkedIn",
    "status": "SENT"
  }'
```

### Actualizar estado

```bash
curl -X PATCH http://localhost:8080/api/applications/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "INTERVIEW",
    "notes": "Entrevista técnica programada"
  }'
```

## Próximas funcionalidades

- [ ] Integración con Gmail API para detección automática de postulaciones
- [ ] Frontend con Angular
- [ ] Estadísticas y dashboards
- [ ] Migración a PostgreSQL

## Licencia

MIT
