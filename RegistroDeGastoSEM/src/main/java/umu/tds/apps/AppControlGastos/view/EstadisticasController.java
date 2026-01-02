package umu.tds.apps.AppControlGastos.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import umu.tds.controlador.AppControlGastos;
import umu.tds.modeloNegocio.Gasto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticasController {

    @FXML private ComboBox<String> comboPeriodo;
    @FXML private ComboBox<String> comboTipoGrafico;
    @FXML private BarChart<String, Number> graficoBarras;
    @FXML private PieChart graficoCircular;

    @FXML
    public void initialize() {
        comboPeriodo.setItems(FXCollections.observableArrayList(
            "Esta Semana", "Este Mes", "Histórico Total"
        ));
        comboPeriodo.getSelectionModel().select("Este Mes"); 

        comboTipoGrafico.setItems(FXCollections.observableArrayList(
            "Barras", "Circular (Quesito)"
        ));
        comboTipoGrafico.getSelectionModel().select("Barras");

        comboPeriodo.setOnAction(e -> cargarDatosGraficos());
        comboTipoGrafico.setOnAction(e -> actualizarVisibilidad());

        actualizarVisibilidad();
        cargarDatosGraficos();
    }

    private void actualizarVisibilidad() {
        String seleccion = comboTipoGrafico.getSelectionModel().getSelectedItem();
        
        if ("Barras".equals(seleccion)) {
            graficoBarras.setVisible(true);
            graficoCircular.setVisible(false);
        } else {
            graficoBarras.setVisible(false);
            graficoCircular.setVisible(true);
        }
    }

    private void cargarDatosGraficos() {
        AppControlGastos controlador = AppControlGastos.getInstancia();
        String periodoSeleccionado = comboPeriodo.getSelectionModel().getSelectedItem();
        List<Gasto> listaGastos;

        switch (periodoSeleccionado) {
            case "Esta Semana":
                listaGastos = controlador.obtenerGastosSemana();
                break;
            case "Este Mes":
                listaGastos = controlador.obtenerGastosMes();
                break;
            default: 
                listaGastos = controlador.obtenerTodosLosGastos();
                break;
        }

        Map<String, Double> gastosPorCategoria = new HashMap<>();
        
        for (Gasto g : listaGastos) {
            String nombreCat = g.getCategoria().getNombreCategoria();
            double cantidad = g.getCantidad();
            
            gastosPorCategoria.put(nombreCat, gastosPorCategoria.getOrDefault(nombreCat, 0.0) + cantidad);
        }

        graficoBarras.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Gastos en " + periodoSeleccionado);
        
        for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        graficoBarras.getData().add(series);

        graficoCircular.getData().clear();
        ObservableList<PieChart.Data> datosCircular = FXCollections.observableArrayList();
        
        for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
            String etiqueta = entry.getKey() + " (" + String.format("%.2f", entry.getValue()) + "€)";
            datosCircular.add(new PieChart.Data(etiqueta, entry.getValue()));
        }
        graficoCircular.setData(datosCircular);
    }

    @FXML
    public void cerrar(ActionEvent event) {
        Stage stage = (Stage) comboPeriodo.getScene().getWindow();
        stage.close();
    }
}