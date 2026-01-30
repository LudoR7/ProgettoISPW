package com.artigianhair.model;

public record Prodotto(String tipo, String nome, String descrizione) {
    @Override
    public String toString() {
        return tipo + ": " + nome + " (" + descrizione + ")";
    }
    public void getNome() {
        System.out.println(nome);
    }

}
