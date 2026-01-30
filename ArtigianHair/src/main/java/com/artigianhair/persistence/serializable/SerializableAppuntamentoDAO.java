package com.artigianhair.persistence.serializable;

import com.artigianhair.model.Appuntamento;
import com.artigianhair.persistence.dao.AppuntamentoDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializableAppuntamentoDAO implements AppuntamentoDAO {
    private static final String FILE_NAME = "appuntamenti.ser";

    @Override
    @SuppressWarnings("unchecked")
    public void save(Appuntamento appuntamento) throws IOException {
        List<Appuntamento> list = findAll();
        list.add(appuntamento);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
            oos.writeObject(list);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Appuntamento> findAll() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (List<Appuntamento>) ois.readObject();
        }catch (ClassNotFoundException e){
            return new ArrayList<>();
        }
    }

    @Override
    public void delete(Appuntamento appuntamento) throws IOException {
        List<Appuntamento> list = findAll();

        boolean rimosso = list.removeIf(a -> a.getData().equals(appuntamento.getData()) &&
                a.getOrario().equals(appuntamento.getOrario()) &&
                a.getClienteEmail().equalsIgnoreCase(appuntamento.getClienteEmail()));


        if (rimosso) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(list);
            }
        }
    }
}
