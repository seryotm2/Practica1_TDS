package umu.tds.apps.AppControlGastos.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;

public class CrearAlertaController {

    @FXML private ComboBox<String> comboTipo;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private TextField txtLimite;

    @FXML
    public void initialize() {
        comboTipo.getItems().addAll("Mensual", "Semanal");
        comboTipo.getSelectionModel().selectFirst();
        
        comboCategoria.getItems().add("Todas");
        comboCategoria.getItems().addAll(AppControlGastos.getInstancia().obtenerNombresCategorias());
        comboCategoria.getSelectionModel().selectFirst();
    }

    @FXML
    public void crear() {
        try {
            double limite = Double.parseDouble(txtLimite.getText());
            String tipo = comboTipo.getValue();
            String cat = comboCategoria.getValue();
            
            if(cat.equals("Todas")) cat = null; 
            
            boolean ok = AppControlGastos.getInstancia().crearAlerta(tipo, limite, cat);
            
            if(ok) {
                mostrarAlerta("Éxito", "Alerta creada. Te avisaremos si superas el límite.");
                cancelar();
            }
        } catch(NumberFormatException e) {
            mostrarAlerta("Error", "Introduce un número válido.");
        }
    }
    
    @FXML public void cancelar() {
        ((Stage) txtLimite.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t); a.setContentText(m); a.showAndWait();
    }
}