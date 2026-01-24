package com.artigianhair.persistence.fs;

import com.artigianhair.model.Ruolo;
import com.artigianhair.model.User;
import com.artigianhair.persistence.dao.UserDAO;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSystemUserDAO implements UserDAO {
    private static final String CSV_FILE_NAME = "utenti.csv";
    private static final Logger logger = Logger.getLogger(FileSystemUserDAO.class.getName());
    private final File file;

    public FileSystemUserDAO() {
        this.file = new File(CSV_FILE_NAME);
        if (!this.file.exists()) {
            try{
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveUser(User user) throws IOException {
        writeToFile(user);
    }
    private void writeToFile(User user) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String Line = String.format("%s,%s,%s,%s,%s", user.getCognome(), user.getNome(), user.getEmail(), user.getPassword(), user.getRuolo());
            writer.write(Line);
            writer.newLine();
        }catch (IOException ex){
            logger.log(Level.SEVERE, "Errore scrittura", ex);
        }

    }
    @Override
    public User findUserByEmail(String email) throws IOException {
        if(!file.exists()){
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if(data.length>=5 && data[2].equals(email)){
                   return new User(data[1], data[1], data[2], data[3], Ruolo.valueOf(data[4])) ;
                }
            }
        }
    return null;
    }

}
