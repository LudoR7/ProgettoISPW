package com.artigianhair.persistence.dao;

import com.artigianhair.engineering.exception.PrenotazioneException;
import com.artigianhair.model.Appuntamento;

import java.io.IOException;
import java.util.List;

public interface AppuntamentoDAO {
    void save(Appuntamento appuntamento)throws IOException;
    List<Appuntamento> findAll() throws IOException;
    void delete(Appuntamento appuntamento) throws IOException;
}
