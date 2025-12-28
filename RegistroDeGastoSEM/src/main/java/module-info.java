module umu.tds.apps.RegistroDeGastoSEM {
    requires javafx.controls;
    requires javafx.fxml;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.datatype.jsr310;

    opens umu.tds.AppRegistroDeGastoSEM to javafx.fxml;
    exports umu.tds.AppRegistroDeGastoSEM;
}
