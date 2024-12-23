# Test App Móvil - Backend Pago de Servicios

## Requisitos Previos

Asegúrate de tener instalados los siguientes componentes en tu sistema:

1. **Java**: Versión 17 o superior.
   - [Descargar e instalar Java](https://www.oracle.com/java/technologies/javase-downloads.html)
2. **Maven**: Versión 3.8.0 o superior.
   - [Descargar e instalar Maven](https://maven.apache.org/install.html)
3. **PostgreSQL**: Versión 13 o superior.
   - [Descargar e instalar PostgreSQL](https://www.postgresql.org/download/)
4. **Docker** (opcional): Si deseas ejecutar la aplicación en un contenedor Docker.
   - [Descargar e instalar Docker](https://www.docker.com/products/docker-desktop)
  
# Pasos para Clonar, Construir y Ejecutar la Aplicación

git clone https://github.com/tu_usuario/tu_repositorio.git
cd tu_repositorio

mvn clean install

mvn spring-boot:run

# Accede a la aplicación:

La API estará disponible en http://localhost:8080.
La documentación Swagger estará en http://localhost:8080/swagger-ui.html.

# Ejecucion en Docker
docker build -t test-app-movil .

docker run -p 8080:8080 test-app-movil

La API estará disponible en http://localhost:8080.
La documentación Swagger estará en http://localhost:8080/swagger-ui.html.

## Configuración de la Base de Datos PostgreSQL

1. **Crea una base de datos en PostgreSQL**:
   - Nombre de la base de datos: `test_app_movil`
   - Usuario: `postgres`
   - Contraseña: `postgres`

2. **Ejecuta el siguiente script SQL para crear las tablas requeridas**:
   ```sql
   CREATE TABLE usuarios (
       id SERIAL PRIMARY KEY,
       nombre VARCHAR(100) NOT NULL,
       email VARCHAR(100) UNIQUE NOT NULL,
       documento VARCHAR(50) NOT NULL,
       saldo NUMERIC(10, 2) NOT NULL,
       password VARCHAR(255) NOT NULL
   );

   CREATE TABLE servicios (
       id SERIAL PRIMARY KEY,
       nombre VARCHAR(100) NOT NULL,
       codigo VARCHAR(50) UNIQUE NOT NULL,
       deuda NUMERIC(10, 2) NOT NULL
   );

   CREATE TABLE transacciones (
       id SERIAL PRIMARY KEY,
       usuario_id INTEGER REFERENCES usuarios(id),
       servicio_id INTEGER REFERENCES servicios(id),
       monto NUMERIC(10, 2) NOT NULL,
       referencia VARCHAR(100),
       fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   
