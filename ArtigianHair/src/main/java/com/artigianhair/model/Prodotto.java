package com.artigianhair.model;

public record Prodotto(String tipo, String nome, String descrizione, double prezzo) {
    @Override
    public String toString() {
        return tipo + ": " + nome + " (" + descrizione + ") - €" + String.format("%.2f", prezzo);
    }
    public void getNome() {
        System.out.println(nome);
    }
}