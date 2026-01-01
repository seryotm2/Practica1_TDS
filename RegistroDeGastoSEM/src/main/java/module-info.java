module umu.tds.apps.RegistroDeGastoSEM {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind; 
    requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.annotation;
    
    exports umu.tds.apps.AppControlGastos to javafx.graphics;

    opens umu.tds.apps.AppControlGastos.view to javafx.fxml;
    
    
    opens umu.tds.modeloNegocio to com.fasterxml.jackson.databind;
    
    opens umu.tds.modeloNegocio.AlertasEstrategia to com.fasterxml.jackson.databind;
    
    opens umu.tds.apps.AppControlGastos to javafx.fxml;
    
    exports umu.tds.modeloNegocio;
}