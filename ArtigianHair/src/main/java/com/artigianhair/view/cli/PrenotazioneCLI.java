package com.artigianhair.view.cli;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.bean.UserBean;
import com.artigianhair.controller.PrenotazioneController;
import com.artigianhair.engineering.exception.PrenotazioneException;
import com.artigianhair.model.Appuntamento;

import java.util.Scanner;

public class PrenotazioneCLI {
    private final UserBean userSession;
    private final PrenotazioneController controller = new PrenotazioneController();

    public PrenotazioneCLI(UserBean userSession){
        this.userSession = userSession;
    }
    public void start(){
        System.out.println("NUOVA PRENOTAZIONE:  ");
        AppuntamentoBean bean = new AppuntamentoBean();

        bean.setClienteEmail(userSession.getEmail());

        bean.setData(GestioneInputCLI.leggiString("Inserisci data (GG-MM-AAAA):  "));
        bean.setOrario(GestioneInputCLI.leggiString("Inserisci l'orario (OO:mm)"));

        System.out.println("\nTrattamenti disponibili:\n1) Piega, 2) Taglio, 3) Colore, 4) Keratina");
        System.out.println("Puoi scegliere massimo 3 trattamenti. Premi 0 per terminare:  ");

        boolean inserimentoTrattamenti = true;
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
        try{
            controller.confermaAppuntamento(bean);
            System.out.println("Prenotazione effettuata con successo.");
            System.out.println("Riceverai a breve una mail di conferma, all'indirizzo: " + userSession.getEmail());
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
}
