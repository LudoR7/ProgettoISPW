package com.artigianhair.bean;

import com.artigianhair.model.Prodotto;

import java.util.HashMap;

import java.util.Map;

public class CarrelloBean {
    private final Map<Prodotto, Integer> prodottiQuantita = new HashMap<>();
    private String emailCliente;

    public void addProdotto(Prodotto p) {
        prodottiQuantita.put(p, prodottiQuantita.getOrDefault(p, 0) + 1);
    }
    public Map<Prodotto, Integer> getProdottiConQuantita() {
        return prodottiQuantita;
    }

    public double getTotale() {
        return prodottiQuantita.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().prezzo() * entry.getValue())
                .sum();
    }
    public void svuota() {
        this.prodottiQuantita.clear();
    }

    public void setEmailCliente(String email) { this.emailCliente = email; }
    public String getEmailCliente() { return emailCliente; }
}