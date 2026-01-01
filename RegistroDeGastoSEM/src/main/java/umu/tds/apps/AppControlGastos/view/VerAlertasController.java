package umu.tds.apps.AppControlGastos.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Alerta;

public class VerAlertasController {

    @FXML private ListView<Alerta> listaNotificaciones;

    @FXML
    public void initialize() {
        cargarNotificaciones();
        
        listaNotificaciones.setCellFactory(param -> new ListCell<Alerta>() {
            @Override
            protected void updateItem(Alerta item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.getDescripcion());
                    
                    // Opcional: ponerlo en rojo negrita
                    setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                }
            }
        });
    }

    private void cargarNotificaciones() {
        listaNotificaciones.getItems().setAll(
            AppControlGastos.getInstancia().obtenerNotificaciones()
        );
    }

    @FXML
    public void borrarNotificacion(ActionEvent event) {
        Alerta seleccionada = listaNotificaciones.getSelectionModel().getSelectedItem();
        
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una notificación para borrarla.");
            return;
        }

        AppControlGastos.getInstancia().borrarNotificacion(seleccionada);
        
        cargarNotificaciones();
    }
    
    @FXML
    public void cerrar(ActionEvent event) {
        Stage stage = (Stage) listaNotificaciones.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}