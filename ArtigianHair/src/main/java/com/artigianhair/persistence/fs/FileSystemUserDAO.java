package com.artigianhair.persistence.fs;

import com.artigianhair.model.Ruolo;
import com.artigianhair.model.User;
import com.artigianhair.persistence.dao.UserDAO;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//Memorizza le informazioni degli utenti (nome, cognome, email, password, ruolo) in "utenti.csv".
public class FileSystemUserDAO implements UserDAO {
    private static final String CSV_FILE_NAME = "utenti.csv";
    private static final Logger logger = Logger.getLogger(FileSystemUserDAO.class.getName());
    private final File file;

    public FileSystemUserDAO() {
        this.file = new File(CSV_FILE_NAME);
        if (!this.file.exists()) {
            try{
                boolean result = file.createNewFile();

                if(result){
                    logger.info("Nuovo database utenti.csv creato!");
                }else {
                    logger.warning("Errore nella creazione utenti.csv!");
                }
            } catch (IOException e) {
                throw new IllegalStateException("Errore: impossibile creare il file utenti.csv!");
            }
        }
    }

    @Override
    public void saveUser(User user) throws IOException {
        writeToFile(user);
    }

    // Metodo per scrivere i dati di un nuovo utente,  in una nuova riga del file CVS
    private void writeToFile(User user) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String line = String.format("%s,%s,%s,%s,%s", user.getNome(), user.getCognome(), user.getEmail(), user.getPassword(), user.getRuolo());
            writer.write(line);
            writer.newLine();
        }catch (IOException ex){
            logger.log(Level.SEVERE, "Errore scrittura", ex);
        }

    }

    //Cerca un utente verificando sia l'email che la password (usato per il Login).
    @Override
    public User findUserByEmailAndPassword(String email, String password) throws IOException {
        if(!file.exists()){
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if(data.length>=5 && data[2].equals(email) && data[3].equals(password)){
                   return new User(data[0], data[1], data[2], data[3], Ruolo.valueOf(data[4])) ;
                }
            }
        }
    return null;
    }

    //Cerca un utente solo tramite l'email (usato per verificare se l'email è già stata usata durante la registrazione).
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
                    return new User(data[0], data[1], data[2], data[3], Ruolo.valueOf(data[4])) ;
                }
            }
        }
        return null;
    }


}
