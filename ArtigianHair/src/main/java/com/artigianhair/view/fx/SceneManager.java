package com.artigianhair.view.fx;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {

    private static Stage stage;
    private static String lastScene;

    private SceneManager() {}

    public static void setStage(Stage stage) {
        SceneManager.stage = stage;
    }

    public static void changeScene(String fxmlFile) {
        try {
            String resourcePath = "/com/artigianhair/view/fx/" + fxmlFile;
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(resourcePath));

            if (loader.getLocation() == null) {
                throw new IOException("File FXML non trovato: " + resourcePath);
            }

            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errores");
            alert.setHeaderText("Errore nel caricamento della scena");
            alert.showAndWait();
        }
    }

    public static void setLastScene(String scene) {
        lastScene = scene;
    }

    public static String getLastScene() {
        return lastScene;
    }
}
