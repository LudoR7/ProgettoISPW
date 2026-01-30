package com.artigianhair.controller;

import com.artigianhair.model.Ordine;
import com.artigianhair.model.StatoOrdine;
import com.artigianhair.persistence.fs.FileSystemOrdineDAO;
import java.io.IOException;
import java.util.List;

public class GestioneOrdiniController {
    private final FileSystemOrdineDAO ordineDAO = new FileSystemOrdineDAO();

    public List<Ordine> visualizzaTuttiOrdini() throws IOException {
        return ordineDAO.findAll();
    }
    public void cambiaStatoOrdine(Ordine ordine, StatoOrdine nuovoStato) throws IOException {

        ordineDAO.aggiornaOrdine(ordine, nuovoStato);
    }
}
