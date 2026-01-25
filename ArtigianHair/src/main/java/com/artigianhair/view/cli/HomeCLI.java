package com.artigianhair.view.cli;

import com.artigianhair.bean.UserBean;
import com.artigianhair.model.Ruolo;

public class HomeCLI {
    private final UserBean userBean;

    public HomeCLI(UserBean userBean) {
        this.userBean = userBean;
    }

    public void start() {
        boolean valido = true;
        while (valido) {
            System.out.println("\n HOME (" + userBean.getRuolo() + ") ");
            System.out.println("Benvenuto, " + userBean.getNome() + " " + userBean.getCognome());

            if (userBean.getRuolo() == Ruolo.PROPRIETARIA) {
                valido = mostraMenuProprietaria();
            } else {
                valido = mostraMenuUtente();
            }
        }
    }

    private boolean mostraMenuUtente() {
        System.out.println("1) Prenota Appuntamento ");
        System.out.println("2) Acquista Prodotti Personalizzati ");
        System.out.println("3) Logout");

        int scelta = GestioneInputCLI.leggiInt("Scegli un'opzione: ");
        switch (scelta) {
            case 1: {
                new PrenotazioneCLI(userBean).start();
                return true;
            }
            case 2: {
                System.out.println("Sezione E-commerce...");
                return true;
            }
            case 3: {
                return false; }
            default: {
                System.out.println("Opzione non valida.");
                return true;
            }
        }
    }

    private boolean mostraMenuProprietaria() {
        System.out.println("1) Visualizza Agenda");
        System.out.println("2) Gestione Prodotti ");
        System.out.println("3) Logout");

        int scelta = GestioneInputCLI.leggiInt("Scegli un'opzione: ");
        switch (scelta) {
            case 1 : {
                new AgendaCLI().start();
                return true;
            }
            case 2 : {
                System.out.println("Apertura sezione E-commerce...");
                return true;
            }
            case 3 : {
                return false; }
            default : {
                System.out.println("Opzione non valida.");
                return true;
            }
        }
    }
}