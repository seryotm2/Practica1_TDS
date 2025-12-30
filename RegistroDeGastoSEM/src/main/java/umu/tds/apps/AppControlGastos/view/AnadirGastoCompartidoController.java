package umu.tds.apps.AppControlGastos.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AnadirGastoCompartidoController {

    @FXML private ComboBox<CuentaCompartida> comboCuentas;
    @FXML private ComboBox<Usuario> comboPagador;
    @FXML private TextField txtConcepto;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker datePickerFecha;

    @FXML private TableView<FilaParticipante> tablaReparto;
    @FXML private TableColumn<FilaParticipante, String> colNombre;
    @FXML private TableColumn<FilaParticipante, Double> colPorcentaje;

    private ObservableList<FilaParticipante> listaParticipantes;

    @FXML
    public void initialize() {
        datePickerFecha.setValue(LocalDate.now());
        listaParticipantes = FXCollections.observableArrayList();
        tablaReparto.setItems(listaParticipantes);
        
        configurarCombos();

        colNombre.setCellValueFactory(cd -> cd.getValue().nombreProperty());
        colPorcentaje.setCellValueFactory(cd -> cd.getValue().porcentajeProperty().asObject());
        colPorcentaje.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPorcentaje.setOnEditCommit(e -> e.getRowValue().setPorcentaje(e.getNewValue()));

        comboCuentas.setOnAction(e -> cargarParticipantes());
    }
    
    private void configurarCombos() {
         comboCuentas.setConverter(new StringConverter<>() {
            @Override public String toString(CuentaCompartida c) { return c==null?"":c.getNombre(); }
            @Override public CuentaCompartida fromString(String s) { return null; }
        });
        comboPagador.setConverter(new StringConverter<>() {
            @Override public String toString(Usuario u) { return u==null?"":u.getNombre(); }
            @Override public Usuario fromString(String s) { return null; }
        });
        comboCuentas.getItems().setAll(AppControlGastos.getInstancia().getCuentasCompartidas());
    }

    private void cargarParticipantes() {
        CuentaCompartida c = comboCuentas.getValue();
        if(c == null) return;
        
        listaParticipantes.clear();
        comboPagador.getItems().setAll(c.getParticipantes());
        
        double pct = 100.0 / c.getParticipantes().size();
        for(Usuario u : c.getParticipantes()) {
            listaParticipantes.add(new FilaParticipante(u, pct));
        }
    }

    @FXML
    public void guardarGastoCompartido(ActionEvent event) {
        CuentaCompartida cuenta = comboCuentas.getValue();
        Usuario pagador = comboPagador.getValue();
        String concepto = txtConcepto.getText();
        LocalDate fecha = datePickerFecha.getValue();
        String textoCantidad = txtCantidad.getText();

        if (cuenta == null || pagador == null || concepto == null || concepto.isEmpty() || fecha == null || textoCantidad.isEmpty()) {
            mostrarAlerta("Error", "Por favor, rellena todos los campos.");
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(textoCantidad);
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La cantidad debe ser un número positivo válido.");
            return;
        }

        Map<String, Double> mapaReparto = new HashMap<>();
        double sumaPorcentajes = 0.0;

        for (FilaParticipante fila : tablaReparto.getItems()) {
            double p = fila.getPorcentaje();
            sumaPorcentajes += p;
            if (fila.getUsuario() != null) {
                mapaReparto.put(fila.getUsuario().getNombre(), p);
            }
        }

        if (Math.abs(sumaPorcentajes - 100.0) > 0.1) {
            mostrarAlerta("Error de Porcentajes", "Los porcentajes deben sumar 100%.");
            return;
        }

        boolean exito = AppControlGastos.getInstancia().registrarGastoCompartido(
            cuenta, concepto, cantidad, fecha, pagador, mapaReparto
        );

        if (exito) {
            Stage stage = (Stage) txtConcepto.getScene().getWindow();
            stage.close();
        } else {
            mostrarAlerta("Error", "No se pudo registrar el gasto.");
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    @FXML public void cancelar(ActionEvent e) { ((Stage)txtCantidad.getScene().getWindow()).close(); }
}	