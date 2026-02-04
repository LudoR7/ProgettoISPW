package com.artigianhair.view.cli;

import com.artigianhair.controller.GestioneOrdiniController;
import com.artigianhair.model.Ordine;
import com.artigianhair.model.StatoOrdine;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class GestioneProdottiCLI {
    Logger logger = Logger.getLogger(getClass().getName());

    private final GestioneOrdiniController controller = new GestioneOrdiniController();

    public void start() {
        boolean back = false;
        while (!back) {
            logger.info("\nGESTIONE ORDINI: \n");
            logger.info("1) Visualizza Ordini e Cambia Stato");
            logger.info("2) Torna alla Home\n");

            int scelta = GestioneInputCLI.leggiInt("Scelta: ");
            if (scelta == 1) {
                gestisciOrdini();
            }
            else {
                back = true;
            }
        }
    }

    private void gestisciOrdini() {
        try {
            List<Ordine> ordini = controller.visualizzaTuttiOrdini();
            if (ordini.isEmpty()) {
                logger.info("Nessun ordine presente.");
                return;
            }



            boolean b = true;
            while (b) {
                for (int i = 0; i < ordini.size(); i++) {
                    Ordine o = ordini.get(i);
                    int finalI = i;
                    logger.info(() -> (finalI + 1) + ") [" + o.getStato() + "] Cliente: " + o.getEmailCliente());
                }
                int index = GestioneInputCLI.leggiInt("Seleziona ID ordine per cambiare stato (0 per annullare): ") - 1;
                if (index >= 0 && index < ordini.size()) {
                    modificaStato(ordini.get(index));
                } else {
                    logger.info("Seleziona un numero valido.");
                    b = false;
                }
            }

        } catch (IOException e) {
            logger.info("Errore: " + e.getMessage());
        }
    }

    private void modificaStato(Ordine ordine) throws IOException {
        logger.info("\nNuovo stato per l'ordine di " + ordine.getEmailCliente() + ":");
        logger.info("1) IN LAVORAZIONE\n2) SPEDITO\n3) CONSEGNATO");
        int s = GestioneInputCLI.leggiInt("Scegli: ");

        StatoOrdine nuovo = switch(s) {
            case 2 -> StatoOrdine.SPEDITO;
            case 3 -> StatoOrdine.CONSEGNATO;
            default -> StatoOrdine.IN_LAVORAZIONE;
        };

        try {
            controller.cambiaStatoOrdine(ordine, nuovo);
            logger.info("[OK] Stato aggiornato con successo sul file ordini.csv!");
        } catch (IOException e) {
            logger.info("[ERRORE] Impossibile aggiornare il file: " + e.getMessage());
        }
    }
}