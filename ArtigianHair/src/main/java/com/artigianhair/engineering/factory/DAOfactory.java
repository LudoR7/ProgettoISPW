package com.artigianhair.engineering.factory;

import com.artigianhair.model.User;
import com.artigianhair.persistence.dao.UserDAO;
import com.artigianhair.persistence.fs.FileSystemUserDAO;
import com.artigianhair.persistence.fs.SerializableUserDAO;
import com.artigianhair.persistence.memory.MemoryUserDAO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOfactory {
    private static final Logger logger = Logger.getLogger(DAOfactory.class.getName());
    private static final String CONFIGURATION_FILE = "DAOfactory.properties";
    private static final String PERSISTENCE_TYPE_KEY = "persistence.type";

    private DAOfactory() {}

    public static UserDAO getUser() throws IOException {
        String type = readPersistenceTypeFromConfiguration();

        if("DEMO".equalsIgnoreCase(type)){
            return new MemoryUserDAO();
        }
        else if("FS".equalsIgnoreCase(type)){
            return new FileSystemUserDAO();
        }else if("SER".equalsIgnoreCase(type)){
            return new SerializableUserDAO();
        }else{
            throw new IllegalArgumentException(type + " non è un tipo valido");
        }
    }



    private static String readPersistenceTypeFromConfiguration() throws IOException {
        Properties properties = new Properties();

        try (InputStream inputStream = DAOfactory.class.getClassLoader().getResourceAsStream(CONFIGURATION_FILE)) {
            if (inputStream == null) {
                logger.warning("File " + CONFIGURATION_FILE + " non trovato" + ", impostato a DEMO");
                return "DEMO";
            }
            properties.load(inputStream);
            return properties.getProperty(PERSISTENCE_TYPE_KEY);
        }catch (IOException e) {
            logger.log(Level.WARNING, "File illeggibile: " + e);
            return "DEMO";
        }

    }
}
