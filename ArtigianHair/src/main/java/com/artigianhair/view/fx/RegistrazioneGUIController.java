package com.artigianhair.view.fx;

import com.artigianhair.bean.UserBean;
import com.artigianhair.controller.LoginController;
import com.artigianhair.model.Ruolo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class RegistrazioneGUIController {

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final LoginController loginController = new LoginController();

    @FXML
    protected void handleRegistration() {
        if (nomeField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Dati mancanti.", "Compila tutti i campi obbligatori.");
            return;
        }

        UserBean newUserBean = new UserBean();
        newUserBean.setNome(nomeField.getText());
        newUserBean.setCognome(cognomeField.getText());
        newUserBean.setEmail(emailField.getText());
        newUserBean.setPassword(passwordField.getText());
        newUserBean.setRuolo(Ruolo.UTENTE);

        try {
            loginController.registraUtente(newUserBean);
            showAlert(Alert.AlertType.INFORMATION, "Successo", "Registrazione completata!");
            goToHome();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore Registrazione", e.getMessage());
        }
    }

    @FXML
    protected void goToLogin() {
        SceneManager.changeScene("LoginGUI.fxml");
    }

    @FXML
    protected void goToHome() {
        SceneManager.changeScene("HomeGUI.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
