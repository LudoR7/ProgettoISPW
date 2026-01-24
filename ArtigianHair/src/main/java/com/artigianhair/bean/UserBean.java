package com.artigianhair.bean;
import com.artigianhair.model.Ruolo;

public class UserBean {
    private String email;
    private String password;
    private String nome;
    private String cognome;
    private Ruolo ruolo;


    public UserBean() {

    }

    public String getEmail() {
        return email; }
    public void setEmail(String email) {
        this.email = email; }

    public String getPassword() {
        return password; }
    public void setPassword(String password) {
        this.password = password; }

    public String getNome() {
        return nome; }
    public void setNome(String nome) {
        this.nome = nome; }

    public String getCognome() {
        return cognome; }
    public void setCognome(String cognome) {
        this.cognome = cognome; }

    public Ruolo getRuolo() {
        return ruolo; }
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo; }
}