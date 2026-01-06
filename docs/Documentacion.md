# Practica1_TDS - Documentación

## Introduccion

## Arquitectura de la aplicación, decisiones de diseño y patrones de diseño usados.

### Vista y controlador principal
RG:SEM es una aplicación de registro de datos de gasto que consiste en un interfaz gráfica soportada por JavaFX, la cual utiliza un controlador para mostrar ventanas que contienen botones que llaman al controlador principal de la aplicación.  
Este controlador tiene como tareas crear gastos, crear usuarios, crear cuentas compartidas, crear categorias, crear alertas, buscar categorias e importar ficheros de gastos. Utiliza el patrón adaptador ya que los controladores de la vista tienen una visión general de estas tareas, y es una clase Singleton.
La importación de datos funciona mediante un patrón adaptador para independizarse del formato de fichero, utilizando también un método factoria para crear las clases importadoras. De momento la aplicación solo soporta formato CSV, pero se podría ampliar a otros.
Además existen 3 repositorios para los usuarios, los diferentes tipos de gastos y las alertas, que utilizan también el patron adaptador.

### Gastos, categorias y cuentas compartidas
Para la gestion y clasificación de gastos, gestion y busqueda de categorias y alamcenamiento de cuentas compartidas, decidimos crear una clase llamada Libro de Cuentas (Siendo esta clase Singleton). Esta clase tiene acceso a un repositorio para el almacenamiento persistente de los datos que maneja. Las categorias son listas de gastos y los gastos tienen una referencia a su lista perteneciente. Las cuentas compartidas no son definidas con un porcentaje de reparto del gasto fijo, sino que cada gasto compartido define su propio porcentaje. Las búsquedas de categorias fueron implementadas con un patron decorador para facilitar las busquedas con criterios variables.

### Usuarios
La clase Directorio gestiona usuarios y tiene acceso al repositorio de usuarios. Es una clase singleton.

### Alertas
La clase gestorAlertas se encarga de controlar las alertas y las notificaciones generadas por las mismas, ademas del acceso a su correspondiente repositorio. Esta clase utiliza el patron Singleton también. Las notificaciones se activan cuando un gasto notifica por patron observer al gestor de alertas. Las alertas se diferencian en semanales y mensuales mediante un patron Estrategia.

## Diagrama de clases del dominio del proyecto.

## Diagrama de interacción para la historia de usuario 1.

### Registro de usuarios  
**Como** usuario  
**Quiero** crear diferentes usuarios  
**Para** gestionar los gastos grupales.

**Criterio de aceptación:**
La aplicación permite registrar personas por nombre y correo (opcional) y añadirlas a la lista de contribuyentes. La aplicación muestra un mensaje de registro exitoso.

![Diagrama](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/Diagrama%20de%20interaccion.png)
