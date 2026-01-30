package com.artigianhair.persistence.fs;

import com.artigianhair.model.Appuntamento;
import com.artigianhair.persistence.dao.AppuntamentoDAO;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileSystemAppuntamentoDAO implements AppuntamentoDAO {
    private static final String FILE_NAME = "appuntamenti.csv";

    @Override
    public void save(Appuntamento appuntamento) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String line = String.format("%s,%s,%s,%s",
                    appuntamento.getData(),
                    appuntamento.getOrario(),
                    String.join(" - ", appuntamento.getTrattamenti()),
                    appuntamento.getClienteEmail());
            writer.write(line);
            writer.newLine();
        }
    }

    @Override
    public List<Appuntamento> findAll() throws IOException {
        List<Appuntamento> list = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; //salta le righe vuote

                String[] parts = line.split(",");
                if (parts.length == 4) {
                    list.add(new Appuntamento(
                            LocalDate.parse(parts[0]),
                            LocalTime.parse(parts[1]),
                            new ArrayList<>(Arrays.asList(parts[2].split(";"))), parts[3]));
                }
            }
        }

        return list;
    }
    @Override
    public void delete(Appuntamento appuntamento) throws IOException {
        List<Appuntamento> list = findAll();
        list.removeIf(a -> a.getData().equals(appuntamento.getData()) &&
                a.getOrario().equals(appuntamento.getOrario()) &&
                a.getClienteEmail().equalsIgnoreCase(appuntamento.getClienteEmail()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (Appuntamento a : list) {
                String line = String.format("%s,%s,%s,%s", a.getData(), a.getOrario(), String.join(" - ", a.getTrattamenti()), a.getClienteEmail());
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
