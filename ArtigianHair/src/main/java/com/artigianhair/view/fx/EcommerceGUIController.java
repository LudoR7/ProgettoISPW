package com.artigianhair.view.fx;

import com.artigianhair.bean.CarrelloBean;
import com.artigianhair.controller.EcommerceController;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Prodotto;
import com.artigianhair.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class EcommerceGUIController {
    private static final String ACTION_1 = "Shampoo";
    private static final String ACTION_2 = "Maschera";
    private static final String ACTION_3 = "Siero";
    private static final String ACTION_4 = "EcommerceGUI.fxml";


    @FXML private VBox paneSceltaCapelli;
    @FXML private VBox paneSelezioneProdotti;
    @FXML private VBox paneCarrello;
    @FXML private Button btnProd1;
    @FXML private Button btnProd2;
    @FXML private Button btnProd3;
    @FXML private TextArea txtAreaCarrello;
    @FXML private Label lblStatus;
    @FXML private Text txtTitoloCategoria;
    @FXML private Text txtDesc1;
    @FXML private Text txtDesc2;
    @FXML private Text txtDesc3;

    private final EcommerceController appController = new EcommerceController();
    private final CarrelloBean carrello = new CarrelloBean();
    private List<Prodotto> prodottiDisponibili;


    private final Prodotto[] caso1 = {
            new Prodotto(ACTION_1, "Hydra-Soft", " Arricchito con olio di Argan per idratare in profondità.", 15.50),
            new Prodotto(ACTION_2, "Nutri-Gloss", " Nutri-Gloss. Trattamento intensivo emolliente.", 22.00),
            new Prodotto(ACTION_3, "Silk-Drop", "Elimina l'effetto crespo istantaneamente.", 18.90)
    };
    private final Prodotto[] caso2 = {
            new Prodotto(ACTION_1, "Pure-Balance", "Estratti di menta e argilla per purificare la cute.", 14.00),
            new Prodotto(ACTION_2, "Light-Touch", "Idratazione leggera che non appesantisce.", 20.00),
            new Prodotto(ACTION_3, "Fresh-Scalp", "Riequilibrante a lunga durata.", 17.50)
    };
    private final Prodotto[] caso3 = {
            new Prodotto(ACTION_1, "Universal-Care", "Deterge con delicatezza, per uso quotidiano.", 12.00),
            new Prodotto(ACTION_2, "Basic-Repair", "Riforza la struttura del capello.", 19.00),
            new Prodotto(ACTION_3, "Shine-Boost", "Per una lucentezza naturale.", 16.00)
    };

    @FXML
    public void initialize() {
        User user = SessioneAttuale.getInstance().getCurrentUser();
        if (user != null) {
            carrello.setEmailCliente(user.getEmail());
        } else {
            lblStatus.setText("Errore: Sessione non valida.");
        }
    }

    @FXML private void onSceltaSecchi() { mostraSelezione(caso1, "Capelli Secchi"); }
    @FXML private void onSceltaGrassi() { mostraSelezione(caso2, "Capelli Grassi"); }
    @FXML private void onSceltaNormali() { mostraSelezione(caso3, "Capelli Normali"); }

    private void mostraSelezione(Prodotto[] prodotti, String titolo) {
        prodottiDisponibili = List.of(prodotti);
        txtTitoloCategoria.setText("Prodotti per " + titolo + ":");


        btnProd1.setText(prodotti[0].nome() + " (€" + prodotti[0].prezzo() + ")");
        btnProd2.setText(prodotti[1].nome() + " (€" + prodotti[1].prezzo() + ")");
        btnProd3.setText(prodotti[2].nome() + " (€" + prodotti[2].prezzo() + ")");


        txtDesc1.setText(prodotti[0].descrizione());
        txtDesc2.setText(prodotti[1].descrizione());
        txtDesc3.setText(prodotti[2].descrizione());

        paneSceltaCapelli.setVisible(false);
        paneSceltaCapelli.setManaged(false);
        paneSelezioneProdotti.setVisible(true);
        paneSelezioneProdotti.setManaged(true);
    }

    @FXML private void addProd1() { aggiungiAlCarrello(prodottiDisponibili.get(0)); }
    @FXML private void addProd2() { aggiungiAlCarrello(prodottiDisponibili.get(1)); }
    @FXML private void addProd3() { aggiungiAlCarrello(prodottiDisponibili.get(2)); }

    private void aggiungiAlCarrello(Prodotto p) {
        carrello.addProdotto(p);
        lblStatus.setText("Aggiunto: " + p.nome());
    }

    @FXML
    private void confermaSelezione() {
        aggiornaAreaCarrello();
        paneSelezioneProdotti.setVisible(false);
        paneSelezioneProdotti.setManaged(false);
        paneCarrello.setVisible(true);
        paneCarrello.setManaged(true);
    }

    private void aggiornaAreaCarrello() {
        StringBuilder sb = new StringBuilder();
        carrello.getProdottiConQuantita().forEach((p, qta) -> {
            double parziale = p.prezzo() * qta;
            sb.append(" - ").append(p.nome())
                    .append(" [Qta: ").append(qta).append("]")
                    .append(" - Subtotale: €").append(String.format("%.2f", parziale))
                    .append("\n");
        });


        sb.append("\n--------------------------\n");
        sb.append("TOTALE ORDINE: €").append(String.format("%.2f", carrello.getTotale()));

        txtAreaCarrello.setText(sb.toString());
    }

    @FXML
    private void handleConfermaOrdine() {
        if (carrello.getProdottiConQuantita().isEmpty()) {
            lblStatus.setText("Il carrello è vuoto!");
            return;
        }
        appController.processaOrdine(carrello);
        lblStatus.setText("Ordine inviato con successo!");
        showAlert(Alert.AlertType.INFORMATION, "I Tuoi Appuntamenti", "Il tuo ordine è stato salvato con successo!");
        paneCarrello.setDisable(true);
        SceneManager.changeScene(ACTION_4);

    }
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    @FXML
    private void handleCancellaOrdine() {
        paneCarrello.setDisable(true);
        SceneManager.changeScene(ACTION_4);

    }

    @FXML
    private void resetShopping() {
        paneCarrello.setVisible(false);
        paneCarrello.setManaged(false);
        paneSceltaCapelli.setVisible(true);
        paneSceltaCapelli.setManaged(true);
        lblStatus.setText("Continua i tuoi acquisti.");
    }


    @FXML private void goToHome() { SceneManager.changeScene("HomeGUI.fxml"); }
    @FXML private void goToPrenotazione() { SceneManager.changeScene("PrenotazioneGUI.fxml"); }
    @FXML private void goToProfilo() { SceneManager.changeScene("ProfiloGUI.fxml"); }
    @FXML private void goToEcommerce() { SceneManager.changeScene(ACTION_4); }
    @FXML private void handleLogout() {
        SessioneAttuale.getInstance().logout();
        SceneManager.changeScene("LoginGUI.fxml");
    }
}