package com.artigianhair;

import com.artigianhair.view.cli.HomeCLI;
import com.artigianhair.view.cli.LoginCLI;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        String viewType = loadViewType();
        if ("CLI".equalsIgnoreCase(viewType)) {
            System.out.println("Avvio interfaccia testuale: CLI...");
            new LoginCLI().start();

        }else{
            System.out.println("Avvio interfaccia grafica: GUI...");
        }
    }

    private static String loadViewType() throws IOException {
        Properties properties = new Properties();
        try(InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.properties")){
            if(inputStream == null){
                System.out.println("File not found. Default: CLI");
                return "CLI";
            }
            properties.load(inputStream);
            return properties.getProperty("view.Type");
        }catch(IOException e){
            e.printStackTrace(); //da aggiustare dopo
            return "CLI";
        }
    }
}

