# Sistema de Gestión de Proyectos e Inversiones - Web 2026-i

## Descripción del Proyecto
<img width="1354" height="745" alt="image" src="https://github.com/user-attachments/assets/8e8b8083-b92e-4fd6-bdcb-ba108e2e8657" />


**Web-2026-i** es una aplicación web desarrollada con **Spring Boot 4.0.2** que implementa un sistema integral de gestión de proyectos e inversiones para instituciones educativas. La aplicación permite administrar información sobre proyectos de desarrollo, sus inversiones asociadas y generar reportes y análisis de inversión.

### Tecnología y Stack Tecnológico

- **Backend**: Spring Boot 4.0.2
- **Framework Web**: Spring MVC con Thymeleaf como motor de plantillas
- **Base de Datos**: PostgreSQL con JPA/Hibernate
- **Seguridad**: Spring Security
- **Testing**: JUnit 5, Spring Security Test
- **Utilidades**: Lombok, PDFBox, JFreeChart, Maven
- **Lenguajes**: Java 25

---

## Funcionalidades Principales
<img width="475" height="382" alt="image" src="https://github.com/user-attachments/assets/0eae96ec-d5bd-494a-9701-b8ac96d5d465" />

### 1. **Autenticación y Autorización**
- Sistema de login seguro con Spring Security
- Control de acceso basado en roles:
  - **ADMIN**: Acceso completo a todos los módulos y datos
  - **USUARIO_TIC**: Gestión de proyectos de infraestructura TIC
  - **USUARIO_INFRAESTRUCTURA**: Gestión de proyectos de infraestructura
  - **USUARIO_AGRICULTURA**: Gestión de proyectos agrícolas
- Visualización y gestión de usuarios con roles específicos
- Validación de permisos por sectorial asignada



### 2. **Gestión de Proyectos**
<img width="739" height="602" alt="image" src="https://github.com/user-attachments/assets/12c64def-1520-437a-bf11-1f49d98fba81" />
- **Crear Proyectos**: Formulario para registrar nuevos proyectos con información completa
- **Editar Proyectos**: Modificación de datos existentes con validaciones
- **Gestión de Estados**: Clasificación de proyectos por estados (Planeación, Ejecución, Completado, etc.)
- **Asociación de Proyectos**: Vinculación con municipios y sectoriales específicas
- **Información del Proyecto**: Nombre, fecha de registro, estado, tipo, responsable
- **Validación Temporal**: Asegura que las inversiones no sean anteriores a la fecha de registro del proyecto

### 3. **Gestión de Inversiones**
- **Registro de Inversiones Simple**: Agregar una inversión a la vez
- **Registro de Inversiones Múltiples**: Crear múltiples inversiones en una misma fecha con diferentes fuentes
- **Fuentes de Inversión**: 
  - Municipio
  - Departamento
  - Nación
  - Otro Aportante (con nombre del aportante)
- **Montos y Fechas**: Registrar monto invertido y fecha de inversión
- **Edición y Eliminación**: Modificar o eliminar inversiones registradas
- **Validaciones**: Control de fechas y montos

### 4. **Dashboard y Visualización**
- **Resumen Ejecutivo**: 
  - Total de proyectos registrados
  - Inversión total acumulada
  - Distribución por municipios
  - Estadísticas por estado de proyecto
- **Filtros Dinámicos**: 
  - Filtrar por municipio
  - Filtrar por sectorial
  - Filtrar por proyecto específico
  - Filtrar por estado del proyecto
- **Gráficos Interactivos**: Visualización de inversiones por municipios
- **Información Detallada**: Tabla con listado de proyectos e inversiones

### 5. **Consultas Avanzadas**
- **Consulta por Estado**: Buscar proyectos por estado en un rango de fechas
- **Consulta por Municipio e Inversión**: Filtrar inversiones por:
  - Municipio específico
  - Fuente de inversión
  - Rango de fechas de inversión
- **Resultados Detallados**: Visualización completa de resultados con desglose de inversiones

### 6. **Generación de Reportes**
- **Reportes en PDF**: Generar informes en PDF con información de proyectos filtrados
- **Exportación de Datos**: Descarga de informes con formato profesional
- **Información Incluida**: Detalles de proyectos, inversiones, municipios y sectoriales

### 7. **Sistema de Notificaciones por Email**
- **Notificación de Proyectos Nuevos**: Envío automático de confirmación cuando se crea un proyecto
- **Notificación de Reportes**: Envío de confirmación cuando se genera un reporte PDF
- **Enrutamiento por Rol**: Los correos se envían a diferentes destinatarios según el rol del usuario
- **Configuración Flexible**: Correos configurables para cada tipo de rol
- **Gmail SMTP**: Integración con servidor SMTP de Gmail (smtp.gmail.com:587)

### 8. **Control de Acceso por Sectorial**
- **Restricción de Datos**: Los usuarios no-admin solo ven datos de su sectorial asignada
- **Validación Constante**: Verifica que los usuarios accedan solo a proyectos e inversiones de su sectorial
- **Seguridad Multinivel**: Validación tanto en controladores como en servicios

---

## Estructura de Componentes

### Controladores
- **AppController**: Gestiona el dashboard principal, visualización de datos y reportes
- **GestionMvcController**: Controla la creación, edición y gestión de proyectos e inversiones
- **ProyectoApiController**: API REST para operaciones sobre proyectos

### Servicios
- **ProyectoGestionService**: Lógica de negocio para proyectos e inversiones
- **DashboardService**: Generación de datos y estadísticas para el dashboard
- **DashboardServiceImpl**: Implementación del servicio de dashboard
- **ReportePdfService**: Generación de reportes en formato PDF
- **EmailService**: Envío de correos electrónicos
- **UserDetailsServiceImpl**: Integración con Spring Security

### Entidades
- **ProyectoEntity**: Información de proyectos
- **InversionEntity**: Información de inversiones asociadas a proyectos
- **UsuarioEntity**: Datos de usuarios del sistema
- **SectorialEntity**: Clasificación sectorial (TIC, Infraestructura, Agricultura, etc.)
- **MunicipioEntity**: Información de municipios
- **RolEntity**: Roles del sistema
- **EstadoProyecto**: Estados posibles de un proyecto
- **FuenteInversion**: Fuentes posibles de inversión

### DTOs (Data Transfer Objects)
- **ProyectoDetalleRespuestaDTO**: Respuesta con detalles de proyectos
- **InversionesMultiplesDTO**: DTO para manejo de inversiones múltiples
- **DashboardResumenDTO**: Resumen para el dashboard
- **NombreValorDTO**: Par nombre-valor para gráficos

### Repositorios
- **ProyectoRepository**: Acceso a datos de proyectos
- **InversionRepository**: Acceso a datos de inversiones
- **UsuarioRepository**: Acceso a datos de usuarios
- **SectorialRepository**: Acceso a datos de sectoriales
- **MunicipioRepository**: Acceso a datos de municipios
- **RolRepository**: Acceso a datos de roles

---

## Flujos de Trabajo Principales

### Flujo de Creación de Proyecto
1. Usuario accede a "Crear Proyecto"
2. Sistema valida permisos y muestra formulario
3. Usuario ingresa información del proyecto
4. Sistema valida datos y guarda en BD
5. Sistema envía correo de confirmación al rol correspondiente
6. Usuario recibe confirmación

### Flujo de Registro de Inversión
1. Usuario accede a "Registrar Inversión"
2. Usuario selecciona proyecto
3. Usuario ingresa fecha y montos de inversión
4. Sistema valida:
   - Que la fecha sea >= fecha de registro del proyecto
   - Que al menos un monto sea ingresado
5. Sistema guarda inversión(es) en BD
6. Usuario recibe confirmación

### Flujo de Consulta y Análisis
1. Usuario accede al Dashboard
2. Sistema carga resumen ejecutivo
3. Usuario aplica filtros deseados
4. Sistema filtra datos según permisos
5. Usuario visualiza resultados y gráficos
6. Usuario puede generar y descargar PDF

---

## Seguridad y Validaciones

- **Autenticación obligatoria**: Todos los accesos requieren login
- **Control de permisos por rol**: Cada operación valida el rol del usuario
- **Isolamiento de datos**: Los usuarios no-admin solo ven su sectorial
- **Validación temporal**: Las fechas de inversión no pueden ser anteriores a la fecha de registro
- **Manejo de excepciones**: Mensajes claros de error para el usuario
- **Transacciones**: Operaciones con garantía de consistencia

---

## Requisitos del Sistema

- **Java**: 25 o superior
- **Maven**: 3.6+
- **PostgreSQL**: 12+
- **Puerto**: 8080 (configurable)

---

## Configuración Inicial

1. Clonar el repositorio
2. Configurar la base de datos PostgreSQL
3. Actualizar `application.properties` con credenciales de BD y correo
4. Ejecutar: `mvn clean install`
5. Ejecutar: `mvn spring-boot:run`
6. Acceder a: `http://localhost:8080/login`

---

## Estado del Proyecto

- **Versión**: 0.0.1-SNAPSHOT
- **Estado**: En desarrollo
- **Último Build**: Incluye tests unitarios e integración

---

## Desarrolladores

- Dayanna Huertas
- Yeison Romero
