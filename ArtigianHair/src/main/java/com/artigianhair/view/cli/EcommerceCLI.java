package com.artigianhair.view.cli;

import com.artigianhair.bean.CarrelloBean;
import com.artigianhair.controller.EcommerceController;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Prodotto;
import java.util.List;

public class EcommerceCLI {
    private final EcommerceController controller = new EcommerceController();
    public void start() {
        CarrelloBean carrello = new CarrelloBean();
        carrello.setEmailCliente(SessioneAttuale.getInstance().getCurrentUser().getEmail());
        start(carrello);
    }
    public void start(CarrelloBean carrello) {
        System.out.println("\nACQUISTO PRODOTTI PERSONALIZZATI \n");
        System.out.println("\nScegli le caratteristiche che si avvicinano meglio ai tuoi capelli: ");
        System.out.println(" 1) Secchi e sfibrati.");
        System.out.println(" 2) Grassi.");
        System.out.println(" 3) Normali.");

        int scelta = GestioneInputCLI.leggiInt("___");
        List<Prodotto> prodotti = controller.generaProdottiPersonalizzati(scelta);

        carrello.setEmailCliente(SessioneAttuale.getInstance().getCurrentUser().getEmail());

        for (int i = 0; i < prodotti.size(); i++) {
            System.out.println(" - " + prodotti.get(i));
        }
        prodotti.forEach(carrello::addProdotto);
        confermaCarrello(carrello);

    }

    private void confermaCarrello(CarrelloBean carrello) {
        System.out.println("\nIL TUO CARRELLO: ");

        carrello.getProdottiConQuantita().forEach((p, qta) -> System.out.println(" - " + p.nome() + " [Quantità: " + qta + "]"));

        System.out.println("\n1) Conferma e Procedi all'ordine");
        System.out.println("2) Continua lo shopping");
        System.out.println("3) Torna alla Home");

        int scelta3 = GestioneInputCLI.leggiInt("Scelta: ");
        switch (scelta3) {
            case 1 -> {
                controller.processaOrdine(carrello);
                System.out.println("Grazie! Ordine effettuato. Riceverai i prodotti a breve.");
            }
            case 2 -> start(carrello);
            default -> System.out.println("Ritorno alla home...");
        }
    }
}
