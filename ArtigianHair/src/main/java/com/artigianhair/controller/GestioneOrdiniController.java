package com.artigianhair.controller;

import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.model.Ordine;
import com.artigianhair.model.StatoOrdine;
import com.artigianhair.persistence.dao.OrdineDAO;
import java.io.IOException;
import java.util.List;

public class GestioneOrdiniController {
    private final OrdineDAO ordineDAO = DAOfactory.getOrdineDAO();

    public List<Ordine> visualizzaTuttiOrdini() throws IOException {
        return ordineDAO.findAll();
    }
    public void cambiaStatoOrdine(Ordine ordine, StatoOrdine nuovoStato) throws IOException {

        ordineDAO.aggiornaOrdine(ordine, nuovoStato);
    }
}
