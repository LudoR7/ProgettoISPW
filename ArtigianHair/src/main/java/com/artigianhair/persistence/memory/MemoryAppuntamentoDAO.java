package com.artigianhair.persistence.memory;

import com.artigianhair.model.Appuntamento;
import com.artigianhair.persistence.dao.AppuntamentoDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemoryAppuntamentoDAO implements AppuntamentoDAO {
    private static final List<Appuntamento> agenda = new ArrayList<>();

    @Override
    public void save(Appuntamento appuntamento) throws IOException {
        agenda.add(appuntamento);
    }

    @Override
    public List<Appuntamento> findAll() throws IOException {
        return new ArrayList<>(agenda);
    }
    @Override
    public void delete(Appuntamento appuntamento) throws IOException {
        agenda.removeIf(a -> a.getData().equals(appuntamento.getData()) &&
                a.getOrario().equals(appuntamento.getOrario()) &&
                a.getClienteEmail().equalsIgnoreCase(appuntamento.getClienteEmail()));
    }
}
