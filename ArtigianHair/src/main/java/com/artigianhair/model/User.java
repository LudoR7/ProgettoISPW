package com.artigianhair.model;

public class User {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Ruolo ruolo;

    public User(String nome, String cognome, String email, String password, Ruolo ruolo){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public Ruolo getRuolo() {
        return ruolo;
    }
}
