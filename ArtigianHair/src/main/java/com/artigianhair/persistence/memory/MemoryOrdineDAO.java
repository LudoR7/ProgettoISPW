package com.artigianhair.persistence.memory;

import com.artigianhair.model.Ordine;
import com.artigianhair.model.StatoOrdine;
import com.artigianhair.persistence.dao.OrdineDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Implementazione DAO per la gestione degli ordini in memoria
public class MemoryOrdineDAO implements OrdineDAO {
    private static final List<Ordine> ordini = new ArrayList<>();

    @Override
    public void salvaOrdine(Ordine ordine) {
        ordini.add(ordine);
    }
    public List<Ordine> findAll() {
        return new ArrayList<>(ordini);
    }
    public void aggiornaOrdine(Ordine ordine, StatoOrdine nuovoStato) throws IOException {
        boolean trovato = false;

        for (Ordine o : ordini) {
        // Verifica la corrispondenza dell'ordine basandosi su email cliente e lista prodotti
            if (o.getEmailCliente().equals(ordine.getEmailCliente()) && o.getProdotti().equals(ordine.getProdotti())) {o.setStato(nuovoStato);
                trovato = true;
                break;
            }
        }

        if (!trovato) {
            throw new IOException("Ordine non trovato nel database in memoria.");
        }
    }
}