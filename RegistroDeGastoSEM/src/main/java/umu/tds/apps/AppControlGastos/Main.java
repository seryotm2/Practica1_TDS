package umu.tds.apps.AppControlGastos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            String fxmlPath = "/umu/tds/view/menu_principal.fxml";
            
            URL url = getClass().getResource(fxmlPath);
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            
            primaryStage.setTitle("RG:SEM - Control de Gastos");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); 
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("Ocurrió un error al iniciar la aplicación: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}