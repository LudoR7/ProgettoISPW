package com.artigianhair.view.fx;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.controller.AgendaController;
import com.artigianhair.controller.PrenotazioneController;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Ruolo;
import com.artigianhair.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artigianhair.model.Ordine;
import com.artigianhair.controller.GestioneOrdiniController;



import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.collections.FXCollections;
import com.artigianhair.model.StatoOrdine;

public class ProfiloGUIController {

    @FXML private Label labelBenvenuto;
    @FXML private VBox clientContainer;
    @FXML private VBox ownerContainer;
    @FXML private VBox disdettaBox;
    @FXML private VBox listaCheckDisdetta;
    @FXML private VBox guestContainer;
    @FXML private Button logoutBtn;
    @FXML private VBox agendaBox;
    @FXML private TableView<AppuntamentoBean> tabellaAgenda;
    @FXML private TableColumn<AppuntamentoBean, String> colData;
    @FXML private TableColumn<AppuntamentoBean, String> colOrario;
    @FXML private TableColumn<AppuntamentoBean, String> colCliente;
    @FXML private TableColumn<AppuntamentoBean, String> colTrattamenti;
    @FXML private VBox ordiniBox;
    @FXML private TableView<Ordine> tabellaOrdini;
    @FXML private TableColumn<Ordine, String> colClienteOrdine;
    @FXML private TableColumn<Ordine, String> colProdottiOrdine;
    @FXML private TableColumn<Ordine, StatoOrdine> colStatoOrdine;

    @FXML private VBox recuperaBox;
    @FXML private VBox listaAppuntamentiRecupera;

    @FXML private VBox mieiOrdiniBox;
    @FXML private TableView<Ordine> tabellaMieiOrdini;
    @FXML private TableColumn<Ordine, String> colMieiProdotti;
    @FXML private TableColumn<Ordine, String> colMioStato;


    private final PrenotazioneController prenoController = new PrenotazioneController();
    private final GestioneOrdiniController ordiniController = new GestioneOrdiniController();
    private final Map<CheckBox, AppuntamentoBean> mappaSelezioni = new HashMap<>();

    @FXML
    public void initialize() {
        User user = SessioneAttuale.getInstance().getCurrentUser();
        if (user == null) {
            mostraSezione(guestContainer);
            labelBenvenuto.setText("Area Riservata");
        }else {
            labelBenvenuto.setText("Benvenuto/a, " + user.getNome());
            logoutBtn.setVisible(true);
            logoutBtn.setManaged(true);

            if (user.getRuolo() == Ruolo.PROPRIETARIA) {
                mostraSezione(ownerContainer);
            } else {
                mostraSezione(clientContainer);
            }
        }
    }

    private void mostraSezione(VBox sezioneAttiva) {
        guestContainer.setVisible(false); guestContainer.setManaged(false);
        clientContainer.setVisible(false); clientContainer.setManaged(false);
        ownerContainer.setVisible(false); ownerContainer.setManaged(false);

        sezioneAttiva.setVisible(true);
        sezioneAttiva.setManaged(true);
    }


    @FXML protected void handleGoToLogin() {
        SceneManager.changeScene("LoginGUI.fxml");
    }

    @FXML protected void handleGoToRegistration() {
        SceneManager.changeScene("RegistrazioneGUI.fxml");
    }


    @FXML
    protected void handleRecupera() {
        try {
            String email = SessioneAttuale.getInstance().getCurrentUser().getEmail();
            List<AppuntamentoBean> appuntamenti = prenoController.recuperaAppuntamentiUtente(email);

            if (appuntamenti.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Nessun appuntamento", "Non risultano prenotazioni a tuo nome.");
                return;
            }


            listaAppuntamentiRecupera.getChildren().clear();


            for (AppuntamentoBean a : appuntamenti) {
                VBox card = new VBox(5);
                card.setStyle("-fx-background-color: #E6D2C1; -fx-padding: 10; -fx-background-radius: 10;");

                String fascia = a.getOrario().equalsIgnoreCase("M") ? "Mattina" : "Pomeriggio";

                Label infoData = new Label("📅 Data: " + a.getData() + " " + a.getMese());
                infoData.setStyle("-fx-font-weight: bold; -fx-text-fill: #4D3E33;");

                Label infoFascia = new Label("⏰ Fascia: " + fascia);
                infoFascia.setStyle("-fx-text-fill: #4D3E33;");

                Label infoTrattamenti = new Label("💇 Trattamenti: " + String.join(", ", a.getTrattamenti()));
                infoTrattamenti.setStyle("-fx-text-fill: #4D3E33; -fx-wrap-text: true;");

                card.getChildren().addAll(infoData, infoFascia, infoTrattamenti);
                listaAppuntamentiRecupera.getChildren().add(card);
            }

            recuperaBox.setVisible(true);
            recuperaBox.setManaged(true);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile recuperare lo storico appuntamenti.");
        }
    }

    @FXML
    protected void handleNascondiRecupera() {
        recuperaBox.setVisible(false);
        recuperaBox.setManaged(false);
    }

    @FXML protected void handleDisdici() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Funzionalità 'Disdici' in fase di implementazione.");
    }



    @FXML protected void handleVisualizzaAgenda() {

        try {

            List<AppuntamentoBean> listaAppuntamenti = AgendaController.recuperaAppuntamenti();

            if (listaAppuntamenti.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Agenda Vuota", "Non ci sono appuntamenti registrati.");
                return;
            }


            colData.setCellValueFactory(new PropertyValueFactory<>("data"));
            colOrario.setCellValueFactory(new PropertyValueFactory<>("orario"));
            colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteEmail"));

            colTrattamenti.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.join(", ", cellData.getValue().getTrattamenti())));


            tabellaAgenda.getItems().setAll(listaAppuntamenti);


            agendaBox.setVisible(true);
            agendaBox.setManaged(true);

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare l'agenda dal database.");
        }
    }

    @FXML
    protected void handleNascondiAgenda() {
        agendaBox.setVisible(false);
        agendaBox.setManaged(false);
    }


    @FXML protected void handleLogout() {
        SessioneAttuale.getInstance().logout();
        SceneManager.changeScene("ProfiloGUI.fxml");
    }

    @FXML protected void goToHome() {
        SceneManager.changeScene("HomeGUI.fxml");
    }
    @FXML protected void goToPrenotazione() {
        SceneManager.changeScene("PrenotazioneGUI.fxml");
    }

    @FXML protected void goToEcommerce() {
        SceneManager.changeScene("EcommerceGUI.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    protected void handleMostraDisdetta() {
        String email = SessioneAttuale.getInstance().getCurrentUser().getEmail();

        try {
            List<AppuntamentoBean> mieiAppuntamenti = prenoController.recuperaAppuntamentiUtente(email);

            if (mieiAppuntamenti.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Nessun appuntamento", "Non hai appuntamenti da disdire.");
                disdettaBox.setVisible(false);
                disdettaBox.setManaged(false);
                return;
            }


            listaCheckDisdetta.getChildren().clear();
            mappaSelezioni.clear();


            for (AppuntamentoBean app : mieiAppuntamenti) {
                String info = String.format("%s %s - %s (%s)",
                        app.getData(), app.getMese(),
                        (app.getOrario().equals("M") ? "Mattina" : "Pomeriggio"),
                        String.join(", ", app.getTrattamenti()));

                CheckBox cb = new CheckBox(info);
                cb.setTextFill(Color.BLACK);
                cb.setStyle("-fx-font-size: 13px;");

                mappaSelezioni.put(cb, app);
                listaCheckDisdetta.getChildren().add(cb);
            }


            disdettaBox.setVisible(true);
            disdettaBox.setManaged(true);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare gli appuntamenti.");
        }
    }
    @FXML
    protected void handleVisualizzaOrdini() {
        try {
            List<Ordine> listaOrdini = ordiniController.visualizzaTuttiOrdini();

            if (listaOrdini.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Nessun Ordine", "Non sono presenti ordini.");
                return;
            }


            tabellaOrdini.setEditable(true);

            colClienteOrdine.setCellValueFactory(new PropertyValueFactory<>("emailCliente"));
            colProdottiOrdine.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.join(", ", cellData.getValue().getProdotti())));


            colStatoOrdine.setCellValueFactory(new PropertyValueFactory<>("stato"));


            colStatoOrdine.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(StatoOrdine.values())));


            colStatoOrdine.setOnEditCommit(event -> {
                Ordine ordine = event.getRowValue();
                StatoOrdine nuovoStato = event.getNewValue();

                try {

                    ordiniController.cambiaStatoOrdine(ordine, nuovoStato);


                    ordine.setStato(nuovoStato);

                    showAlert(Alert.AlertType.INFORMATION, "Stato Aggiornato",
                            "L'ordine di " + ordine.getEmailCliente() + " è ora in stato: " + nuovoStato);
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile aggiornare lo stato su file.");
                }
            });

            tabellaOrdini.getItems().setAll(listaOrdini);
            ordiniBox.setVisible(true);
            ordiniBox.setManaged(true);

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare gli ordini.");
        }
    }
    @FXML
    protected void handleNascondiOrdini() {
        ordiniBox.setVisible(false);
        ordiniBox.setManaged(false);
    }
    @FXML
    protected void handleNascondiDisdetta() {
        disdettaBox.setVisible(false);
        disdettaBox.setManaged(false);
    }

    @FXML
    protected void handleEcommerce() {
        SceneManager.changeScene("EcommerceGUI.fxml");
    }
    @FXML
    protected void confermaEliminazione() {
        List<AppuntamentoBean> daEliminare = new ArrayList<>();


        for (Map.Entry<CheckBox, AppuntamentoBean> entry : mappaSelezioni.entrySet()) {
            if (entry.getKey().isSelected()) {
                daEliminare.add(entry.getValue());
            }
        }

        if (daEliminare.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Selezione vuota", "Seleziona almeno un appuntamento da disdire.");
            return;
        }

        try {

            for (AppuntamentoBean bean : daEliminare) {
                AgendaController.cancellaAppuntamento(bean);
            }

            showAlert(Alert.AlertType.INFORMATION, "Successo", "Appuntamenti annullati con successo.");


            handleMostraDisdetta();
            SceneManager.changeScene("ProfiloGUI.fxml");

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Errore durante la cancellazione su file.");
        }
    }
    @FXML
    protected void handleMieiOrdini() {
        try {
            User user = SessioneAttuale.getInstance().getCurrentUser();
            if (user == null) return;

            String emailLoggata = user.getEmail();

            List<Ordine> tuttiOrdini = ordiniController.visualizzaTuttiOrdini();


            List<Ordine> ordiniUtente = tuttiOrdini.stream()
                    .filter(o -> o.getEmailCliente().equalsIgnoreCase(emailLoggata))
                    .toList();

            if (ordiniUtente.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Nessun Ordine", "Non hai ancora effettuato ordini.");
                return;
            }

            colMieiProdotti.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.join(", ", cellData.getValue().getProdotti())));
            colMioStato.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getStato().toString()));


            tabellaMieiOrdini.getItems().setAll(ordiniUtente);
            mieiOrdiniBox.setVisible(true);
            mieiOrdiniBox.setManaged(true);

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare i tuoi ordini.");
        }
    }

    @FXML
    protected void handleNascondiMieiOrdini() {
        mieiOrdiniBox.setVisible(false);
        mieiOrdiniBox.setManaged(false);
    }
    @FXML
    protected void goToProfilo() {
        SceneManager.changeScene("ProfiloGUI.fxml");
    }
    @FXML
    protected void goToLogin() {
        SceneManager.changeScene("LoginGUI.fxml");
    }



}