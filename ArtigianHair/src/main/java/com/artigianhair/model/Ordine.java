package com.artigianhair.model;

import java.io.Serializable;
import java.util.List;

public class Ordine implements Serializable {
    private final String emailCliente;
    private final List<String> prodotti;
    private StatoOrdine stato;

    public Ordine(String emailCliente, List<String> prodotti, StatoOrdine stato) {
        this.emailCliente = emailCliente;
        this.prodotti = prodotti;
        this.stato = stato;
    }

    public String getEmailCliente() {
        return emailCliente;
    }
    public List<String> getProdotti() {
        return prodotti;
    }
    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }
}
