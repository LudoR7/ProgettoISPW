package com.artigianhair.view.fx;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.bean.UserBean;
import com.artigianhair.controller.AgendaController;
import com.artigianhair.controller.LoginController;
import com.artigianhair.engineering.exception.LoginException;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.List;

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
                String nomeUtente = SessioneAttuale.getInstance().getCurrentUser().getNome();
                showInfo("Benvenuto", "Accesso effettuato come: " + nomeUtente);
                SceneManager.changeScene("PrenotazioneGUI.fxml");
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
    protected void handleRecovery() {
        // Implementazione futura basata su Pagina 3 del PDF
        System.out.println("Navigazione verso Recupero Prenotazione...");
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


    @FXML
    protected void handleRecuperaAppuntamento() {
        User currentUser = SessioneAttuale.getInstance().getCurrentUser();

        if (currentUser != null) {
            SceneManager.changeScene("PrenotazioneGUI.fxml");
        } else {

            try {
                handleLogin();

                if (SessioneAttuale.getInstance().getCurrentUser() != null) {
                    SceneManager.changeScene("PrenotazioneGUI.fxml");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.WARNING, "Autenticazione richiesta",
                        "Per recuperare i tuoi appuntamenti devi prima effettuare il login.");
            }
        }
    }
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


}