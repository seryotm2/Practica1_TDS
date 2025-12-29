module umu.tds.apps.RegistroDeGastoSEM {
    requires javafx.controls;
    requires javafx.fxml;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.core;

    opens umu.tds.AppRegistroDeGastoSEM to javafx.fxml;
    opens umu.tds.modeloNegocio to com.fasterxml.jackson.databind;
    exports umu.tds.AppRegistroDeGastoSEM;
}
