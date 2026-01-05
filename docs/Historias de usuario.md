# Historias de usuario
## 1. Registro de usuarios

- Crear usuario (nombre y correo).

- Añadir a lista de usuarios.

## 2. Crear categorías

- Nombre de categoría.

## 3. Registro y gestión de gastos

- Concepto.

- Usuario.

- Cantidad.

- Fecha.

- Categoría.

- Edición y eliminación.

## 4. Visualización avanzada

- Creación de gráficas.

- Mostrar en calendario.

## 5. Buscar gastos

- Filtrar por categoría, cantidad y rango de fecha.

- Búsqueda por concepto.

- Búsqueda por combinación de filtros.

## 6. Sistema de alertas

- Crear alertas.

- Límite de gasto.

- Vincular límite a categoría o lapso de tiempo.

- Historial de notificaciones.

## 7. Crear cuentas de gasto compartidas

- Crear cuentas de gasto compartidas (nombre de la cuenta y categoría).

- Añadir contribuyentes.

- Fijar importe.

- Distribución (por defecto equitativo).

- Saldo pendiente por usuario.

## 8. Definir el porcentaje de gasto asumido

- Indicar porcentaje de gasto asumido.

## 9. Importar datos de gasto de fuentes externas

- Importar documentos de texto con ciertos formatos.

- Registrar los gastos del archivo.

# Como, quiero y para
## 1 Crear usuario

**Como** usuario  
**Quiero** crear diferentes usuarios  
**Para** gestionar los gastos grupales.

*Criterio de aceptación:*  
La aplicación permite registrar personas por nombre y correo (opcional) y añadirlas a la lista de contribuyentes. La aplicación muestra un mensaje de registro exitoso.

---

## 2. Crear categorías

**Como** usuario  
**Quiero** crear diferentes categorías de gastos  
**Para** ordenar y filtrar mejor los diferentes gastos.

*Criterio de aceptación:*  
La aplicación permite crear categorías y añadirlas a un catálogo. La aplicación muestra un mensaje de registro exitoso.

---

## 3.1 Registro de gastos

**Como** usuario  
**Quiero** indicar los diferentes gastos proporcionando un concepto, una cantidad y una categoría  
**Para** llevar el control de las finanzas.

*Criterio de aceptación:*  
La aplicación permite crear gastos y añadirlos a una categoría. La aplicación muestra un mensaje de registro exitoso y un resumen del registro con el concepto, cantidad, fecha y hora del registro.

---

## 3.2 Gestión de gastos

**Como** usuario  
**Quiero** editar o eliminar los gastos  
**Para** modificarlos o eliminarlos en caso de error.

*Criterio de aceptación:*  
La aplicación permite modificar los datos de un gasto. La aplicación muestra un resumen del registro modificado con el concepto, cantidad, fecha y hora del registro.

---

## 4. Creación de gráficas y calendario

**Como** usuario  
**Quiero** tener una interfaz más amigable  
**Para** ver de manera más sencilla los gastos.

*Criterio de aceptación:*  
La aplicación permite ver los gastos en forma de gráficas. Pueden verse ordenados por fecha o por concepto.

---

## 5. Búsqueda de gastos

**Como** usuario  
**Quiero** buscar mis gastos  
**Para** poder encontrarlos más cómodamente.

*Criterio de aceptación:*  
La aplicación permite buscar los gastos por concepto, categoría o rango de fecha.

---

## 6.1 Sistema de alertas

**Como** usuario  
**Quiero** crear alertas aportando un límite de gasto y opcionalmente una categoría asociada  
**Para** estar pendiente de mis gastos.

*Criterio de aceptación:*  
La aplicación da alertas en forma de notificaciones avisando del sobrepaso de presupuesto. La aplicación muestra un resumen de la alerta creada con éxito.

---

## 6.2 Historial de notificaciones

**Como** usuario  
**Quiero** tener una lista de notificaciones  
**Para** ver en todo momento las alertas activadas.

*Criterio de aceptación:*  
La aplicación permite almacenar y mostrar las diferentes alertas, incluso si ya han expirado.

---

## 7. Crear cuentas de gasto compartidas

**Como** usuario  
**Quiero** crear una cuenta compartida  
**Para** compartir gastos entre múltiples usuarios.

*Criterio de aceptación:*  
La aplicación permite añadir personas para colaborar en un gasto. La aplicación debe informar si la persona que se quiere añadir no existe en la lista de usuarios.

---

## 8. Definir el porcentaje de gasto asumido

**Como** usuario  
**Quiero** definir un porcentaje de gasto asumido  
**Para** pagar lo correspondiente individualmente en gastos compartidos.

*Criterio de aceptación:*  
La aplicación permite especificar el porcentaje de gasto en las diferentes cuentas de gasto compartidas.

---

## 9. Importar datos de gasto de fuentes externas

**Como** usuario  
**Quiero** importar datos de gastos a partir de ficheros externos  
**Para** transferir automáticamente información desde documentos a la aplicación.

*Criterio de aceptación:*  
La aplicación permite guardar en forma de gastos la información contenida en archivos que cumplan formatos específicos.
