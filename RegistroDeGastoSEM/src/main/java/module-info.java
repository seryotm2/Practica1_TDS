module umu.tds.apps.RegistroDeGastoSEM {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind; 
    requires com.fasterxml.jackson.datatype.jsr310;
    
    exports umu.tds.apps.AppControlGastos to javafx.graphics;

    opens umu.tds.apps.AppControlGastos.view to javafx.fxml;
    
    opens umu.tds.modeloNegocio to com.fasterxml.jackson.databind;
    exports umu.tds.modeloNegocio;
}