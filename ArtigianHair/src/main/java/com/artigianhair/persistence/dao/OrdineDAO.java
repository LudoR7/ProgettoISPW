package com.artigianhair.persistence.dao;

import com.artigianhair.model.Ordine;
import com.artigianhair.model.StatoOrdine;

import java.io.IOException;
import java.util.List;

public interface OrdineDAO {
    void salvaOrdine(Ordine ordine) throws IOException;
    List<Ordine> findAll() throws IOException;
    void aggiornaOrdine(Ordine ordineTarget, StatoOrdine nuovoStato) throws IOException;
}