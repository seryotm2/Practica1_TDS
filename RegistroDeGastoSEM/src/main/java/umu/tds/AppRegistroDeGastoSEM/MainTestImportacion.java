package umu.tds.AppRegistroDeGastoSEM;

import java.util.List;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Gasto;
import umu.tds.modeloNegocio.LibroDeCuenta;

public class MainTestImportacion {

    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE IMPORTACIÓN DE GASTOS ===");

        // 1. Instanciamos el controlador
        AppControlGastos controller = AppControlGastos.getInstancia();
        
        // 2. Ruta del archivo CSV (La que me has facilitado)
        String rutaArchivo = "/home/sergio/Descargas/EjemploDatosGasto.csv";
        
        System.out.println("Intentando importar desde: " + rutaArchivo);
        System.out.println("Gasto Global antes de importar: " + LibroDeCuenta.getInstancia().getGastoGlobal() + "€");
        
        // 3. Ejecutamos la importación
        boolean exito = controller.importarGastos(rutaArchivo);
        
        if (exito) {
            System.out.println("\n✅ IMPORTACIÓN EXITOSA.");
        } else {
            System.err.println("\n❌ FALLO EN LA IMPORTACIÓN.");
            System.err.println("Asegúrate de que la ruta es correcta y el archivo tiene formato CSV.");
            // Continuamos para ver si se importó algo parcialmente
        }

        // 4. Verificación de resultados
        // Usamos getUltimosNGastos para recuperar todo lo que hay en el sistema (ponemos un número alto)
        List<Gasto> todosLosGastos = LibroDeCuenta.getInstancia().getUltimosNGastos(100);
        
        System.out.println("\n--- LISTADO DE GASTOS EN EL SISTEMA (" + todosLosGastos.size() + ") ---");
        
        if (todosLosGastos.isEmpty()) {
            System.out.println("(No hay gastos registrados)");
        } else {
            for (Gasto g : todosLosGastos) {
                System.out.println("------------------------------------------------");
                System.out.println("Fecha:    " + g.getFecha());
                System.out.println("Concepto: " + g.getConcepto());
                System.out.println("Cantidad: " + g.getCantidad() + "€");
                System.out.println("Categoría:" + g.getCategoria().getNombreCategoria());
                System.out.println("Autor:    " + g.getUsuario().getNombre());
            }
        }
        
        System.out.println("\n------------------------------------------------");
        System.out.println("Gasto Global Final (Mis Pagos): " + LibroDeCuenta.getInstancia().getGastoGlobal() + "€");
        System.out.println("================================================");
    }
}