package com.artigianhair.view.cli;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.bean.UserBean;
import com.artigianhair.controller.AgendaController;
import com.artigianhair.controller.PrenotazioneController;
import com.artigianhair.engineering.exception.AppuntamentoException;
import com.artigianhair.engineering.exception.PrenotazioneException;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Appuntamento;
import com.artigianhair.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class PrenotazioneCLI {
    private final PrenotazioneController controller = new PrenotazioneController();
    public static final List<String> MESI_VALIDI = Arrays.asList("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
            "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre");


    public boolean start1(){
        System.out.println("\n1) Effettua una nuova prenotazione.");
        System.out.println("2) Recupera prenotazione.");
        System.out.println("3) Annulla una prenotazione.");
        int scelta = GestioneInputCLI.leggiInt("\nScegli un'opzione: ");
        switch (scelta) {
            case 1 : {
                new PrenotazioneCLI().start();
                return true;
            }
            case 2 : {
                //
                recuperaPrenotazione();
                return true;
            }
            case 3 : {
                annullaPrenotazione(); return true;
            }
            default : {
                System.out.println("Opzione non valida.");
                return true;
            }
        }
    }

    public void start(){
        User userCorrente = SessioneAttuale.getInstance().getCurrentUser();

        System.out.println("--------------------------------------------------");
        System.out.println("\nNUOVA PRENOTAZIONE:  ");
        AppuntamentoBean bean = new AppuntamentoBean();

        String meseScelto = validazioneMese();
        bean.setMese(meseScelto);
        int meseIndice = MESI_VALIDI.indexOf(meseScelto) + 1;
        int maxGiorni = LocalDate.of(LocalDate.now().getYear(), meseIndice, 1).lengthOfMonth();
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
            System.out.println("\nPrenotazione effettuata con successo.");
            System.out.println("\nRiceverai a breve una mail di conferma, all'indirizzo: " + userCorrente.getEmail());
        } catch(PrenotazioneException e){
            System.out.println("Errore durante la prenotazione: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore nel sistema, riprovare più tardi: " + e.getMessage());
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

    private void stampaCalendarioMensile(String mese){
        int meseIndice = MESI_VALIDI.indexOf(mese) + 1;
        int annoCorrente = LocalDate.now().getYear();
        //int giorniMese = LocalDate.of(annoCorrente, meseIndice, 1).lengthOfMonth();
        LocalDate primoDelMese = LocalDate.of(annoCorrente, meseIndice, 1);
        int giorniMese = primoDelMese.lengthOfMonth();
        int offsetGiorno = primoDelMese.getDayOfWeek().getValue();

        List<AppuntamentoBean> appuntamentiPresenti;
        try {
            appuntamentiPresenti = AgendaController.recuperaAppuntamenti();
        } catch (Exception e) {
            appuntamentiPresenti = new ArrayList<>();
        }

        Set<Integer> giorniOccupati = new HashSet<>();
        LocalDate oggi = LocalDate.now();
        for (AppuntamentoBean b : appuntamentiPresenti) {
            try {
                LocalDate dataApp = LocalDate.parse(b.getData());
                if (dataApp.getMonth() == oggi.getMonth()) {
                    giorniOccupati.add(dataApp.getDayOfMonth());
                }
            } catch (Exception ignored) {}
        }

        System.out.println("\nCALENDARIO " + mese.toUpperCase() + " " + annoCorrente);
        System.out.println("\nLegenda: (-,P) = Mattina occupata, (M,-) = Pomeriggio occupato, X = Pieno");System.out.println("LUN     MAR     MER     GIO     VEN     SAB     DOM");
        System.out.println("--------------------------------------------------");

        for (int i = 1; i <= giorniMese; i++) {
            boolean mOcc = isFasciaOccupata(i, mese, "M");
            boolean pOcc = isFasciaOccupata(i, mese, "P");

            String marker;
            if (mOcc && pOcc) {
                marker = "X";
            } else if (mOcc) {
                marker = String.valueOf(i) + "(-,P)";
            } else if (pOcc) {
                marker = String.valueOf(i) + "(M,-)";
            } else if(i % 7 == 0){
                marker = "-";
            }else {
                marker = String.valueOf(i);
            }

            System.out.printf("%-8s", marker);
            if (i % 7 == 0) System.out.println();
        }
        System.out.println("\n--------------------------------------------------\n");
    }

    private String validazioneFasciaOraria(int giorno, String mese){
            String fascia = GestioneInputCLI.leggiString("Scegli fascia (Mattina: M, Pomeriggio: P):").toUpperCase();
            if(!fascia.equals("M") && !fascia.equals("P")){
                System.out.println("Opzione non valida.\n");
                return "";
            }
            if(isFasciaOccupata(giorno, mese, fascia)){
                System.out.println("Spiacenti, la fascia - " + fascia + " - è già occupata per questo giorno.\n");
                return "";
            } else{
                return fascia;
            }
    }

    private boolean isFasciaOccupata(int giorno, String mese, String fascia) {
        try {
            int meseIndice = MESI_VALIDI.indexOf(mese) + 1;
            LocalDate dataTarget = LocalDate.of(LocalDate.now().getYear(), meseIndice, giorno);
            String dataTargetStr = dataTarget.toString(); // Formato "YYYY-MM-DD"

            return AgendaController.recuperaAppuntamenti().stream()
                    .anyMatch(a -> a.getData().equals(dataTargetStr) && a.getOrario().equalsIgnoreCase(fascia));
        } catch (Exception e) {
            return false;
        }
    }

    private String validazioneMese(){
        /*boolean meseValido = false;
        String meseInserito = "";
        while (!meseValido) {
            meseInserito = GestioneInputCLI.leggiString("Inserisci il mese (Gennaio, Febbraio, Marzo...): ");

            for (String m : MESI_VALIDI) {
                if (m.equalsIgnoreCase(meseInserito)) {
                    meseValido = true;
                    break;
                }
            }

            if (!meseValido) {
                System.out.println("Errore: '" + meseInserito + "' non è un mese valido. Riprova.");
            }
        }
        return meseInserito;*/
        while (true) {
            String input = GestioneInputCLI.leggiString("Inserisci il mese (es. Gennaio): ");
            for (String m : MESI_VALIDI) {
                if (m.equalsIgnoreCase(input)) {
                    return m;
                }
            }
            System.out.println("Mese non valido. Inserire il nome completo del mese.");
        }
    }

    private String cambiaData(int giorno, String mese){
        int meseIndice = MESI_VALIDI.indexOf(mese) + 1;
        return String.format("%02d-%02d", giorno, meseIndice);
    }

    private void selezionaTrattamenti(AppuntamentoBean bean){
        System.out.println("\nTrattamenti disponibili:\n- Piega (1), \n- Taglio (2),\n- Colore (3), \n- Keratina (4)");
        System.out.println("Puoi scegliere massimo 3 trattamenti.\n(Premi 0 per terminare.) ");
        /*boolean piega = false;
        boolean taglio = false;
        boolean colore = false;
        boolean keratina = false;
        boolean inserimentoTrattamenti = true;

        while(inserimentoTrattamenti && bean.getTrattamenti().size()<3){
            int scelta = GestioneInputCLI.leggiInt("Inserisci ID del trattamento: ");

            String t = mappaTrattamento(scelta);
            if(scelta == 1 && !piega){
                if(t != null){
                    bean.addTrattamento(t);
                    System.out.println("Aggiunto:  " + t);
                }else{
                    System.out.println("Scelta non valida.");
                }
                piega = true;
            }else if(scelta == 2 && !taglio){
                if(t != null){
                    bean.addTrattamento(t);
                    System.out.println("Aggiunto:  " + t);
                }else{
                    System.out.println("Scelta non valida.");
                }
                taglio = true;
            }else if(scelta == 3 && !colore){
                if(t != null){
                    bean.addTrattamento(t);
                    System.out.println("Aggiunto:  " + t);
                }else{
                    System.out.println("Scelta non valida.");
                }
                colore = true;
            }else if(scelta == 4 && !keratina){
                if(t != null){
                    bean.addTrattamento(t);
                    System.out.println("Aggiunto:  " + t);
                }else{
                    System.out.println("Scelta non valida.");
                }
                keratina = true;
            }else if(scelta == 0){
                inserimentoTrattamenti = false;
            }*/

            boolean inserimentoTrattamenti = true;

            while (inserimentoTrattamenti && bean.getTrattamenti().size() < 3) {
                try {
                    int scelta = GestioneInputCLI.leggiInt("Scegli il trattamento: ");

                    if (scelta == 0) {
                        inserimentoTrattamenti = false;
                        continue;
                    }

                    String t = mappaTrattamento(scelta);

                    if (t == null) {
                        System.out.println("Scelta non valida.");
                        continue;
                    }

                    if (bean.getTrattamenti().contains(t)) {

                        throw new AppuntamentoException("Il trattamento - " + t + " - è già stato inserito.");
                    }

                    bean.addTrattamento(t);
                    System.out.println("Aggiunto: " + t);

                } catch (AppuntamentoException e) {
                    System.out.println("\nATTENZIONE: " + e.getMessage());
                    System.out.println("Scegli un trattamento diverso o premi 0 per terminare.");
                }
            }
    }


    private void recuperaPrenotazione() {
        User userCorrente = SessioneAttuale.getInstance().getCurrentUser();
        String emailUtente = userCorrente.getEmail();

        System.out.println("\nLE TUE PRENOTAZIONI: ");
        try {

            List<AppuntamentoBean> tuttiAppuntamenti = AgendaController.recuperaAppuntamenti();
            boolean trovato = false;


            System.out.printf("%-12s | %-8s | %-30s%n", "DATA", "ORA", "TRATTAMENTI");
            System.out.println("------------------------------------------------------------");

            for (AppuntamentoBean b : tuttiAppuntamenti) {
                if (b.getClienteEmail().equalsIgnoreCase(emailUtente)) {
                    System.out.printf("%-12s | %-8s | %-30s%n",
                            b.getData(),
                            b.getOrario(),
                            String.join(", ", b.getTrattamenti()));
                    trovato = true;
                }
            }

            if (!trovato) {
                System.out.println("Nessuna prenotazione trovata per l'account:  " + emailUtente);
            }

        } catch (Exception e) {
            System.out.println("Errore durante il recupero delle prenotazioni: " + e.getMessage());
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
                System.out.println("Non hai appuntamenti da annullare.");
                return;
            }

            System.out.println("\nSELEZIONA L'APPUNTAMENTO DA ANNULLARE: ");
            for (int i = 0; i < iMieiAppuntamenti.size(); i++) {
                AppuntamentoBean b = iMieiAppuntamenti.get(i);
                System.out.printf("%d) Data: %s | Ora: %s | Trattamenti: %s%n",
                        i + 1, b.getData(), b.getOrario(), String.join(", ", b.getTrattamenti()));
            }

            int index = GestioneInputCLI.leggiInt("\nInserisci il numero dell'appuntamento (0 per tornare indietro): ") - 1;

            if (index == -1) {
                return;
            }
            if (index >= 0 && index < iMieiAppuntamenti.size()) {
                AgendaController.cancellaAppuntamento(iMieiAppuntamenti.get(index));
                System.out.println("Appuntamento annullato con successo.");
            } else {
                System.out.println("Scelta non valida.");
                annullaPrenotazione();
            }

        } catch (IOException e) {
            System.out.println("Errore durante l'operazione: " + e.getMessage());
        }

    }

}

