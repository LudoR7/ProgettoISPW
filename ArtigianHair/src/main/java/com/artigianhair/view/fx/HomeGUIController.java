package com.artigianhair.view.fx;

import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class HomeGUIController {

    @FXML
    protected void goToPrenotazione() {
        if (checkLogin()) SceneManager.changeScene("PrenotazioneGUI.fxml");
    }

    @FXML
    protected void goToEcommerce() {
        SceneManager.changeScene("EcommerceGUI.fxml");
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
        SessioneAttuale.getInstance().logout();
        SceneManager.changeScene("LoginGUI.fxml");
    }
}