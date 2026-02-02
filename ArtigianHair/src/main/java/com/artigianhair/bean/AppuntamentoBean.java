package com.artigianhair.bean;

import java.util.ArrayList;
import java.util.List;

public class AppuntamentoBean {
    private String data;
    private String mese;
    private String orario;
    private List<String> trattamenti = new ArrayList<>();
    private String clienteEmail;

    public AppuntamentoBean() {}

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getMese() {
        return mese;
    }
    public void setMese(String mese) {
        this.mese = mese;
    }


    public String getOrario() {
        return orario;
    }
    public void setOrario(String orario) {
        this.orario = orario;
    }
    public List<String> getTrattamenti() { return trattamenti;}

    public void setTrattamenti(List<String> trattamenti) {
        this.trattamenti = trattamenti;
    }
    public String getClienteEmail() { return clienteEmail;}
    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public void addTrattamento(String t) {
        trattamenti.add(t);
    }
}
