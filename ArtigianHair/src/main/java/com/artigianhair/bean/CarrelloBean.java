package com.artigianhair.bean;

import com.artigianhair.model.Prodotto;
import java.util.ArrayList;
import java.util.List;

public class CarrelloBean {
    private List<Prodotto> prodotti = new ArrayList<>();
    private String emailCliente;

    public void addProdotto(Prodotto p) { prodotti.add(p); }
    public List<Prodotto> getProdotti() { return prodotti; }
    public void setEmailCliente(String email) { this.emailCliente = email; }
    public String getEmailCliente() { return emailCliente; }
}