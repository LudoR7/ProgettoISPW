package com.artigianhair.persistence.dao;

import com.artigianhair.model.User;

import java.io.IOException;

public interface UserDAO {
    void saveUser(User user) throws IOException;
    User findUserByEmail(String email) throws IOException;
    User findUserByEmailAndPassword(String email, String password) throws IOException;
}
