package com.artigianhair.model;

import java.io.Serializable;

public class User implements Serializable {
    private final String nome;
    private final String cognome;
    private final String email;
    private final String password;
    private final Ruolo ruolo;

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
