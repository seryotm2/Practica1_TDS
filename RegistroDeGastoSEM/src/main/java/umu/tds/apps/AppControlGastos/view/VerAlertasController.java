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
    @FXML private ListView<Alerta> listaAlertasActivas;

    @FXML
    public void initialize() {
        configurarRenderizado(listaNotificaciones, true);
        configurarRenderizado(listaAlertasActivas, false);
        cargarListas();
    }
    
    private void configurarRenderizado(ListView<Alerta> lista, boolean esAlertaDisparada) {
        lista.setCellFactory(param -> new ListCell<Alerta>() {
            @Override
            protected void updateItem(Alerta item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.getDescripcion());
                    if (esAlertaDisparada) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
    }

    private void cargarListas() {
        listaNotificaciones.getItems().setAll(
            AppControlGastos.getInstancia().obtenerNotificaciones()
        );
        
        listaAlertasActivas.getItems().setAll(
            AppControlGastos.getInstancia().obtenerAlertas()
        );
    }

    @FXML
    public void borrarNotificacion(ActionEvent event) {
        Alerta seleccionada = listaNotificaciones.getSelectionModel().getSelectedItem();
        
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona un aviso de la lista superior para borrarlo.");
            return;
        }

        AppControlGastos.getInstancia().borrarNotificacion(seleccionada);
        cargarListas();
    }
    
    @FXML
    public void borrarAlertaConfigurada(ActionEvent event) {
        Alerta seleccionada = listaAlertasActivas.getSelectionModel().getSelectedItem();
        
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una regla de la lista inferior para eliminarla.");
            return;
        }

        AppControlGastos.getInstancia().borrarAlertaConfigurada(seleccionada);
        cargarListas();
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