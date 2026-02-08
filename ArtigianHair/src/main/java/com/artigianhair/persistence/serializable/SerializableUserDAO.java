package com.artigianhair.persistence.serializable;

import com.artigianhair.model.User;
import com.artigianhair.persistence.dao.UserDAO;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SerializableUserDAO implements UserDAO {
    private static final String FILE_NAME = "utenti.ser";
    private Map<String, User> users = new HashMap<>();

    public SerializableUserDAO() {
        // Carica i dati dal file quando l'oggetto viene istanziato
        loadData();
    }

    //Aggiunge un utente alla mappa e salva l'intera mappa aggiornata sul file.
    @Override
    public void saveUser(User user) throws IOException {
        users.put(user.getEmail(), user);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        }
    }

    @Override
    public User findUserByEmail(String email) throws IOException {
        return users.get(email);
    }
    @Override
    public User findUserByEmailAndPassword(String email, String password) throws IOException {
        return users.get(email);
    }


    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            // ObjectInputStream legge il flusso binario e ricostruisce l'oggetto originale
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (Map<String, User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                users = new HashMap<>();
            }
        }
    }
}