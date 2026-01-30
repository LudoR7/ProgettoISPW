package com.artigianhair.controller;

import com.artigianhair.bean.CarrelloBean;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Ordine;
import com.artigianhair.model.Prodotto;
import com.artigianhair.model.StatoOrdine;
import com.artigianhair.persistence.dao.OrdineDAO;
import com.artigianhair.persistence.fs.FileSystemOrdineDAO;
import com.artigianhair.view.cli.GestioneInputCLI;

import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;

public class EcommerceController {

    private final FileSystemOrdineDAO OrdineDAO = new FileSystemOrdineDAO();

    public List<Prodotto> generaProdottiPersonalizzati(int scelta) {
        List<Prodotto> personalizzati = new ArrayList<>();
        CarrelloBean carrello = new CarrelloBean();

        System.out.println("\nAbbiamo creato questi prodotti per te: aggiungi al carrello quelli che preferisci");

        switch (scelta) {
            case 1 -> generaCaso1(personalizzati);
            case 2 -> generaCaso2(personalizzati);
            case 3 -> generaCaso3(personalizzati);
            default ->
                    System.out.println("Scelta non valide.");
        }
        return personalizzati;
    }

    public void processaOrdine(CarrelloBean carrello) {
        try {
             List<String> nomiProdotti = carrello.getProdotti().stream()
                    .map(Prodotto::nome) // <-- ATTENZIONE: Se Prodotto è un record, si usa nome(), non getNome()
                    .toList();

            Ordine nuovoOrdine = new Ordine(
                    carrello.getEmailCliente(),
                    nomiProdotti,
                    StatoOrdine.IN_LAVORAZIONE
            );

            OrdineDAO.salvaOrdine(nuovoOrdine);
            System.out.println("Ordine processato con successo per: " + carrello.getEmailCliente());

        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dell'ordine: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("Errore: Dati del carrello incompleti.");
        }
    }

    private void generaCaso1(List<Prodotto> lista) {
        System.out.println("\n1) Shampoo: Hydra-Soft. Arricchito con olio di Argan per idratare in profondità.\n2) Maschera: Nutri-Gloss. Trattamento intensivo emolliente.\n3) Siero: Silk-Drop. Elimina l'effetto crespo istantaneamente.");
        selezionaProdotti(lista,
                new Prodotto("Shampoo", "Hydra-Soft", "Idratazione profonda"),
                new Prodotto("Maschera", "Nutri-Gloss", "Trattamento emolliente"),
                new Prodotto("Siero", "Silk-Drop", "Effetto seta")
        );
    }

    private void generaCaso2(List<Prodotto> lista) {
        System.out.println("\n1) Shampoo: Pure-Balance. Estratti di menta e argilla per purificare la cute.\n2) Maschera: Light-Touch. Idratazione leggera che non appesantisce.\n3) Siero: Fresh-Scalp. Riequilibrante del sebo a lunga durata.");
        selezionaProdotti(lista,
                new Prodotto("Shampoo", "Pure-Balance", "Purificante"),
                new Prodotto("Maschera", "Light-Touch", "Idratazione leggera"),
                new Prodotto("Siero", "Fresh-Scalp", "Riequilibrante")
        );
    }

    private void generaCaso3(List<Prodotto> lista) {
        System.out.println("\n1) Shampoo: Universal-Care. Detergente delicato per uso quotidiano.\n2) Maschera: Basic-Repair. Protezione standard per tutti i tipi di capelli.\n3) Siero: Shine-Boost. Per una lucentezza naturale.");
        selezionaProdotti(lista,
                new Prodotto("Shampoo", "Universal-Care", "Detergente delicato"),
                new Prodotto("Maschera", "Basic-Repair", "Protezione standard"),
                new Prodotto("Siero", "Shine-Boost", "Lucentezza naturale")
        );
    }

    private void selezionaProdotti(List<Prodotto> lista, Prodotto p1, Prodotto p2, Prodotto p3) {
        /*boolean continua = true;
        while (continua && lista.size() < 3) {
            int input = GestioneInputCLI.leggiInt("Inserisci ID prodotto: ");

            Prodotto scelto = null;
            switch (input) {
                case 0 -> continua = false;
                case 1 -> scelto = p1;
                case 2 -> scelto = p2;
                case 3 -> scelto = p3;
                default -> System.out.println("Opzione non valida.");
            }

            if (scelto != null) {
                // IL CONTROLLO CRUCIALE:
                // Se la lista non contiene già il prodotto scelto, lo aggiunge.
                if (!lista.contains(scelto)) {
                    lista.add(scelto);
                    System.out.println("Aggiunto: " + scelto.nome());
                } else {
                    // Se l'utente clicca due volte, riceve questo feedback
                    System.out.println("Hai già aggiunto " + scelto.nome() + " al carrello.");
                }
            }*/

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
                    default -> System.out.println("Opzione non valida.");
                }
            }
    }
}


