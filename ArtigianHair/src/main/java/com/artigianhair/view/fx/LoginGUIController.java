package com.artigianhair.view.fx;

import com.artigianhair.bean.UserBean;
import com.artigianhair.controller.LoginController;
import com.artigianhair.engineering.exception.LoginException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;


public class LoginGUIController {

    // Campi di testo collegati al file FXML
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    //Controller della logica applicativa
    private final LoginController loginController = new LoginController();

    //Gestione dell'evento "premuto tasto Login"
    @FXML
    protected void handleLogin() throws LoginException, IOException {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showError("Campi Obbligatori", "Inserire Email e Password: ");
            return;
        }

        // Inserimento dei dati in un UserBean per il trasferimento al controller
        UserBean loginBean = new UserBean();
        loginBean.setEmail(emailField.getText());
        loginBean.setPassword(passwordField.getText());

        try {
            //Login tramite il controller
            if (loginController.login(loginBean)) {
                //Login riuscito: cambio scena
                SceneManager.changeScene(SceneManager.getLastScene());
            }
        } catch (LoginException e) {
            showError("Credenziali Errate", e.getMessage());
            SceneManager.changeScene("LoginGUI.fxml");
        } catch (IOException e) {
            showError("Errore di Sistema", "Impossibile accedere ai dati di persistenza.");
        }
    }




    @FXML
    protected void goToHome() {
        SceneManager.changeScene("HomeGUI.fxml");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void goToRegistration() {
        SceneManager.changeScene("RegistrazioneGUI.fxml");
    }
}