package umu.tds.apps.AppControlGastos.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Alerta;
import umu.tds.modeloNegocio.Gasto;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MenuPrincipalController {

    @FXML private Label lblGastoTotal;
    @FXML private Label lblGastoSemana;
    @FXML private Label lblGastoMes;
    // ---------------------------------------

    @FXML private ListView<String> listaUltimosGastos;

    @FXML
    public void initialize() {
        actualizarDatos(null);
    }

    @FXML
    public void actualizarDatos(ActionEvent event) {
        AppControlGastos controlador = AppControlGastos.getInstancia();
        
        double total = controlador.obtenerGastoTotal();
        lblGastoTotal.setText(String.format("%.2f €", total));

        double semanal = controlador.obtenerGastoSemanal();
        double mensual = controlador.obtenerGastoMensual();
        
        lblGastoSemana.setText(String.format("%.2f €", semanal));
        lblGastoMes.setText(String.format("%.2f €", mensual));
        List<Gasto> gastos = controlador.obtenerTodosLosGastos();
        listaUltimosGastos.getItems().clear();

        for (Gasto g : gastos) {
            String texto = String.format("%s | %s | %.2f €", 
                g.getFecha().toString(), 
                g.getConcepto(), 
                g.getCantidad()
            );
            listaUltimosGastos.getItems().add(0, texto);
        }
        
        List<Alerta> notificaciones = controlador.obtenerNotificaciones();
        
        if(!notificaciones.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();
            
            for (Alerta a : notificaciones) {
                mensaje.append("• ").append(a.getDescripcion()).append("\n");
            }
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso de Gasto");
            alert.setHeaderText("Has superado los siguientes límites:");
            alert.setContentText(mensaje.toString());
            
            alert.showAndWait();
        }
    }

    @FXML
    public void abrirNuevoGasto(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/anadir_gasto.fxml", "Nuevo Gasto Individual", event, true);
    }

    @FXML
    public void abrirNuevaCuenta(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/crear_cuenta.fxml", "Crear Cuenta Compartida", event, false);
    }

    @FXML
    public void abrirDirectorio(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/anadir_usuario.fxml", "Registrar Usuario", event, false);
    }

    @FXML
    public void abrirGastoCompartido(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/anadir_gasto_compartido.fxml", "Gasto Compartido", event, true);
    }
    
    public void abrirEliminar(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/eliminar_datos.fxml", "Gestión de Datos", event, true);
    }

    @FXML
    public void abrirImportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de gastos");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos de Texto/CSV", "*.csv", "*.txt"),
            new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            boolean exito = AppControlGastos.getInstancia().importarGastos(archivoSeleccionado.getAbsolutePath());
            if (exito) {
                mostrarAlerta("Importación Exitosa", "Se han cargado los nuevos gastos.");
                actualizarDatos(null);
            } else {
                mostrarAlerta("Error", "No se pudieron cargar los gastos.");
            }
        }
    }
    
    @FXML
    public void abrirBusqueda(ActionEvent event) {
    	abrirVentanaModal("/umu/tds/view/PanelDeBusqueda.fxml", "Panel de Búsqueda", event, false);
    }

    @FXML
    public void abrirCreditos(ActionEvent event) {
        cambiarEscena(event, "/umu/tds/view/creditos.fxml");
    }
    
    @FXML
    public void abrirCrearCategoria(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/crear_categoria.fxml", "Nueva Categoría", event, false);
    }
    
    @FXML
    public void abrirAlertas(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/ver_alertas.fxml", "Alertas/Notificaciones", event, false);
    }
    
    @FXML
    public void abrirCrearAlerta(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/crear_alerta.fxml", "Crear Nueva Alerta", event, false);
    }
    
    @FXML
    public void abrirEstadisticas(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/estadisticas.fxml", "Estadísticas y Gráficos", event, false);
    }
    
    @FXML
    public void abrirEstadoCuentas(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/estado_cuentas.fxml", "Estado de Cuentas y Deudas", event, false);
    }
    @FXML
    public void abrirTerminal(ActionEvent event) {
        abrirVentanaModal("/umu/tds/view/terminal.fxml", "Terminal de Comandos", event, true);
    }
    

    private void abrirVentanaModal(String fxml, String titulo, ActionEvent event, boolean actualizarAlVolver) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)event.getSource()).getScene().getWindow());
            
            if (actualizarAlVolver) {
                stage.showAndWait(); 
                actualizarDatos(null); 
            } else {
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cambiarEscena(ActionEvent event, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}