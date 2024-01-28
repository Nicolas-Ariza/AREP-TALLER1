# HTTP SERVER - API REST (TALLER 1 AREP)

Taller 1 del curso de Arquitecturas Empresariales (AREP) en el que se implementa un servidor HTTP de cero en Java para crear una fachada que atienda consultas de clientes desde un browser hacia un API de peliculas.

## Diseño
Este proyecto funciona y esta organizado en diferentes componentes con responsabilidades unicas.

1. `APIConnection` este componente se encarga de establecer conexión y comunicación con la API [OMDb API](https://www.omdbapi.com). Además es quien administra el caché de consultas que los clientes realizan.
2. `HTTPResponseData` y `HTTPResponseHeaders` dos componentes auxiliares cuya función principal es ayudar al servidor a generar los respectivos encabezados y datos de respuesta cuando el cliente los solicite.
3. `HTTPServer` componente principal del proyecto encargado de administrar el servicio HTTP y de atender las solicitudes de conexión de los clientes.

## Extensión e implementación de otros servicios

Aún existen algunas funcionalidades a tener en cuenta para futuras versiones de este proyecto:

1. El componente encargado de generar encabezados puede extenderse para agregar aún más códigos de respuesta a diferencia de los que se encuentran actualmente (200 y 404) esto con el fin de cubrir todos los espectros de prueba que el usuario pueda generar.

2. El componente encargado de generar datos para las consultas HTTP podría extenderse para implementar aún más funcionalidades dentro del dominio, por ejemplo, implementar un path para el uso del método POST hacia la API.

3. El componente que se encarga de las solicitudes HTTP podría implementar un apartado de filtros con los que ya cuenta el API [OMDb API](https://www.omdbapi.com) por defecto para hacer así más amena la experiencia de usuario con la aplicación.

4. En lugar de implementar un caché de manera local, se podría implementar un servicio de terceros como lo es [Redis](https://redis.io) para almacenar las consultas de los clientes de manera más eficiente y segura.



## Instrucciones de uso

### Pre-requisitos

Antes de ejecutar el servidor es necesario contar con los siguientes programas instalados y funcionando:

1. JDK (Java Development Kit)
2. MVN (Maven)

### Instalación y Ejecución

A continuación se muestra el paso a paso de como instalar y ejecutar el servidor HTTP

1. Clone este repositorio localmente en un entorno o carpeta de trabajo.

```
$ git clone https://github.com/NickArB/AREP-TALLER1.git
```

2. Dentro del entorno o directorio en el que clono el proyecto, asegurese de que no existan ejecutables previos o no deseados con maven.

```
$ mvn clean
```
3. Una vez que los targets han sido descartados compilelos y re asignelos al target
```
$ mvn package
```
4. Con los target asignados, ejecute el metodo main de la clase HTTPServer. Dependiendo de su IDE esta clase se puede ejecutar de varias formas, en caso de no tener un IDE se recomienda el uso del siguiente comando
```
$ java '-XX:+ShowCodeDetailsInExceptionMessages' -cp '<Path-al-directorio-de-trabajo>\target\classes' 'edu.escuelaing.app.taller.HTTPServer'
```
5. Una vez el servicio esta corriendo puede verificar que esta funcionando al escribir la ruta en el navegador
```
http://localhost:35000/
```
A continuación se muestra la interfaz de usuario en el browser junto a una consulta.

![Alt text](images\sample1.png)
![Alt text](images\sample2.png)


## Ejecutando pruebas unitarias

A continuación se presenta como ejecutar las pruebas unitarias
1. Ubiquese en el directorio o entorno de trabajo desde su terminal.
```
$ cd <Path-al-directorio-de-trabajo>
```
2. Utilice maven para ejecutar las pruebas.
```
$ mvn test
```
3. Maven automáticamente detectara y ejecutara todas las pruebas unitarias. Debería aparecer algo similar a esto:
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running edu.escuelaing.app.HTTPResponseDataTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.029 s -- in edu.escuelaing.app.HTTPResponseDataTest
[INFO] Running edu.escuelaing.app.HTTPResponseHeadersTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in edu.escuelaing.app.HTTPResponseHeadersTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.019 s
[INFO] Finished at: 2024-01-28T16:27:49-05:00
[INFO] ------------------------------------------------------------------------
``` 

## Construido con

* [Maven](https://maven.apache.org/) - Manejo de dependencias

## Version 1.0

## Autor

* **Nicolás Ariza Barbosa**