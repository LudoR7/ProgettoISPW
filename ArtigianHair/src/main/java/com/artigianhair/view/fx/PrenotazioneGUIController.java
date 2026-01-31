package com.artigianhair.view.fx;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.bean.UserBean;
import com.artigianhair.controller.PrenotazioneController;
import com.artigianhair.engineering.exception.PrenotazioneException;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.User;
import com.artigianhair.view.cli.PrenotazioneCLI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneGUIController {

    @FXML private DatePicker datePicker;
    @FXML private RadioButton mattinaRadio;
    @FXML private RadioButton pomeriggioRadio;

    @FXML private CheckBox piegaCheck;
    @FXML private CheckBox taglioCheck;
    @FXML private CheckBox coloreCheck;
    @FXML private CheckBox keratinaCheck;

    private final PrenotazioneController controller = new PrenotazioneController();

    @FXML
    public void initialize() {

        ToggleGroup group = new ToggleGroup();
        mattinaRadio.setToggleGroup(group);
        pomeriggioRadio.setToggleGroup(group);
        mattinaRadio.setSelected(true);

        CheckBox[] allChecks = {piegaCheck, taglioCheck, coloreCheck, keratinaCheck};

        for (CheckBox cb : allChecks) {
            cb.selectedProperty().addListener((observable, wasSelected, isNowSelected) -> {

                long count = 0;
                for (CheckBox c : allChecks) {
                    if (c.isSelected()) count++;
                }

                if (count >= 3) {

                    for (CheckBox c : allChecks) {
                        if (!c.isSelected()) {
                            c.setDisable(true);
                        }
                    }
                } else {

                    for (CheckBox c : allChecks) {
                        c.setDisable(false);
                    }
                }
            });
        }
    }

    @FXML
    protected void handlePrenotazione() {

        if (SessioneAttuale.getInstance().getCurrentUser() == null) {
            SceneManager.changeScene("LoginGUI.fxml");
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


        String meseIta = PrenotazioneCLI.MESI_VALIDI.get(date.getMonthValue() - 1);
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
    protected void goToHome() {
        SceneManager.changeScene("HomeGUI.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}