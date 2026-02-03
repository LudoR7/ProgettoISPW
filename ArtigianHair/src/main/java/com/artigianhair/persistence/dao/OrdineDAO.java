package com.artigianhair.persistence.dao;

import com.artigianhair.model.Ordine;

import java.io.IOException;

public interface OrdineDAO {
    void salvaOrdine(Ordine ordine) throws IOException;
}