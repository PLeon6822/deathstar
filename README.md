# Prueba de código

## Diseño de las APIs

### API Gateway

Método | Dirección | Parámetros | Acción
------ | --------- | ---------- | ------
GET | /resources |  | Devuelve la disponibilidad de recursos
POST | /build/hull | position | Comprueba la presencia de recursos y construye una cubierta
POST | /build/reinforced_hull | position | Comprueba la presencia de recursos y construye una cubierta acorazada
POST | /build/cannon | position | Comprueba la presencia de recursos y construye un cañón
POST | /build/reinforced_cannon | position | Comprueba la presencia de recursos y construye un cañón acorazado

### API Recursos

Método | Dirección | Parámetros | Acción
------ | --------- | ---------- | ------
GET | /resources/titanium |  | Lee en BD la cantidad disponible de titanio
GET | /resources/plasma |  | Lee en BD la cantidad disponible de plasma
PUT | /resources/titanium | amount | Actualiza en BD la cantidad disponible de titanio
PUT | /resources/plasma | amount | Actualiza en BD la cantidad disponible de plasma
### API Construcción

Método | Dirección | Parámetros | Acción
------ | --------- | ---------- | ------
------ | --------- | ---------- | ------

## Arquitectura

![Arquitectura](https://raw.githubusercontent.com/PLeon6822/deathstar/master/architecture.png "Arquitectura")

## Explicación

El funcionamiento de este sistema está distribuido en 3 APIs REST que comunican al usuario con una base de datos de modo que se simula el proceso de reconstrucción de la Estrella de la Muerte. La idea básica que guía esta estructura es que el usuario tenga todas las acciones descritas disponibles a través de una interfaz web y sea capaz de realizar peticiones a la base de datos, repartiendo las operaciones de lectura y escritura en función del propósito de la petición: un servidor para recursos y otro para construcción.

### API Gateway

El primer API descrito en el flujo de arquitectura está desarrollado en Java, y conecta al usuario con el resto del sistema. Está construido utilizando Spring Boot, montado sobre Maven y expuesto al puerto 8080. Este API acepta peticiones de lectura (GET) y escritura (POST) que afectan tanto al API de recursos como al de construcción, y devuelven información útil al usuario tras cada petición en formato JSON. Idealmente, el objetivo de este API sería mostrar mediante una interfaz web, un diagrama del estado de la Estrella de la Muerte con las posiciones distribuidas en cuadrantes que representan los elementos construidos o en construcción.

### API Recursos

Este API desarrollado en Python sobre Flask utilizando la librería Connexion, que implementa el diseño de plantillas de Swagger, recibe peticiones GET y PUT en el puerto 5000, para leer y actualizar información de la base de datos en lo referente a recursos. Tanto la API Gateway como la API de construcción lanzan peticiones a esta API para mostrar al usuario los recursos de los que dispone como para comprobar antes de una construcción que existen recursos suficientes.

### API Construcción

El último API está también desarrollado en Python sobre Flask, recibe peticiones GET y PUT en el puerto 5001 para realizar lecturas y escrituras sobre la base de datos en lo referente a construcciones. Esta API, a su vez, se encarga mediante múltiples hilos de la simulación del retardo que supone la construcción. Una vez este API recibe una llamada, primero consulta el estado de construcción de ese cuadrante, y si es necesario, lo actualiza. Tras la actualización, utilizando la estructura Queue, encola un proceso con el retardo especificado su tipo de construcción en la cola correspondiente según si es cubierta o cañón. Finalmente, cuando el proceso es encolado y finaliza su tiempo de construcción, se realiza una consulta de actualización al servidor de base de datos para marcar la construcción de ese cuadrante como finalizada. Actualmente no se contempla notificar de vuelta al usuario tras la finalización de una construcción.

### Base de datos

El servidor de base de datos es de tipo MySQL, expuesto al puerto 3306, que contiene los datos de cada cuadrante de la Estrella de la Muerte y los recursos disponibles para realizar construcciones.
