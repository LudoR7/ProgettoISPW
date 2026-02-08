package com.artigianhair.controller;

import com.artigianhair.bean.CarrelloBean;

import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.model.Ordine;
import com.artigianhair.model.Prodotto;
import com.artigianhair.model.StatoOrdine;

import com.artigianhair.persistence.dao.OrdineDAO;
import com.artigianhair.view.cli.GestioneInputCLI;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EcommerceController {
    Logger logger = Logger.getLogger(getClass().getName());
    // Accesso ai dati degli ordini tramite interfaccia DAO
    private final OrdineDAO ordineDAO = DAOfactory.getOrdineDAO();
    // Costanti per i nomi base delle categorie di prodotto
    private static final String ACTION_1 = "Shampoo";
    private static final String ACTION_2 = "Maschera";
    private static final String ACTION_3 = "Siero";

    public List<Prodotto> generaProdottiPersonalizzati(int scelta) {
        List<Prodotto> personalizzati = new ArrayList<>();

        GestioneInputCLI.print("\nAbbiamo creato questi prodotti per te: aggiungi al carrello quelli che preferisci");

        switch (scelta) {
            case 1 -> generaCaso1(personalizzati);
            case 2 -> generaCaso2(personalizzati);
            case 3 -> generaCaso3(personalizzati);
            default ->
                    GestioneInputCLI.print("Scelta non valida.");
        }
        return personalizzati;
    }

    public boolean processaOrdine(CarrelloBean carrello) {
        try {
            // Controllo che il carrello non sia vuoto e ci sia un utente associato
            if (carrello.getProdottiConQuantita().isEmpty() || carrello.getEmailCliente() == null) {
                logger.warning("Impossibile processare l'ordine: carrello vuoto o email mancante.");
                return false;
            }
            // Converte la mappa dei prodotti in una lista di stringhe che siano leggibili per il modello Ordine
            List<String> righeProdotti = new ArrayList<>();
            carrello.getProdottiConQuantita().forEach((prodotto, quantita) -> righeProdotti.add(prodotto.nome() + " x" + quantita));

            // Crea il nuovo oggetto di dominio Ordine impostando lo stato iniziale
            Ordine nuovoOrdine = new Ordine(carrello.getEmailCliente(), righeProdotti, StatoOrdine.IN_LAVORAZIONE);

            // Salvataggio permanente tramite DAO
            ordineDAO.salvaOrdine(nuovoOrdine);
            GestioneInputCLI.print("Ordine processato con successo per: " + carrello.getEmailCliente());
            return true;

        } catch (IOException e) {
            logger.warning("Errore durante il salvataggio dell'ordine: " + e.getMessage());
        } catch (NullPointerException e) {
            logger.warning("Errore: Dati del carrello incompleti.");
        }
        return false;
    }

    private void generaCaso1(List<Prodotto> lista) {
        GestioneInputCLI.print("\n1) Shampoo: Hydra-Soft. Arricchito con olio di Argan per idratare in profondità.\n2) Maschera: Nutri-Gloss. Trattamento intensivo emolliente.\n3) Siero: Silk-Drop. Elimina l'effetto crespo istantaneamente.");
        selezionaProdotti(lista,
                new Prodotto(ACTION_1, "Hydra-Soft", "Idratazione profonda", 15.50),
                new Prodotto(ACTION_2, "Nutri-Gloss", "Trattamento emolliente", 22.00),
                new Prodotto(ACTION_3, "Silk-Drop", "Effetto seta", 18.90)
        );
    }

    private void generaCaso2(List<Prodotto> lista) {
        GestioneInputCLI.print("\n1) Shampoo: Pure-Balance. Estratti di menta e argilla per purificare la cute.\n2) Maschera: Light-Touch. Idratazione leggera che non appesantisce.\n3) Siero: Fresh-Scalp. Riequilibrante a lunga durata.");
        selezionaProdotti(lista,
                new Prodotto(ACTION_1, "Pure-Balance", "Purificante", 14.00),
                new Prodotto(ACTION_2, "Light-Touch", "Idratazione leggera", 20.00),
                new Prodotto(ACTION_3, "Fresh-Scalp", "Riequilibrante", 17.50)
        );
    }

    private void generaCaso3(List<Prodotto> lista) {
        GestioneInputCLI.print("\n1) Shampoo: Universal-Care. Deterge con delicatezza, per uso quotidiano.\n2x) Maschera: Basic-Repair. Riforza la struttura del capello.\n3) Siero: Shine-Boost. Per una lucentezza naturale.");
        selezionaProdotti(lista,
                new Prodotto(ACTION_1, "Universal-Care", "Detergente delicato", 12.00),
                new Prodotto(ACTION_2, "Basic-Repair", "Protezione standard", 19.00),
                new Prodotto(ACTION_3, "Shine-Boost", "Lucentezza naturale", 16.00)
        );
    }

    private void selezionaProdotti(List<Prodotto> lista, Prodotto p1, Prodotto p2, Prodotto p3) {
            while (true) {
                int input = GestioneInputCLI.leggiInt("Scelta: ");
                if (input == 0) {
                    break;
                }

                switch (input) {
                    case 1 -> lista.add(p1);
                    case 2 -> lista.add(p2);
                    case 3 -> lista.add(p3);
                    default -> GestioneInputCLI.print("Opzione non valida.");
                }
            }
    }
}


