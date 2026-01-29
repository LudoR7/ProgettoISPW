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

import java.time.LocalDate;
import java.util.*;

public class PrenotazioneCLI {
    private final PrenotazioneController controller = new PrenotazioneController();
    public static final List<String> MESI_VALIDI = Arrays.asList("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
            "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre");


    public void start(){
        User userCorrente = SessioneAttuale.getInstance().getCurrentUser();
        System.out.println("NUOVA PRENOTAZIONE:  ");
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


        //bean.setData(cambiaData(giorno, meseScelto));
        //.setOrario(fascia.equalsIgnoreCase("M") ? "09:00" : "15:00");
        selezionaTrattamenti(bean);

        try{
            controller.confermaAppuntamento(bean);
            System.out.println("Prenotazione effettuata con successo.");
            System.out.println("Riceverai a breve una mail di conferma, all'indirizzo: " + userCorrente.getEmail());
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
        int giorniMese = LocalDate.of(annoCorrente, meseIndice, 1).lengthOfMonth();
// METTI I GIORNI DEI MESI GIUSTI. 30, 31, 28

        List<AppuntamentoBean> appuntamentiPresenti;
        try {
            // Recupera la lista di tutti gli appuntamenti tramite il controller
            appuntamentiPresenti = AgendaController.recuperaAppuntamenti();
        } catch (Exception e) {
            appuntamentiPresenti = new ArrayList<>();
        }

        // Estrae i giorni del mese già prenotati per il mese corrente
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
        System.out.println("Legenda: m=Mattina occ, p=Pomeriggio occ, X=Pieno");System.out.println("LUN  MAR  MER  GIO  VEN  SAB  DOM");
        System.out.println("---------------------------------");

        for (int i = 1; i <= giorniMese; i++) {

            /*if (giorniOccupati.contains(i)||i % 7 ==0) {
                System.out.printf("%-5s", "X");
            } else {
                System.out.printf("%-5d", i);
            }


            if (i % 7 == 0) {
                System.out.println();
            }
        }
        System.out.println("\n---------------------------------");*/
            boolean mOcc = isFasciaOccupata(i, mese, "M");
            boolean pOcc = isFasciaOccupata(i, mese, "P");

            String marker;
            if (mOcc && pOcc || i%7 ==0) marker = "X";
            else if (mOcc) marker = "m";
            else if (pOcc) marker = "p";
            else marker = String.valueOf(i);

            System.out.printf("%-5s", marker);

            // Vai a capo ogni 7 giorni (assumendo lunedì come 1°)
            if (i % 7 == 0) System.out.println();
        }
        System.out.println("\n---------------------------------");
    }

    private String validazioneFasciaOraria(int giorno, String mese){
            String fascia = GestioneInputCLI.leggiString("Scegli fascia (Mattina: M, Pomeriggio: P):").toUpperCase();
            if(!fascia.equals("M") && !fascia.equals("P")){
                System.out.println("Opzione non valida.\n");
                return "";
            }

            // Controllo se la fascia è già occupata
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

            // Confronta con i dati restituiti dall'AgendaController (data ISO e fascia M/P)
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
                // Controllo che ignora maiuscole/minuscole
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

        boolean inserimentoTrattamenti = true;

  // !!!!!!!!!!!!! io ci metterei un for per gestire se clicco due volte lo stesso trattamento.
        while(inserimentoTrattamenti && bean.getTrattamenti().size()<3){
            int scelta = GestioneInputCLI.leggiInt("Inserisci ID del trattamento: ");
            if(scelta == 0){
                inserimentoTrattamenti = false;
            }else {
                String t = mappaTrattamento(scelta);
                if(t != null){
                    bean.addTrattamento(t);
                    System.out.println("Aggiunto:  " + t);
                }else{
                    System.out.println("Scelta non valida.");
                }

            }
        }

    }
}
