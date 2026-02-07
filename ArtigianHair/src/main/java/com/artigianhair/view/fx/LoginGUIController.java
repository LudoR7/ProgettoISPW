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

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final LoginController loginController = new LoginController();

    @FXML
    protected void handleLogin() throws LoginException, IOException {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showError("Campi Obbligatori", "Inserire Email e Password: ");
            return;
        }

        UserBean loginBean = new UserBean();
        loginBean.setEmail(emailField.getText());
        loginBean.setPassword(passwordField.getText());

        try {
            if (loginController.login(loginBean)) {
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