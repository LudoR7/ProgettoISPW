package com.artigianhair.view.fx;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.bean.UserBean;
import com.artigianhair.controller.AgendaController;
import com.artigianhair.controller.LoginController;
import com.artigianhair.engineering.exception.LoginException;
import com.artigianhair.engineering.singleton.SessioneAttuale;

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
        }else {
            showError("Errore", "Credenziali non valide o errore di autenticazione.");
        }

        UserBean loginBean = new UserBean();
        loginBean.setEmail(emailField.getText());
        loginBean.setPassword(passwordField.getText());

        try {
            if (loginController.login(loginBean)) {
                SceneManager.changeScene("ProfiloGUI.fxml");
            }
        } catch (LoginException e) {
            showError("Credenziali Errate", e.getMessage());
        } catch (IOException e) {
            showError("Errore di Sistema", "Impossibile accedere ai dati di persistenza.");
        }
    }


    @FXML
    protected void handleLogout() {
        if (SessioneAttuale.getInstance().getCurrentUser() != null) {
            SessioneAttuale.getInstance().logout();

            emailField.clear();
            passwordField.clear();

            showInfo("Logout", "Sessione chiusa con successo.");
        } else {
            showError("Operazione non valida", "Nessun utente risulta attualmente loggato.");
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

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void goToRegistration() {
        SceneManager.changeScene("RegistrazioneGUI.fxml");
    }


    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


}