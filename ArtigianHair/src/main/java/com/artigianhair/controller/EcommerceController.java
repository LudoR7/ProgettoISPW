package com.artigianhair.controller;

import com.artigianhair.bean.CarrelloBean;

import com.artigianhair.model.Ordine;
import com.artigianhair.model.Prodotto;
import com.artigianhair.model.StatoOrdine;

import com.artigianhair.persistence.fs.FileSystemOrdineDAO;
import com.artigianhair.view.cli.GestioneInputCLI;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EcommerceController {
    Logger logger = Logger.getLogger(getClass().getName());
    private final FileSystemOrdineDAO ordineDAO = new FileSystemOrdineDAO();

    public List<Prodotto> generaProdottiPersonalizzati(int scelta) {
        List<Prodotto> personalizzati = new ArrayList<>();
        //CarrelloBean carrello = new CarrelloBean();
        logger.info("\nAbbiamo creato questi prodotti per te: aggiungi al carrello quelli che preferisci");

        switch (scelta) {
            case 1 -> generaCaso1(personalizzati);
            case 2 -> generaCaso2(personalizzati);
            case 3 -> generaCaso3(personalizzati);
            default ->
                    logger.info("Scelta non valide.");
        }
        return personalizzati;
    }

    public void processaOrdine(CarrelloBean carrello) {
        try {
            List<String> righeProdotti = new ArrayList<>();
            carrello.getProdottiConQuantita().forEach((prodotto, quantita) -> righeProdotti.add(prodotto.nome() + " x" + quantita));

            Ordine nuovoOrdine = new Ordine(
                    carrello.getEmailCliente(),
                    righeProdotti,
                    StatoOrdine.IN_LAVORAZIONE
            );

            ordineDAO.salvaOrdine(nuovoOrdine);
            logger.info("Ordine processato con successo per: " + carrello.getEmailCliente());

        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dell'ordine: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("Errore: Dati del carrello incompleti.");
        }
    }

    private void generaCaso1(List<Prodotto> lista) {
        logger.info("\n1) Shampoo: Hydra-Soft. Arricchito con olio di Argan per idratare in profondità.\n2) Maschera: Nutri-Gloss. Trattamento intensivo emolliente.\n3) Siero: Silk-Drop. Elimina l'effetto crespo istantaneamente.");
        selezionaProdotti(lista,
                new Prodotto("Shampoo", "Hydra-Soft", "Idratazione profonda", 15.50),
                new Prodotto("Maschera", "Nutri-Gloss", "Trattamento emolliente", 22.00),
                new Prodotto("Siero", "Silk-Drop", "Effetto seta", 18.90)
        );
    }

    private void generaCaso2(List<Prodotto> lista) {
        logger.info("\n1) Shampoo: Pure-Balance. Estratti di menta e argilla per purificare la cute.\n2) Maschera: Light-Touch. Idratazione leggera che non appesantisce.\n3) Siero: Fresh-Scalp. Riequilibrante a lunga durata.");
        selezionaProdotti(lista,
                new Prodotto("Shampoo", "Pure-Balance", "Purificante", 14.00),
                new Prodotto("Maschera", "Light-Touch", "Idratazione leggera", 20.00),
                new Prodotto("Siero", "Fresh-Scalp", "Riequilibrante", 17.50)
        );
    }

    private void generaCaso3(List<Prodotto> lista) {
        logger.info("\n1) Shampoo: Universal-Care. Deterge con delicatezza, per uso quotidiano.\n2x) Maschera: Basic-Repair. Riforza la struttura del capello.\n3) Siero: Shine-Boost. Per una lucentezza naturale.");
        selezionaProdotti(lista,
                new Prodotto("Shampoo", "Universal-Care", "Detergente delicato", 12.00),
                new Prodotto("Maschera", "Basic-Repair", "Protezione standard", 19.00),
                new Prodotto("Siero", "Shine-Boost", "Lucentezza naturale", 16.00)
        );
    }

    private void selezionaProdotti(List<Prodotto> lista, Prodotto p1, Prodotto p2, Prodotto p3) {
            boolean b = true;
            while (b) {
                int input = GestioneInputCLI.leggiInt("Scelta: ");
                if (input == 0) {
                    b = false;
                    break;
                }

                switch (input) {
                    case 1 -> lista.add(p1);
                    case 2 -> lista.add(p2);
                    case 3 -> lista.add(p3);
                    default -> logger.info("Opzione non valida.");
                }
            }
    }
}


