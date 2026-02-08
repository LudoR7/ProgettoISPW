package com.artigianhair.engineering.factory;

import com.artigianhair.persistence.dao.AppuntamentoDAO;
import com.artigianhair.persistence.dao.OrdineDAO;
import com.artigianhair.persistence.dao.UserDAO;
import com.artigianhair.persistence.fs.FileSystemAppuntamentoDAO;
import com.artigianhair.persistence.fs.FileSystemOrdineDAO;
import com.artigianhair.persistence.fs.FileSystemUserDAO;
import com.artigianhair.persistence.memory.MemoryOrdineDAO;
import com.artigianhair.persistence.serializable.SerializableAppuntamentoDAO;
import com.artigianhair.persistence.serializable.SerializableOrdineDAO;
import com.artigianhair.persistence.serializable.SerializableUserDAO;
import com.artigianhair.persistence.memory.MemoryAppuntamentoDAO;
import com.artigianhair.persistence.memory.MemoryUserDAO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

//Classe per la gestione della persistenza dei dati
public class DAOfactory {
    private static final Logger logger = Logger.getLogger(DAOfactory.class.getName());
    private static final String CONFIGURATION_FILE = "config.properties"; //File di configurazione esterno
    private static final String PERSISTENCE_TYPE_KEY = "persistence.type"; //Tipo di persistenza

    private DAOfactory() {}

    public static UserDAO getUser() {
        String type = readPersistenceTypeFromConfiguration();

        if("DEMO".equalsIgnoreCase(type)){
            return new MemoryUserDAO(); //Dati volatili in memoria
        }
        else if("FS".equalsIgnoreCase(type)){
            return new FileSystemUserDAO(); // Persistenza su file CSV
        }else if("SER".equalsIgnoreCase(type)){
            return new SerializableUserDAO(); // Persistenza tramite serializzazione
        }else{
            throw new IllegalArgumentException(type + " non è un tipo valido");
        }
    }



    private static String readPersistenceTypeFromConfiguration() {
        Properties properties = new Properties();

        try (InputStream inputStream = DAOfactory.class.getClassLoader().getResourceAsStream(CONFIGURATION_FILE)) {
            if (inputStream == null) {
                logger.warning("File " + CONFIGURATION_FILE + " non trovato" + ", impostato a DEMO");
                return "DEMO";
            }
            properties.load(inputStream);
            return properties.getProperty(PERSISTENCE_TYPE_KEY);
        }catch (IOException e) {
            logger.warning( () -> "File illeggibile: %s" + e.getMessage());
            return "DEMO";
        }

    }

    //Implementazione DAO per gli appuntamenti
    public static AppuntamentoDAO getAppuntamentoDAO() {
        String type = readPersistenceTypeFromConfiguration();
        return switch (type.toUpperCase()){
            case "DEMO" -> new MemoryAppuntamentoDAO();
            case "FS" -> new FileSystemAppuntamentoDAO();
            case "SER" -> new SerializableAppuntamentoDAO();
            default ->  new MemoryAppuntamentoDAO();
        };
    }

    //Implementazione DAO per la gestione degli ordini
    public static OrdineDAO getOrdineDAO() {
        String type = readPersistenceTypeFromConfiguration();
        return switch (type.toUpperCase()) {
            case "DEMO" -> new MemoryOrdineDAO();
            case "FS" -> new FileSystemOrdineDAO();
            case "SER" -> new SerializableOrdineDAO();
            default -> new MemoryOrdineDAO();
        };
    }
}
