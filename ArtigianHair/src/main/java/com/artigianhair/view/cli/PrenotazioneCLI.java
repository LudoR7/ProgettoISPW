package com.artigianhair.view.cli;

import com.artigianhair.bean.AppuntamentoBean;

import com.artigianhair.controller.AgendaController;
import com.artigianhair.controller.PrenotazioneController;
import com.artigianhair.engineering.exception.AppuntamentoException;
import com.artigianhair.engineering.exception.PrenotazioneException;
import com.artigianhair.engineering.singleton.SessioneAttuale;

import com.artigianhair.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class PrenotazioneCLI {
    private final PrenotazioneController controller = new PrenotazioneController();
    protected static final List<String> MESI_VALIDI = Arrays.asList("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre");


    public void start1(){
        GestioneInputCLI.print("\n1) Effettua una nuova prenotazione.");
        GestioneInputCLI.print("2) Recupera prenotazione.");
        GestioneInputCLI.print("3) Annulla una prenotazione.");
        int scelta = GestioneInputCLI.leggiInt("\nScegli un'opzione: ");
        switch (scelta) {
            case 1 : {
                new PrenotazioneCLI().start();
                break;
            }
            case 2 : {
                //
                recuperaPrenotazione();
                break;
            }
            case 3 : {
                annullaPrenotazione();
                break;
            }
            default : {
                GestioneInputCLI.print("Opzione non valida.");
                break;
            }
        }
    }

    public void start(){
        User userCorrente = SessioneAttuale.getInstance().getCurrentUser();

        GestioneInputCLI.print("--------------------------------------------------");
        GestioneInputCLI.print("\nNUOVA PRENOTAZIONE:  ");
        AppuntamentoBean bean = new AppuntamentoBean();

        String meseScelto = validazioneMese();
        bean.setMese(meseScelto);
        stampaCalendarioMensile(meseScelto);

        // CHECK
        bean.setClienteEmail(userCorrente.getEmail());

        int giorno = 0;
        String fascia = "";
        boolean pc = false;

        while(!pc){
            giorno = GestioneInputCLI.leggiInt("Inserisci data (numero):  ");
            fascia = validazioneFasciaOraria(giorno,  meseScelto);

            if(giorno != 0 && !fascia.isEmpty()){
                pc = true;
            }
        }

        bean.setData(String.valueOf(giorno));
        bean.setOrario(fascia);
        selezionaTrattamenti(bean);

        try{
            controller.confermaAppuntamento(bean);
            GestioneInputCLI.print("\nPrenotazione effettuata con successo.");
            GestioneInputCLI.print("\nRiceverai a breve una mail di conferma, all'indirizzo: " + userCorrente.getEmail());
        } catch(PrenotazioneException e){
            GestioneInputCLI.print("Errore durante la prenotazione: " + e.getMessage());
        } catch (Exception e) {
            GestioneInputCLI.print("Errore nel sistema, riprovare più tardi: " + e.getMessage());
        }
    }

    private String mappaTrattamento(int scelta){
        return switch(scelta){
            case 1 -> "Piega";
            case 2 -> "Taglio";
            case 3 -> "Colore";
            case 4 -> "Keratina";
            default -> null;
        };
    }

    private void stampaCalendarioMensile(String mese) {
        int meseIndice = MESI_VALIDI.indexOf(mese) + 1;
        int annoCorrente = LocalDate.now().getYear();
        int giorniMese = LocalDate.of(annoCorrente, meseIndice, 1).lengthOfMonth();

        stampaIntestazione(mese, annoCorrente);

        for (int i = 1; i <= giorniMese; i++) {
            String marker = determinaMarkerGiorno(i, mese);
            String s = (String.format("%-8s", marker));
            GestioneInputCLI.print2(s);

            if (i % 7 == 0) {
                GestioneInputCLI.print("");
            }
        }
        GestioneInputCLI.print("\n--------------------------------------------------\n");
    }

    private void stampaIntestazione(String mese, int anno) {

        String header = String.format("%nCALENDARIO %s %s", mese.toUpperCase(), anno);
        GestioneInputCLI.print(header);

        GestioneInputCLI.print("\nLegenda: (-,P) = Mattina occupata, (M,-) = Pomeriggio occupato, X = Pieno");
        GestioneInputCLI.print("LUN     MAR     MER     GIO     VEN     SAB     DOM");
        GestioneInputCLI.print("--------------------------------------------------");
    }

    private String determinaMarkerGiorno(int giorno, String mese) {
        boolean mOcc = isFasciaOccupata(giorno, mese, "M");
        boolean pOcc = isFasciaOccupata(giorno, mese, "P");

        if (mOcc && pOcc) {
            return "X";
        }
        if (mOcc) {
            return giorno + "(-,P)";
        }
        if (pOcc) {
            return giorno + "(M,-)";
        }
        if (giorno % 7 == 0) {
            return "-";
        }
        return String.valueOf(giorno);
    }

    private String validazioneFasciaOraria(int giorno, String mese){
            String fascia = GestioneInputCLI.leggiString("Scegli fascia (Mattina: M, Pomeriggio: P):").toUpperCase();
            if(!fascia.equals("M") && !fascia.equals("P")){
                GestioneInputCLI.print("Opzione non valida.\n");
                return "";
            }
            if(isFasciaOccupata(giorno, mese, fascia)){
                String header = String.format("%nSpiacenti, la fascia - %s - è già occupata per questo giorno.%n", fascia);
                GestioneInputCLI.print(header);
                return "";
            } else{
                return fascia;
            }
    }

    private boolean isFasciaOccupata(int giorno, String mese, String fascia) {
        try {
            int meseIndice = MESI_VALIDI.indexOf(mese) + 1;
            LocalDate dataTarget = LocalDate.of(LocalDate.now().getYear(), meseIndice, giorno);
            String dataTargetStr = dataTarget.toString();

            return AgendaController.recuperaAppuntamenti().stream().anyMatch(a -> a.getData().equals(dataTargetStr) && a.getOrario().equalsIgnoreCase(fascia));
        } catch (Exception e) {
            return false;
        }
    }

    private String validazioneMese(){
        while (true) {
            String input = GestioneInputCLI.leggiString("Inserisci il mese (es. Gennaio): ");
            for (String m : MESI_VALIDI) {
                if (m.equalsIgnoreCase(input)) {
                    return m;
                }
            }
            GestioneInputCLI.print("Mese non valido. Inserire il nome completo del mese.");
        }
    }



    private void selezionaTrattamenti(AppuntamentoBean bean){
        GestioneInputCLI.print("\nTrattamenti disponibili:\n- Piega (1), \n- Taglio (2),\n- Colore (3), \n- Keratina (4)");
        GestioneInputCLI.print("Puoi scegliere massimo 3 trattamenti.\n(Premi 0 per terminare.) ");
        while (bean.getTrattamenti().size() < 3) {
            try {
                int scelta = GestioneInputCLI.leggiInt("Scegli il trattamento: ");

                if (scelta == 0) {
                    break;
                }

                String t = mappaTrattamento(scelta);

                if (t == null) {
                    GestioneInputCLI.print("Scelta non valida.");
                } else {
                    if (bean.getTrattamenti().contains(t)) {
                        throw new AppuntamentoException("Il trattamento - " + t + " - è già stato inserito.");
                    }

                    bean.addTrattamento(t);
                    String s = String.format("Aggiunto: %s", t);
                    GestioneInputCLI.print(s);
                }
                } catch (AppuntamentoException e) {
                    GestioneInputCLI.print("\nATTENZIONE: " + e.getMessage());
                    GestioneInputCLI.print("Scegli un trattamento diverso o premi 0 per terminare.");
                }
            }
    }


    private void recuperaPrenotazione() {
        User userCorrente = SessioneAttuale.getInstance().getCurrentUser();
        String emailUtente = userCorrente.getEmail();

        GestioneInputCLI.print("\nLE TUE PRENOTAZIONI: ");
        try {

            List<AppuntamentoBean> tuttiAppuntamenti = AgendaController.recuperaAppuntamenti();
            boolean trovato = false;

            String header = String.format("%-12s | %-8s | %-30s%n", "DATA", "ORA", "TRATTAMENTI");
            GestioneInputCLI.print(header);
            GestioneInputCLI.print("------------------------------------------------------------");

            for (AppuntamentoBean b : tuttiAppuntamenti) {
                if (b.getClienteEmail().equalsIgnoreCase(emailUtente)) {
                    String string = String.format("%-12s | %-8s | %-30s%n", b.getData(), b.getOrario(), String.join(", ", b.getTrattamenti()));
                    GestioneInputCLI.print(string);
                    trovato = true;
                }
            }

            if (!trovato) {
                String s = String.format("Nessuna trattamento: %s", emailUtente);
                GestioneInputCLI.print(s);
            }

        } catch (Exception e) {
            GestioneInputCLI.print("Errore durante il recupero delle prenotazioni: " + e.getMessage());
        }

        GestioneInputCLI.leggiString("\nPremi invio per tornare al menù...");
    }



    private void annullaPrenotazione() {
        User userCorrente = SessioneAttuale.getInstance().getCurrentUser();
        try {
            List<AppuntamentoBean> tutti = AgendaController.recuperaAppuntamenti();
            List<AppuntamentoBean> iMieiAppuntamenti = new ArrayList<>();

            for (AppuntamentoBean b : tutti) {
                if (b.getClienteEmail().equalsIgnoreCase(userCorrente.getEmail())) {
                    iMieiAppuntamenti.add(b);
                }
            }

            if (iMieiAppuntamenti.isEmpty()) {
                GestioneInputCLI.print("Non hai appuntamenti da annullare.");
                return;
            }

            GestioneInputCLI.print("\nSELEZIONA L'APPUNTAMENTO DA ANNULLARE: ");
            for (int i = 0; i < iMieiAppuntamenti.size(); i++) {
                AppuntamentoBean b = iMieiAppuntamenti.get(i);
                String string = String.format("%d) Data: %s | Ora: %s | Trattamenti: %s%n", i + 1, b.getData(), b.getOrario(), String.join(", ", b.getTrattamenti()));
                GestioneInputCLI.print(string);
            }

            int index = GestioneInputCLI.leggiInt("\nInserisci il numero dell'appuntamento (0 per tornare indietro): ") - 1;

            if (index == -1) {
                return;
            }
            if (index >= 0 && index < iMieiAppuntamenti.size()) {
                AgendaController.cancellaAppuntamento(iMieiAppuntamenti.get(index));
                GestioneInputCLI.print("Appuntamento annullato con successo.");
            } else {
                GestioneInputCLI.print("Scelta non valida.");
                annullaPrenotazione();
            }

        } catch (IOException e) {
            GestioneInputCLI.print("Errore durante l'operazione: " + e.getMessage());
        }

    }

}

