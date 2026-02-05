package com.artigianhair.view.fx;

import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class HomeGUIController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    protected void goToPrenotazione() {
        SceneManager.setLastScene("PrenotazioneGUI.fxml");
        if (checkLogin()) {
            SceneManager.changeScene(SceneManager.getLastScene());
        }
    }

    @FXML
    protected void goToEcommerce() {
        SceneManager.setLastScene("EcommerceGUI.fxml");
        if (checkLogin()) {
            SceneManager.changeScene(SceneManager.getLastScene());
        }
    }
    @FXML
    protected void goToProfilo() {
        SceneManager.changeScene("ProfiloGUI.fxml");
    }
    @FXML
    protected void goToLogin() {
        SceneManager.changeScene("LoginGUI.fxml");
    }

    private boolean checkLogin() {
        User currentUser = SessioneAttuale.getInstance().getCurrentUser();
        if (currentUser == null) {
            goToLogin();
            return false;
        }
        return true;
    }


    @FXML
    protected void goToHome() {
        SceneManager.changeScene("HomeGUI.fxml");
    }

    @FXML
    protected void handleLogout() {
        if (SessioneAttuale.getInstance().getCurrentUser() != null) {
            SessioneAttuale.getInstance().logout();
            emailField.clear();
            passwordField.clear();
            showError("Logout", "Sessione chiusa con successo.");
        } else {
            showError("Operazione non valida", "Nessun utente risulta attualmente loggato.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}