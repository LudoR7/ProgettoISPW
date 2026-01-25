package com.artigianhair.model;

import com.artigianhair.bean.UserBean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Appuntamento implements Serializable {
    private final LocalDate data;
    private final LocalTime orario;
    private final List<String> trattamenti;
    private final String clienteEmail;

    public Appuntamento(LocalDate data, LocalTime orario, List<String> trattamenti, String clienteEmail) {
        this.data = data;
        this.orario = orario;
        this.trattamenti = trattamenti;
        this.clienteEmail = clienteEmail;
    }
    public LocalDate getData() {
        return data;
    }
    public LocalTime getOrario() {
        return orario;
    }
    public List<String> getTrattamenti() {
        return trattamenti;
    }
    public String getClienteEmail() {
        return clienteEmail;
    }
}
