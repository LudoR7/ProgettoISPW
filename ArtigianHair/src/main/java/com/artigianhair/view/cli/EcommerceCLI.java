package com.artigianhair.view.cli;

import com.artigianhair.bean.CarrelloBean;
import com.artigianhair.controller.EcommerceController;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Prodotto;
import java.util.List;

public class EcommerceCLI {
    private final EcommerceController controller = new EcommerceController();

    public void start() {
        System.out.println("\nACQUISTO PRODOTTI PERSONALIZZATI \n");
        //String esigenza = GestioneInputCLI.leggiString("Descrivi le caratteristiche dei tuoi capelli (es. secchi, grassi, colorati): ");
        System.out.println("Scegli le caratteristiche che si avvicinano meglio ai tuoi capelli: ");
        System.out.println(" 1) Secchi e sfibrati.");
        System.out.println(" 2) Grassi.");
        System.out.println(" 3) Normali.");

        List<Prodotto> proposti = controller.generaProdottiPersonalizzati();
        CarrelloBean carrello = new CarrelloBean();
        carrello.setEmailCliente(SessioneAttuale.getInstance().getCurrentUser().getEmail());

        System.out.println("\nAbbiamo creato questi prodotti per te:");
        for (int i = 0; i < proposti.size(); i++) {
            System.out.println((i + 1) + ") " + proposti.get(i));
        }

        boolean shopping = true;
        while (shopping) {
            System.out.println("\n1) Aggiungi tutti al carrello e procedi");
            System.out.println("2) Torna alla Home (Annulla)");
            int scelta = GestioneInputCLI.leggiInt("Scegli: ");

            if (scelta == 1) {
                proposti.forEach(carrello::addProdotto);
                confermaCarrello(carrello);
                shopping = false;
            } else if (scelta == 2) {
                shopping = false;
            }
        }
    }

    private void confermaCarrello(CarrelloBean carrello) {
        System.out.println("\nIL TUO CARRELLO: ");
        carrello.getProdotti().forEach(p -> System.out.println(" - " + p.nome()));

        System.out.println("\n1) Conferma e Procedi all'ordine");
        System.out.println("2) Cambia esigenza");
        System.out.println("3) Torna alla Home");

        int scelta = GestioneInputCLI.leggiInt("Scelta: ");
        switch (scelta) {
            case 1 -> {
                controller.processaOrdine(carrello);
                System.out.println("Grazie! Ordine effettuato. Riceverai i prodotti a breve.");
            }
            case 2 -> start();
            default -> System.out.println("Ritorno alla home...");
        }
    }
}
