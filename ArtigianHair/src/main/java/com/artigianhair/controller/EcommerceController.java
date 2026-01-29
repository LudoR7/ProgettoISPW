package com.artigianhair.controller;

import com.artigianhair.bean.CarrelloBean;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.Prodotto;
import com.artigianhair.view.cli.GestioneInputCLI;

import java.util.ArrayList;
import java.util.List;

public class EcommerceController {

    public List<Prodotto> generaProdottiPersonalizzati() {
        List<Prodotto> personalizzati = new ArrayList<>();
        CarrelloBean carrello = new CarrelloBean();

        int scelta = GestioneInputCLI.leggiInt("...");
        switch (scelta) {
            case 1 -> caso1(carrello);
            case 2 -> caso2(carrello);
            case 3 -> caso3(carrello);
            default ->
                    System.out.println("Scelta non valide.");
        }
        return personalizzati;
    }

    public void processaOrdine(CarrelloBean carrello) {
        // Qui andrebbe la logica di persistenza (es. salvataggio su CSV o DB)
        System.out.println("Ordine ricevuto per: " + carrello.getEmailCliente());
        System.out.println("Prodotti acquistati: " + carrello.getProdotti().size());
    }

    private void caso1(CarrelloBean carrello) {
        boolean b = true;
        int i = 0;
        boolean conteggio1 = false;
        boolean conteggio2 = false;
        boolean conteggio3 = false;

        System.out.println("\n1) Shampoo: Hydra-Soft. Arricchito con olio di Argan per idratare in profondità.");
        System.out.println("\n2) Maschera: Nutri-Gloss. Trattamento intensivo emolliente.");
        System.out.println("\n3) Siero: Silk-Drop. Elimina l'effetto crespo istantaneamente.");
        while(b && i<=3){
            int scelta = GestioneInputCLI.leggiInt("...");
            if(scelta == 0){
                b = false;
            }else if(scelta == 1 && !conteggio1){
                carrello.addProdotto(new Prodotto("Shampoo", "Pure-Balance", "Estratti di menta e argilla per purificare la cute."));
                conteggio1 = true;
                i++;
            }else if(scelta == 2 && !conteggio2){
                carrello.addProdotto(new Prodotto("Maschera", "Nutri-Gloss", "Trattamento intensivo emolliente."));
                conteggio2 = true;
                i++;
            }else if(scelta == 3 && !conteggio3){
                carrello.addProdotto(new Prodotto("Siero", "Silk-Drop", "Elimina l'effetto crespo istantaneamente."));
                conteggio3 = true;
                i++;
            }else{
                System.out.println("Opzione non valida, riprova");
            }

        }
    }

    private void caso2(CarrelloBean carrello) {
        boolean b = true;
        int i = 0;
        boolean conteggio1 = false;
        boolean conteggio2 = false;
        boolean conteggio3 = false;

        System.out.println("\n1) Shampoo: Pure-Balance. Estratti di menta e argilla per purificare la cute.");
        System.out.println("\n2) Maschera: Light-Touch. Idratazione leggera che non appesantisce.");
        System.out.println("\n3) Siero: Fresh-Scalp. Riequilibrante del sebo a lunga durata.");
        while(b && i<=3){
            int scelta = GestioneInputCLI.leggiInt("...");
            if(scelta == 0){
                b = false;
            }else if(scelta == 1 && !conteggio1){
                carrello.addProdotto(new Prodotto("Shampoo", "Hydra-Soft", "Arricchito con olio di Argan per idratare in profondità."));
                conteggio1 = true;
                i++;
            }else if(scelta == 2 && !conteggio2){
                carrello.addProdotto(new Prodotto("Maschera", "Light-Touch", "Idratazione leggera che non appesantisce."));
                conteggio2 = true;
                i++;
            }else if(scelta == 3 && !conteggio3){
                carrello.addProdotto(new Prodotto("Siero", "Fresh-Scalp", "Riequilibrante del sebo a lunga durata."));
                conteggio3 = true;
                i++;
            }else{
                System.out.println("Opzione non valida, riprova");
            }

        }
    }
    private void caso3(CarrelloBean carrello) {
        boolean b = true;
        int i = 0;
        boolean conteggio1 = false;
        boolean conteggio2 = false;
        boolean conteggio3 = false;



        System.out.println("\n1) Shampoo: Universal-Care. Detergente delicato per uso quotidiano.");
        System.out.println("\n2) Maschera: Basic-Repair. Protezione standard per tutti i tipi di capelli.");
        System.out.println("\n3) Siero: Shine-Boost. Per una lucentezza naturale.");
        while (b && i <= 2) {
            int scelta = GestioneInputCLI.leggiInt("...");
            if (scelta == 0) {
                b = false;
            } else if (scelta == 1 && !conteggio1) {
                carrello.addProdotto(new Prodotto("Shampoo", "Universal-Care", "Detergente delicato per uso quotidiano."));
                conteggio1 = true;
                i++;
            } else if (scelta == 2 && !conteggio2) {
                carrello.addProdotto(new Prodotto("Maschera", "Basic-Repair", "Protezione standard per tutti i tipi di capelli."));
                conteggio2 = true;
                i++;
            } else if (scelta == 3 && !conteggio3) {
                carrello.addProdotto(new Prodotto("Siero", "Shine-Boost", "Per una lucentezza naturale."));
                conteggio3 = true;
                i++;
            } else {
                System.out.println("Opzione non valida, riprova");
            }

        }
    }
}


