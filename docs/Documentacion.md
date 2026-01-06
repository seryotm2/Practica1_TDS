# Practica1_TDS - Documentación

## Introduccion

## Arquitectura de la aplicación, decisiones de diseño y patrones de diseño usados.
 Para la gestion de los gastos, sus categorias y las cuentas compartidas existe una clase llamada Libro de Cuentas. Esta clase tiene acceso a un repositorio para el almacenamiento persistente de los datos que maneja. De la misma forma funciona la clase Directorio, que gestiona usuarios y tiene acceso al repositorio de usuarios, y la clase gestorAlertas se encarga de las alertas y las notificaciones generadas por las mismas, ademas del acceso a su correspondiente repositorio. El controlador principal puede también importar gastos mediante las clases dedicadas a la importacion de ficheros, y buscar categorias 

### Vista y controlador principal
RG:SEM es una aplicación de registro de datos de gasto que consiste en un interfaz gráfica soportada por JavaFX, la cual utiliza un controlador para mostrar ventanas que contienen botones que llaman al controlador principal de la aplicación.  
Este controlador tiene como tareas crear gastos, crear usuarios, crear cuentas compartidas, crear categorias, crear alertas, buscar categorias e importar ficheros de gastos. Utiliza el patrón adaptador ya que los controladores de la vista tienen una visión general de estas tareas, y es una clase Singleton.
La importación de datos funciona mediante un patrón adaptador para independizarse del formato de fichero, utilizando también un método factoria para crear las clases importadoras. De momento la aplicación solo soporta formato CSV, pero se podría ampliar a otros.



### Gastos, categorias y cuentas compartidas
- Categorias son una lista de gastos y los gastos tienen una referencia a la lista
- Objeto Libro de cuentas que clasifica los gastos, inicia busquedas y almacena cuentas compartidas
- En cuentas compartidas no existe un porcentaje fijo, cada gasto define su propio porcentaje
- Busquedas implementadas con decorador para criterio variable
Decorador - Buscador
Singleton - Libro cuenta, directorio, appcontrolgasto, gestorAlertas
Adaptador - repositorios

### Usuarios
Singleton - Libro cuenta, directorio, appcontrolgasto, gestorAlertas
Adaptador - repositorios

### Alertas
- Alertas utiliza observer
Estrategia - alertas
Singleton - Libro cuenta, directorio, appcontrolgasto, gestorAlertas
Observer - notificar alertas
Adaptador - repositorios




## Diagrama de clases del dominio del proyecto.

## Diagrama de interacción para la historia de usuario 1.

### Registro de usuarios  
**Como** usuario  
**Quiero** crear diferentes usuarios  
**Para** gestionar los gastos grupales.

**Criterio de aceptación:**
La aplicación permite registrar personas por nombre y correo (opcional) y añadirlas a la lista de contribuyentes. La aplicación muestra un mensaje de registro exitoso.

![Diagrama](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/Diagrama%20de%20interaccion.png)
