package com.artigianhair.view.cli;

import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Ruolo;
import com.artigianhair.model.User;

public class HomeCLI {

    public HomeCLI() {
        //Costruttore di default
    }

    public void start() {

        User userCorrente = SessioneAttuale.getInstance().getCurrentUser();
        boolean valido = true;
        while (valido) {
            GestioneInputCLI.print("\nHOME (" + userCorrente.getRuolo() + ") ");
            GestioneInputCLI.print("Ciao, " + userCorrente.getNome() + " " + userCorrente.getCognome());

            if (userCorrente.getRuolo() == Ruolo.PROPRIETARIA) {
                valido = mostraMenuProprietaria();
            } else {
                valido = mostraMenuUtente();
            }
        }
    }

    private boolean mostraMenuUtente() {
        GestioneInputCLI.print("1) Prenota Appuntamento ");
        GestioneInputCLI.print("2) Acquista Prodotti Personalizzati ");
        GestioneInputCLI.print("3) Logout");

        int scelta = GestioneInputCLI.leggiInt("Scegli un'opzione: ");
        return switch (scelta) {
            case 1 -> {
                new PrenotazioneCLI().start1();
                yield true;
            }
            case 2 -> {
                new EcommerceCLI().start();
                yield true;
            }
            case 3 -> false;
            default -> {
                GestioneInputCLI.print("Opzione non valida.");
                yield true;
            }
        };
    }

    private boolean mostraMenuProprietaria() {
        GestioneInputCLI.print("1) Visualizza Agenda");
        GestioneInputCLI.print("2) Gestione Prodotti ");
        GestioneInputCLI.print("3) Logout");

        int scelta = GestioneInputCLI.leggiInt("Scegli un'opzione: ");
        return switch (scelta) {
            case 1 -> {
                new AgendaCLI().start();
                yield true;
            }
            case 2 -> {
                new GestioneProdottiCLI().start();
                yield true;
            }
            case 3 -> false;
            default -> {
                GestioneInputCLI.print("Opzione non valida.");
                yield true;
            }
        };
    }
}