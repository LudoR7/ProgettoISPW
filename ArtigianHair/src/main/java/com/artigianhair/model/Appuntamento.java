package com.artigianhair.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Appuntamento implements Serializable {

    private static final long serialVersionUID = 1L;//check se serve


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

    public String getFasciaOraria() {
        if (orario.isBefore(LocalTime.of(13, 0))) {
            return "M";
        } else {
            return "P";
        }
    }

    @Override
    public String toString() {
        return "Appuntamento{" +
                "data=" + data +
                ", orario=" + orario +
                ", trattamenti=" + trattamenti +
                ", clienteEmail='" + clienteEmail + '\'' +
                '}';
    }
}
