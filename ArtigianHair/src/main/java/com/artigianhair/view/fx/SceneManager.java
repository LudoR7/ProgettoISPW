package com.artigianhair.view.fx;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {

    private static Stage stage;

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
            e.printStackTrace();
        }
    }
}
