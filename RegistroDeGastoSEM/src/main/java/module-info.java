module umu.tds.apps.RegistroDeGastoSEM {
    requires javafx.controls;
    requires javafx.fxml;

    opens umu.tds.apps.RegistroDeGastoSEM to javafx.fxml;
    exports umu.tds.apps.RegistroDeGastoSEM;
}
