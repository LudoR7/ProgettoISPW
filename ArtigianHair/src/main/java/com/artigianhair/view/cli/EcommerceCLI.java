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
        GestioneInputCLI.print("\nACQUISTO PRODOTTI PERSONALIZZATI \n");
        GestioneInputCLI.print("\nScegli le caratteristiche che si avvicinano meglio ai tuoi capelli: ");
        GestioneInputCLI.print(" 1) Secchi e sfibrati.");
        GestioneInputCLI.print(" 2) Grassi.");
        GestioneInputCLI.print(" 3) Normali.");

        int scelta = GestioneInputCLI.leggiInt("___");
        List<Prodotto> prodotti = controller.generaProdottiPersonalizzati(scelta);

        carrello.setEmailCliente(SessioneAttuale.getInstance().getCurrentUser().getEmail());

        for (Prodotto prodotto : prodotti) {
            GestioneInputCLI.print(" - " + prodotto);
        }
        prodotti.forEach(carrello::addProdotto);
        confermaCarrello(carrello);

    }

    private void confermaCarrello(CarrelloBean carrello) {
        GestioneInputCLI.print("\nIL TUO CARRELLO: ");

        carrello.getProdottiConQuantita().forEach((p, qta) -> GestioneInputCLI.print(" - " + p.nome() + " [Quantità: " + qta + "]"));

        GestioneInputCLI.print("\n1) Conferma e Procedi all'ordine");
        GestioneInputCLI.print("2) Continua lo shopping");
        GestioneInputCLI.print("3) Torna alla Home");

        int scelta3 = GestioneInputCLI.leggiInt("Scelta: ");
        switch (scelta3) {
            case 1 -> {
                controller.processaOrdine(carrello);
                GestioneInputCLI.print("Grazie! Ordine effettuato. Riceverai i prodotti a breve.");
            }
            case 2 -> start(carrello);
            default -> GestioneInputCLI.print("Ritorno alla home...");
        }
    }
}
