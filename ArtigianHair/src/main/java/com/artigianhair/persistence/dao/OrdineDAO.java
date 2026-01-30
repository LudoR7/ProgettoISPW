package com.artigianhair.persistence.dao;

import com.artigianhair.model.User;

import java.io.IOException;

public interface OrdineDAO {
    void salvaOrdine(User user) throws IOException;
}