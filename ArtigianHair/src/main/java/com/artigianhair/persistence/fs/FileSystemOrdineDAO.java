package com.artigianhair.persistence.fs;

import com.artigianhair.model.Ordine;
import com.artigianhair.model.StatoOrdine;
import com.artigianhair.persistence.dao.OrdineDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileSystemOrdineDAO implements OrdineDAO {
    private static final String FILE_NAME = "ordini.csv";

    @Override
    public void salvaOrdine(Ordine ordine) throws IOException {
        List<Ordine> ordini = findAll();
        ordini.add(ordine);
        saveAll(ordini);
    }
    @Override
    public void aggiornaOrdine(Ordine ordineTarget, StatoOrdine nuovoStato) throws IOException {
        List<Ordine> tuttiGliOrdini = findAll();
        boolean trovato = false;

        for (Ordine o : tuttiGliOrdini) {
            if (o.getEmailCliente().equals(ordineTarget.getEmailCliente()) &&
                    o.getProdotti().equals(ordineTarget.getProdotti())) {
                o.setStato(nuovoStato);
                trovato = true;
                break;
            }
        }

        if (trovato) {
            saveAll(tuttiGliOrdini);
        } else {
            throw new IOException("Ordine non trovato nel database.");
        }
        saveAll(tuttiGliOrdini);
    }

    public void saveAll(List<Ordine> ordini) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Ordine o : ordini) {
                String riga = o.getEmailCliente() + "," + String.join(";", o.getProdotti()) + "," + o.getStato();
                writer.write(riga);
                writer.newLine();
            }
        }
    }
    @Override
    public List<Ordine> findAll() throws IOException {
        List<Ordine> ordini = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return ordini;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    List<String> prodotti = Arrays.asList(parts[1].split(" - "));
                    StatoOrdine stato = (parts.length >= 3) ? StatoOrdine.valueOf(parts[2]) : StatoOrdine.IN_LAVORAZIONE;
                    ordini.add(new Ordine(parts[0], prodotti, stato));
                }
            }
        }
        return ordini;
    }
}
