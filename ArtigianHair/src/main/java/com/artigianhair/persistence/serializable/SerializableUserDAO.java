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
        loadData();
    }

    @Override
    public void saveUser(User user) throws IOException {
        users.put(user.getEmail(), user);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        }
    }

    @Override
    public User findUserByEmail(String email, String password) throws IOException {
        return users.get(email);
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (Map<String, User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                users = new HashMap<>();
            }
        }
    }
}