# Sistema de Seguimiento de Tareas y Evaluación de Compromiso de Seguridad

## Descripción General
Este sistema de seguimiento de tareas y evaluación de compromiso de seguridad está diseñado para facilitar la gestión de tareas realizadas por empleados en diversas ubicaciones durante sus turnos de trabajo y asegurar el cumplimiento de las normativas de seguridad a través de un cuestionario interactivo. 

## Objetivos Principales
El proyecto tiene como objetivos:
1. **Asignación de Tareas:** Asignar trabajadores a ubicaciones específicas y gestionar las tareas de limpieza y mantenimiento.
2. **Evaluación de Compromiso de Seguridad:** Introducir un cuestionario al inicio de la sesión de trabajo para evaluar el compromiso del empleado con las normativas de seguridad.
3. **Reporte de Tareas Cumplidas:** Permitir a los empleados informar las tareas cumplidas durante su turno.
4. **Generación de Informes:** Crear informes diarios y mensuales para supervisar las tareas y la adherencia a las normativas de seguridad.
5. **Administración Efectiva:** Facilitar la consulta de informes y gestionar la asignación de tareas y cuestionarios.

## Funcionalidades Clave
### Para Empleados
- **Inicio de Sesión Seguro:** Acceso a través de un navegador web con autenticación segura.
- **Selección de Ubicación y Turno:** Los empleados seleccionan su ubicación y turno de trabajo.
- **Lista de Tareas:** Visualización de tareas asignadas con opciones para marcar tareas completadas.
- **Cuestionario de Seguridad:** Cuestionario interactivo al inicio de cada sesión para reforzar el compromiso con las normativas de seguridad.

### Para Administradores
- **Gestión de Cuestionarios:** Capacidad para editar las preguntas del cuestionario de seguridad.
- **Visualización de Informes:** Acceso a informes diarios y mensuales sobre tareas y cuestionarios completados.
- **Supervisión del Cumplimiento:** Herramientas para supervisar la realización de tareas y la adhesión a las normativas de seguridad.

## Tecnologías Utilizadas
- **Backend:** Java con Spring Boot, ofreciendo un robusto framework para la lógica del negocio.
- **Frontend:** Thymeleaf junto con HTML5, CSS3 y JavaScript, proporcionando una interfaz de usuario interactiva y responsive.
- **Base de Datos:** SQL Server, gestionando eficientemente los datos y relaciones complejas.
- **Seguridad:** Configuración de seguridad personalizada en Spring Security para la autenticación y autorización.

## Estructura del Proyecto
La estructura del proyecto está diseñada para maximizar la mantenibilidad y la escalabilidad:
- **Controllers:** Controladores MVC para gestionar las solicitudes de usuario.
- **Entities:** Modelos de dominio que representan tablas en la base de datos.
- **Repositories:** Capa de abstracción que facilita el acceso y la manipulación de datos.
- **Services:** Servicios que encapsulan la lógica de negocio.
- **DTOs y Enums:** Objetos de Transferencia de Datos y enumeraciones para una gestión de datos eficiente.

## Pruebas
El proyecto está altamente testado con más de 160 pruebas automatizadas que cubren funcionalidades esenciales, asegurando que el sistema sea fiable y robusto. Las pruebas incluyen:
- **Pruebas Unitarias y de Integración:** Para servicios y controladores, asegurando que cada componente funcione correctamente de forma aislada y en conjunto.
- **Automatización de Pruebas de Seguridad:** Pruebas para evaluar la seguridad y el manejo de errores.

## Uso
Para utilizar este proyecto, clona el repositorio, configura las propiedades de aplicación según el entorno y ejecuta el sistema usando Maven o tu IDE preferido.

## Contribución
Las contribuciones son bienvenidas, y cualquier colaboración para mejorar el proyecto o extender sus funcionalidades será apreciada.

## Licencia
Este proyecto es de código abierto bajo la licencia MIT, permitiendo su uso y distribución libremente, siempre que se otorgue el crédito correspondiente al autor original.

