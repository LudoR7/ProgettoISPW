package com.artigianhair.view.cli;

import com.artigianhair.bean.CarrelloBean;
import com.artigianhair.controller.EcommerceController;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Prodotto;
import java.util.List;
import java.util.logging.Logger;

public class EcommerceCLI {
    Logger logger = Logger.getLogger(getClass().getName());
    private final EcommerceController controller = new EcommerceController();
    public void start() {
        CarrelloBean carrello = new CarrelloBean();
        carrello.setEmailCliente(SessioneAttuale.getInstance().getCurrentUser().getEmail());
        start(carrello);
    }
    public void start(CarrelloBean carrello) {
        logger.info("\nACQUISTO PRODOTTI PERSONALIZZATI \n");
        logger.info("\nScegli le caratteristiche che si avvicinano meglio ai tuoi capelli: ");
        logger.info(" 1) Secchi e sfibrati.");
        logger.info(" 2) Grassi.");
        logger.info(" 3) Normali.");

        int scelta = GestioneInputCLI.leggiInt("___");
        List<Prodotto> prodotti = controller.generaProdottiPersonalizzati(scelta);

        carrello.setEmailCliente(SessioneAttuale.getInstance().getCurrentUser().getEmail());

        for (Prodotto prodotto : prodotti) {
            logger.info(String.format(" - %s", prodotto));
        }
        prodotti.forEach(carrello::addProdotto);
        confermaCarrello(carrello);

    }

    private void confermaCarrello(CarrelloBean carrello) {
        logger.info("\nIL TUO CARRELLO: ");

        carrello.getProdottiConQuantita().forEach((p, qta) -> logger.info(" - " + p.nome() + " [Quantità: " + qta + "]"));

        logger.info("\n1) Conferma e Procedi all'ordine");
        logger.info("2) Continua lo shopping");
        logger.info("3) Torna alla Home");

        int scelta3 = GestioneInputCLI.leggiInt("Scelta: ");
        switch (scelta3) {
            case 1 -> {
                controller.processaOrdine(carrello);
                logger.info("Grazie! Ordine effettuato. Riceverai i prodotti a breve.");
            }
            case 2 -> start(carrello);
            default -> logger.info("Ritorno alla home...");
        }
    }
}
