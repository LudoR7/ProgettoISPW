package com.artigianhair.view.fx;

import com.artigianhair.bean.CarrelloBean;
import com.artigianhair.controller.EcommerceController;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Prodotto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import java.util.List;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class EcommerceGUIController {

    private final EcommerceController ecommerceController = new EcommerceController();
    private final CarrelloBean carrello = new CarrelloBean();
    private List<Prodotto> prodottiSuggeriti;

    @FXML
    public void initialize() {

        var user = SessioneAttuale.getInstance().getCurrentUser();
        if (user != null) {
            carrello.setEmailCliente(user.getEmail());
        } else {
            carrello.setEmailCliente("Ospite"); // Identificativo temporaneo
        }

        int tipoSuggerimento = (user != null) ? 1 : 3;
        prodottiSuggeriti = ecommerceController.generaProdottiPersonalizzati(tipoSuggerimento);
    }

    @FXML
    protected void addShampoo() {
        aggiungiAlCarrello(0);
    }

    @FXML
    protected void addBalsamo() {
        aggiungiAlCarrello(1);
    }

    @FXML
    protected void addMaschera() {
        aggiungiAlCarrello(2);
    }

    private void aggiungiAlCarrello(int index) {
        if (prodottiSuggeriti != null && index < prodottiSuggeriti.size()) {
            Prodotto p = prodottiSuggeriti.get(index);
            carrello.addProdotto(p);
            showInfo("Carrello", "Aggiunto: " + p.nome());
        }
    }

    @FXML
    protected void handleOrder() {
        if (carrello.getProdottiConQuantita().isEmpty()) {
            showWarning("Carrello vuoto", "Aggiungi almeno un prodotto prima di ordinare.");
            return;
        }

        if ("Ospite".equals(carrello.getEmailCliente())) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ordine Ospite");
            dialog.setHeaderText("Acquisto come ospite");
            dialog.setContentText("Inserisci la tua email per ricevere la conferma:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().isEmpty()) {
                carrello.setEmailCliente(result.get());
            } else {
                showWarning("Email mancante", "È necessaria un'email per processare l'ordine.");
                return;
            }
        }

        ecommerceController.processaOrdine(carrello);
        showInfo("Ordine Effettuato", "Grazie! Ordine registrato per: " + carrello.getEmailCliente());
    }

    @FXML
    protected void goToHome() {
        SceneManager.changeScene("HomeGUI.fxml");
    }

    @FXML
    protected void goToEcommerce() {
        SceneManager.changeScene("EcommerceGUI.fxml");
    }



    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showWarning(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
