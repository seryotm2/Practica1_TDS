package umu.tds.apps.AppControlGastos.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Gasto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TerminalController {

    @FXML private TextArea txtSalida;
    @FXML private TextField txtEntrada;

    private List<Gasto> listaVisible = new ArrayList<>();

    @FXML
    public void initialize() {
        imprimir(">> RG:SEM");
        imprimir(">> Comandos: comandos, listar, crear, borrar, modificar, salir");
        imprimir("--------------------------------------------------");
    }

    @FXML
    public void procesarEntrada(ActionEvent event) {
        String texto = txtEntrada.getText();
        if (texto.isEmpty()) return;

        imprimir("> " + texto); 
        txtEntrada.clear();

        String[] partes = texto.split(" ");
        String comando = partes[0].toLowerCase();

        try {
            switch (comando) {
                case "listar":
                    mostrarLista();
                    break;
                case "crear":
                    crearNuevo(partes);
                    break;
                case "borrar":
                    borrarGasto(partes);
                    break;
                case "modificar":
                    modificarGasto(partes);
                    break;
                case "salir":
                    cerrarVentana();
                    break;
                case "comandos":
                	imprimir(">> Comandos: comandos, listar, crear, borrar, modificar, salir");
                    break;
                default:
                    imprimir("Comando no reconocido.");
                    imprimir(">> Comandos: comandos, listar, crear, borrar, modificar, salir");
            }
        } catch (Exception e) {
            imprimir("Error: " + e.getMessage());
        }
    }

    private void imprimir(String mensaje) {
        txtSalida.appendText(mensaje + "\n");
    }

    private void mostrarLista() {
        listaVisible = AppControlGastos.getInstancia().obtenerTodosLosGastos();
        
        if (listaVisible.isEmpty()) {
            imprimir("No hay gastos.");
            return;
        }

        imprimir("--- TUS GASTOS ---");
        for (int i = 0; i < listaVisible.size(); i++) {
            Gasto g = listaVisible.get(i);
            imprimir("#" + (i + 1) + " | " + g.getConcepto() + " | " + g.getCantidad() + "€ | " + g.getFecha());
        }
    }

    private void crearNuevo(String[] datos) {
        if (datos.length < 5) {
            imprimir("Uso: crear <Concepto> <Cantidad> <Fecha: yyyy-mm-dd> <Categoria>");
            return;
        }

        String concepto = datos[1];
        double cantidad = Double.parseDouble(datos[2]);
        LocalDate fecha = LocalDate.parse(datos[3]);
        String categoria = datos[4];

        boolean ok = AppControlGastos.getInstancia().registrarGasto(concepto, categoria, cantidad, fecha);
        
        if (ok) imprimir("Gasto guardado.");
        else imprimir("Error al guardar (revisa si la categoría existe).");
    }

    private void borrarGasto(String[] datos) {
        if (datos.length < 2) {
            imprimir("Uso: borrar <NumeroDeLista>");
            return;
        }

        int numero = Integer.parseInt(datos[1]); 
        int indiceReal = numero - 1;             

        if (indiceReal >= 0 && indiceReal < listaVisible.size()) {
            Gasto g = listaVisible.get(indiceReal);
            AppControlGastos.getInstancia().eliminarGasto(g);
            imprimir("Gasto eliminado.");
        } else {
            imprimir("Número incorrecto. Usa el comando 'listar' primero.");
        }
    }

    private void modificarGasto(String[] datos) {
        if (datos.length < 6) {
            imprimir("Uso: modificar <Numero> <Concepto> <Cantidad> <Fecha: yyyy-mm-dd> <Categoria>");
            return;
        }

        int numero = Integer.parseInt(datos[1]);
        int indiceReal = numero - 1;

        if (indiceReal >= 0 && indiceReal < listaVisible.size()) {
            Gasto gastoOriginal = listaVisible.get(indiceReal);
            
            String nuevoConcepto = datos[2];
            double nuevaCantidad = Double.parseDouble(datos[3]);
            LocalDate nuevaFecha = LocalDate.parse(datos[4]);
            String nuevaCat = datos[5];

            AppControlGastos.getInstancia().modificarGasto(
                gastoOriginal, nuevoConcepto, nuevaCantidad, nuevaFecha, nuevaCat
            );
            imprimir("Gasto modificado.");
        } else {
            imprimir("Número incorrecto.");
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtEntrada.getScene().getWindow();
        stage.close();
    }
}