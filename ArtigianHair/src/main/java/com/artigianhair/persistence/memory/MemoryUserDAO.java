package com.artigianhair.persistence.memory;

import com.artigianhair.model.User;
import com.artigianhair.persistence.dao.UserDAO;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private static final Map<String, User> users = new HashMap<>();

    @Override
    public void saveUser(User user) {
        users.put(user.getEmail(), user);
    }
}
