package com.artigianhair.view.fx;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.controller.AgendaController;
import com.artigianhair.controller.PrenotazioneController;
import com.artigianhair.engineering.exception.PrenotazioneException;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.DateCell;
import javafx.util.Callback;
import java.time.DayOfWeek;
import java.io.IOException;

public class PrenotazioneGUIController {

    @FXML private DatePicker datePicker;
    @FXML private RadioButton mattinaRadio;
    @FXML private RadioButton pomeriggioRadio;

    @FXML private CheckBox piegaCheck;
    @FXML private CheckBox taglioCheck;
    @FXML private CheckBox coloreCheck;
    @FXML private CheckBox keratinaCheck;

    private static final String ACTION_1 = "LoginGUI.fxml";
    private final PrenotazioneController controller = new PrenotazioneController();
    private List<AppuntamentoBean> appuntamentiEsistenti = new ArrayList<>();
    protected static final List<String> MESI_VALIDI = Arrays.asList("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre");

    @FXML
    public void initialize() {
        inizializzaDatiAppuntamenti();
        setupRadioButtons();
        configuraCelleCalendario();
        setupDatePicker();
        setupServiceLimitLogic();
        setupConstraintTrattamenti();
    }

    private void inizializzaDatiAppuntamenti() {
        try {
            appuntamentiEsistenti = AgendaController.recuperaAppuntamenti();
        } catch (IOException e) {
            appuntamentiEsistenti = new ArrayList<>();
        }
    }

    private void setupRadioButtons() {
        ToggleGroup group = new ToggleGroup();
        mattinaRadio.setToggleGroup(group);
        pomeriggioRadio.setToggleGroup(group);
        mattinaRadio.setSelected(true);
    }

    private void setupDatePicker() {
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                aggiornaDisponibilitaFasce(newDate);
            }
        });
    }

    private void setupServiceLimitLogic() {
        CheckBox[] allChecks = {piegaCheck, taglioCheck, coloreCheck, keratinaCheck};
        for (CheckBox cb : allChecks) {
            cb.selectedProperty().addListener((obs, wasSel, isNowSel) -> aggiornaStatoCheckBox(allChecks)
            );
        }
    }

    private void aggiornaStatoCheckBox(CheckBox[] allChecks) {
        long count = java.util.Arrays.stream(allChecks).filter(CheckBox::isSelected).count();
        boolean limiteRaggiunto = count >= 3;

        for (CheckBox cb : allChecks) {
            if (!cb.isSelected()) {
                cb.setDisable(limiteRaggiunto);
            } else {
                cb.setDisable(false);
            }
        }
    }
    private void configuraCelleCalendario() {
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);


                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");
                    return;
                }


                if (item.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffcccc;");
                    setTooltip(new Tooltip("Negozio chiuso di Domenica"));
                    return;
                }


                if (isGiornoPieno(item)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #6e6e6e;");
                    setTooltip(new Tooltip("Tutto esaurito"));
                } else if (isFasciaOccupata(item, "M") || isFasciaOccupata(item, "P")) {

                    setStyle("-fx-border-color: #7D6B5D; -fx-border-width: 2;");
                    setTooltip(new Tooltip("Disponibilità limitata"));
                }
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }

    private void aggiornaDisponibilitaFasce(LocalDate date) {
        boolean mattinaOcc = isFasciaOccupata(date, "M");
        boolean pomeriggioOcc = isFasciaOccupata(date, "P");

        mattinaRadio.setDisable(mattinaOcc);
        pomeriggioRadio.setDisable(pomeriggioOcc);


        if (mattinaOcc && mattinaRadio.isSelected()) {
            pomeriggioRadio.setSelected(true);
        } else if (pomeriggioOcc && pomeriggioRadio.isSelected()) {
            mattinaRadio.setSelected(true);
        }
    }

    private boolean isFasciaOccupata(LocalDate date, String fascia) {
        String dataStr = date.toString();
        return appuntamentiEsistenti.stream()
                .anyMatch(a -> a.getData().equals(dataStr) && a.getOrario().equalsIgnoreCase(fascia));
    }

    private boolean isGiornoPieno(LocalDate date) {
        return isFasciaOccupata(date, "M") && isFasciaOccupata(date, "P");
    }

    private void setupConstraintTrattamenti() {
        CheckBox[] allChecks = {piegaCheck, taglioCheck, coloreCheck, keratinaCheck};
        for (CheckBox cb : allChecks) {
            cb.selectedProperty().addListener((obs, wasS, isS) -> {
                long count = Arrays.stream(allChecks).filter(CheckBox::isSelected).count();
                for (CheckBox c : allChecks) {
                    if (!c.isSelected()) c.setDisable(count >= 3);
                }
            });
        }
    }

    @FXML
    protected void handlePrenotazione() {

        if (SessioneAttuale.getInstance().getCurrentUser() == null) {
            SceneManager.changeScene(ACTION_1);
            return;
        }

        LocalDate date = datePicker.getValue();


        List<String> trattamentiSelezionati = new ArrayList<>();
        if (piegaCheck.isSelected()) trattamentiSelezionati.add("Piega");
        if (taglioCheck.isSelected()) trattamentiSelezionati.add("Taglio");
        if (coloreCheck.isSelected()) trattamentiSelezionati.add("Colore");
        if (keratinaCheck.isSelected()) trattamentiSelezionati.add("Keratina");

        if (date == null || trattamentiSelezionati.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Dati incompleti", "Seleziona data e almeno un trattamento.");
            return;
        }


        AppuntamentoBean bean = new AppuntamentoBean();
        bean.setClienteEmail(SessioneAttuale.getInstance().getCurrentUser().getEmail());
        bean.setData(String.valueOf(date.getDayOfMonth()));


        String meseIta = MESI_VALIDI.get(date.getMonthValue() - 1);
        bean.setMese(meseIta);
        bean.setOrario(mattinaRadio.isSelected() ? "M" : "P");

        for (String t : trattamentiSelezionati) {
            bean.addTrattamento(t);
        }

        try {
            controller.confermaAppuntamento(bean);

            showAlert(Alert.AlertType.INFORMATION, "Prenotazione Effettuata", "Appuntamento confermato per il giorno " + date.getDayOfMonth() + " " + meseIta);
            SceneManager.changeScene("HomeGUI.fxml");

        } catch (PrenotazioneException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", e.getMessage());
        }
    }


    @FXML
    protected void handleVediMieiAppuntamenti() {

        User currentUser = SessioneAttuale.getInstance().getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "Accesso richiesto", "Devi essere loggato per visualizzare i tuoi appuntamenti.");
            return;
        }

        try {

            List<AppuntamentoBean> appuntamenti = controller.recuperaAppuntamentiUtente(currentUser.getEmail());

            if (appuntamenti.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Nessun appuntamento", "Non risultano prenotazioni a tuo nome.");
                return;
            }


            StringBuilder elenco = new StringBuilder("Ecco i tuoi appuntamenti:\n\n");
            for (AppuntamentoBean a : appuntamenti) {
                String fascia = a.getOrario().equalsIgnoreCase("M") ? "Mattina" : "Pomeriggio";
                elenco.append("- Data: ").append(a.getData()).append(" ").append(a.getMese())
                        .append("\n  Fascia: ").append(fascia)
                        .append("\n  Trattamenti: ").append(String.join(", ", a.getTrattamenti()))
                        .append("\n\n");
            }

            showAlert(Alert.AlertType.INFORMATION, "I Tuoi Appuntamenti", elenco.toString());

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile recuperare lo storico appuntamenti.");
        }
    }
    @FXML
    protected void goToHome() {
        SceneManager.changeScene("HomeGUI.fxml");
    }
    @FXML
    protected void goToProfilo() {
        SceneManager.changeScene("ProfiloGUI.fxml");
    }
    @FXML
    protected void goToEcommerce() {
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
    protected void goToPrenotazione() {
        SceneManager.changeScene("PrenotazioneGUI.fxml");
    }
    @FXML
    protected void goToLogin() {
        SceneManager.changeScene(ACTION_1);
    }
    @FXML
    protected void handleLogout() {
        SessioneAttuale.getInstance().logout();
        SceneManager.changeScene(ACTION_1);
    }
}