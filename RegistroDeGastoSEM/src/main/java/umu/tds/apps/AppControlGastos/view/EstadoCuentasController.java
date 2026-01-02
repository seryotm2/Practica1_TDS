package umu.tds.apps.AppControlGastos.view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.CuentaCompartida;
import umu.tds.modeloNegocio.Gasto;
import umu.tds.modeloNegocio.GastoCompartido;
import umu.tds.modeloNegocio.Usuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadoCuentasController {

    @FXML private ComboBox<CuentaCompartida> comboGrupos;
    @FXML private ListView<String> listaBalances;
    @FXML private Label lblTotalGrupo;

    @FXML
    public void initialize() {
        List<CuentaCompartida> cuentas = AppControlGastos.getInstancia().getCuentasCompartidas();
        comboGrupos.setItems(FXCollections.observableArrayList(cuentas));

        comboGrupos.setConverter(new StringConverter<CuentaCompartida>() {
            @Override
            public String toString(CuentaCompartida c) {
                return (c != null) ? c.getNombre() : "";
            }
            @Override
            public CuentaCompartida fromString(String string) {
                return null;
            }
        });

        comboGrupos.setOnAction(e -> calcularBalances());
    }

    private void calcularBalances() {
        CuentaCompartida grupo = comboGrupos.getSelectionModel().getSelectedItem();
        if (grupo == null) return;

        listaBalances.getItems().clear();
        Map<Usuario, Double> balances = new HashMap<>();
        double totalGastadoGrupo = 0;

        for (Usuario u : grupo.getParticipantes()) {
            balances.put(u, 0.0);
        }

        List<Gasto> todosLosGastos = AppControlGastos.getInstancia().obtenerTodosLosGastos();
        
        for (Gasto g : todosLosGastos) {
            if (g instanceof GastoCompartido) {
                GastoCompartido gc = (GastoCompartido) g;
                
                if (gc.getCuenta() == grupo.getId()) { 
                    
                    double monto = gc.getCantidad();
                    totalGastadoGrupo += monto;
                    Usuario pagador = gc.getUsuario();

                    balances.put(pagador, balances.getOrDefault(pagador, 0.0) + monto);

                    Map<String, Double> porcentajes = gc.getPorcentajes(); 
                    
                    if (porcentajes != null && !porcentajes.isEmpty()) {
                        for (Usuario u : grupo.getParticipantes()) {
                             Double porcentaje = porcentajes.getOrDefault(u.getNombre(), 0.0); 
                             
                             double cuota = monto * (porcentaje / 100.0);
                             balances.put(u, balances.get(u) - cuota);
                        }
                    } else {
                        double cuota = monto / grupo.getParticipantes().size();
                        for (Usuario u : grupo.getParticipantes()) {
                            balances.put(u, balances.get(u) - cuota);
                        }
                    }
                }
            }
        }

        lblTotalGrupo.setText(String.format("Total Movido: %.2f €", totalGastadoGrupo));

        for (Map.Entry<Usuario, Double> entry : balances.entrySet()) {
            double saldo = entry.getValue();
            String nombre = entry.getKey().getNombre();
            String texto;
            
            if (saldo > 0.01) {
                texto = String.format("%s: LE DEBEN %.2f €", nombre, Math.abs(saldo));
            } else if (saldo < -0.01) {
                texto = String.format("%s: DEBE %.2f €", nombre, Math.abs(saldo));
            } else {
                texto = String.format("%s: - (0.00 €)", nombre);
            }
            listaBalances.getItems().add(texto);
        }
        
        listaBalances.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("LE DEBEN")) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else if (item.contains("DEBE")) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
    }

    @FXML
    public void cerrar(ActionEvent event) {
        Stage stage = (Stage) comboGrupos.getScene().getWindow();
        stage.close();
    }
}