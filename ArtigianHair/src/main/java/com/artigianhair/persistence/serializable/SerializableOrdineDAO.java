package com.artigianhair.persistence.serializable;

import com.artigianhair.model.Ordine;
import com.artigianhair.model.StatoOrdine;
import com.artigianhair.persistence.dao.OrdineDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializableOrdineDAO implements OrdineDAO {
    private static final String FILE_NAME = "ordini.ser";
    private List<Ordine> ordini = new ArrayList<>();

    public SerializableOrdineDAO() {
        loadData();
    }

    @Override
    public void salvaOrdine(Ordine ordine) throws IOException {
        ordini.add(ordine);
        saveData();
    }

    public List<Ordine> findAll() {
        return new ArrayList<>(ordini);
    }

    public void aggiornaOrdine(Ordine ordineTarget, StatoOrdine nuovoStato) throws IOException {
        boolean trovato = false;
        for (Ordine o : ordini) {
            if (o.getEmailCliente().equals(ordineTarget.getEmailCliente()) && o.getProdotti().equals(ordineTarget.getProdotti())) {o.setStato(nuovoStato);
                trovato = true;
                break;
            }
        }

        if (trovato) {
            saveData();
        } else {
            throw new IOException("Ordine non trovato nel database serializzato.");
        }
    }

    private void saveData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(ordini);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                ordini = (List<Ordine>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                ordini = new ArrayList<>();
            }
        }
    }
}