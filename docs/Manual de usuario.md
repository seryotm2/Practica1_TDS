# Manual de usuario

## 1. Introducción
Esta aplicación permite seguir los gastos realizados en ciertos periodos de tiempo, repartirlos entre grupos de usuarios, y alertar al usuario si ha gastado más de lo establecido. Permite crear gastos, clasificarlos en categorias, crear grupos de usuarios, alertas, importar ficheros de gastos y ver el registro de todos estos datos en gráficos.

## 2. Acceso a la aplicación
Ejecute eclipse y abra el proyecto. Desde la clase **Main.java** pulse el botón verde de play en la parte superior izquierda para ejecutar el programa.

![Ejecutar](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/Ejecutar.png)

Se abrirá el menú principal, que deberia verse así:
![MenuPrincipal](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/MenuPrincipal.png)


## 3. Funcionalidades principales

### 1. Gastos
Para crear un gasto utilizaremos el botón de **Nuevo Gasto**. Nos aparecerá una ventana como la vista en la imagen. Aquí debemos indicar un concepto, una categoría que debe ser creada previamente (por defecto existe la categoria Gastos Generales), una cantidad en euros (debe usarse el punto para los céntimos, no la coma) y una fecha seleccionable mediante el calendario desplegable.

![AñadirGasto](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/AñadirGasto.png)

Una vez guardado, aparecerá en el historial de movimientos y el gasto se verá reflejado en el gasto total y, si corresponde, en el mensual o semanal.

Ahora podemos modificarlo o eliminarlo mediante el botón Gestion/Eliminar. Debemos seleccionar el gasto que nos interesa. Si lo modificamos, aparecerá una ventana similar a la de añadir gasto, y si lo eliminamos se borrará del historial y de la suma total de gastos.

![ModGasto](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/ModElimGasto.png)

### 2. Categorias
Para crear una categoria existe el botón **Crear Categoría**, que solo pide el nombre de la categoría. Ahora la categoría creada es seleccionable al crear un gasto.

![CrearCategoria](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/CrearCategoria.png)

### 3. Usuarios
Para crear usuarios debemos hacerlo con el botón **Añadir Usuario** y especificar un nombre y un email.

![CrearUsuario](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/CrearUsuario.png)

### 4. Grupos
Para crear un grupo o cuenta compartida con el botón **Crear Grupo** indicaremos un nombre de grupo y añadiremos usuarios creados previamente seleccionándolos en el desplegable y confirmándolos con **Añadir a la lista**.

![CrearGrupo](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/CrearGrupo.png)

### 5. Gastos grupales
Para añadir un gasto a un grupo, mediante el botón **Añadir Gasto a Grupo** debemos seleccionar un grupo, un usuario que haya pagado del grupo (el usuario de la aplicación siempre está dentro de todos los grupos), un concepto, una categoria, la cantidad de gasto, la fecha y un reparto del porcentaje a pagar por miembro. Para repartir los porcentajes editamos los números de la tabla de costes.

![CrearGastoGrupo](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/AñadirGastoGrupal.png)

### 6. Importar
Con el botón **Importar** puedes añadir gastos a partir de un fichero csv con formato (Fecha(dd/mm/yyyy hh:mm), Cuenta (personal o grupo), Categoria, Subcategoria, Concepto, Quien paga, Cantidad, Divisa(EUR)).

### 7. Alertas y notificaciones
Con el botón **Configurar alerta** podemos crear una alerta que nos notifica cuando sobrepasamos un gasto. Esta alerta puede ser mensual o semanal, y puede estar atada a una categoría. Indicamos el tipo de alarma, categoría y límite de gasto para crearla.

![Alertas](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/Alertas.png)

Con el boton **Alertas/Notificaciones** podemos eliminar alertas o notificaciones de alertas si asi lo deseamos

![CentroDeAlertas](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/Centro%20de%20Alertas.png)


### 8. Linea de comandos
El botón linea de comandos nos permite crear, modificar, listar y eliminar gastos simples por una terminal. Los comandos disponibles son:

- Comandos: Muestra los comandos.
- Listar: Muestra todos los gastos con un id numerico.
- Crear: Crea un gasto (Uso: crear <Concepto> <Cantidad> <Fecha: yyyy-mm-dd> <Categoria>).  
- Borrar: Borra un gasto (Uso: borrar <NumeroDeLista>).
- Modificar: Modifca un gasto (Uso: modificar <Numero> <Concepto> <Cantidad> <Fecha: yyyy-mm-dd> <Categoria>).
- Salir: Sale de la terminal.

![Terminal](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/Terminal.png)

### 9. Ver
Podemos ver estadisticas de los gastos mediante graficos para ver los gastos en la ultima semana, mes o en general, estos se filtran por categoria. Se pueden poner como grafico de barras o circular.

![GBarras](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/Estadisticas.png)
![GCircular](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/Circular.png)

Además con el botón **ver Deudas de Grupo** podemos ver grupo por grupo el estado de los pagos.
![GrupoEstadisticas](https://github.com/seryotm2/Practica1_TDS/blob/main/docs/imagenes/GrupoEstadisticas.png)

### 10. Adicionales
- Actualizar Lista: Actualiza la lista a los nuevos cambios si no se ha actualizado previamente.
- Filtrar: Permite filtrar el historial de movimientos por rango de fechas, cantidad, categoria y/o concepto.
