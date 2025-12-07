module umu.tds.apps.RegistroDeGastoSEM {
    requires javafx.controls;
    requires javafx.fxml;

    opens umu.tds.AppRegistroDeGastoSEM to javafx.fxml;
    exports umu.tds.AppRegistroDeGastoSEM;
}
