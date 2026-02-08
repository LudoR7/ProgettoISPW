package com.artigianhair.controller;

import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.model.Ordine;
import com.artigianhair.model.StatoOrdine;
import com.artigianhair.persistence.dao.OrdineDAO;
import java.io.IOException;
import java.util.List;

// Classe per la gestione degli ordini da parte della Proprietaria
public class GestioneOrdiniController {
    private final OrdineDAO ordineDAO = DAOfactory.getOrdineDAO();

    // Visualizzare l'insieme degli ordini effettuati
    public List<Ordine> visualizzaTuttiOrdini() throws IOException {
        return ordineDAO.findAll();
    }
    // Cambiare lo stato di un ordine
    public void cambiaStatoOrdine(Ordine ordine, StatoOrdine nuovoStato) throws IOException {
        ordineDAO.aggiornaOrdine(ordine, nuovoStato);
    }
}
