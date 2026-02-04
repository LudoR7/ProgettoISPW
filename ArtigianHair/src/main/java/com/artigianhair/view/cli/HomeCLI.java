package com.artigianhair.view.cli;

import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Ruolo;
import com.artigianhair.model.User;

import java.util.logging.Logger;

public class HomeCLI {
    Logger logger = Logger.getLogger(getClass().getName());

    public HomeCLI() {
        //Costruttore di default
    }

    public void start() {

        User userCorrente = SessioneAttuale.getInstance().getCurrentUser();
        boolean valido = true;
        while (valido) {
            logger.info("\nHOME (" + userCorrente.getRuolo() + ") ");
            logger.info("Ciao, " + userCorrente.getNome() + " " + userCorrente.getCognome());

            if (userCorrente.getRuolo() == Ruolo.PROPRIETARIA) {
                valido = mostraMenuProprietaria();
            } else {
                valido = mostraMenuUtente();
            }
        }
    }

    private boolean mostraMenuUtente() {
        logger.info("1) Prenota Appuntamento ");
        logger.info("2) Acquista Prodotti Personalizzati ");
        logger.info("3) Logout");

        int scelta = GestioneInputCLI.leggiInt("Scegli un'opzione: ");
        switch (scelta) {
            case 1: {
                new PrenotazioneCLI().start1();
                return true;
            }
            case 2: {
                new EcommerceCLI().start();
                return true;
            }
            case 3: {
                return false; }
            default: {
                logger.info("Opzione non valida.");
                return true;
            }
        }
    }

    private boolean mostraMenuProprietaria() {
        logger.info("1) Visualizza Agenda");
        logger.info("2) Gestione Prodotti ");
        logger.info("3) Logout");

        int scelta = GestioneInputCLI.leggiInt("Scegli un'opzione: ");
        switch (scelta) {
            case 1 : {
                new AgendaCLI().start();
                return true;
            }
            case 2 : {
                new GestioneProdottiCLI().start();
                return true;
            }
            case 3 : {
                return false;
            }
            default : {
                logger.info("Opzione non valida.");
                return true;
            }
        }
    }
}