package com.artigianhair;

import com.artigianhair.view.cli.LoginCLI;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import com.artigianhair.view.fx.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getLogger(Main.class.getName());
        String viewType = loadViewType();
        if ("CLI".equalsIgnoreCase(viewType)) {
            logger.info("Avvio interfaccia testuale: CLI...");
            LoginCLI loginCli = new LoginCLI();
            loginCli.start();

        }else{
            logger.info("Avvio interfaccia grafica: GUI...");
            launch(args);
        }
    }

    private static String loadViewType() throws IOException {
        Logger logger = Logger.getLogger(Main.class.getName());
        Properties properties = new Properties();
        try(InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.properties")){
            if(inputStream == null){
                logger.info("File not found. Default: CLI");
                return "CLI";
            }
            properties.load(inputStream);
            return properties.getProperty("view.type");
        }catch(IOException e){
            e.printStackTrace(); //da aggiustare dopo
            return "CLI";
        }
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setStage(primaryStage);
        primaryStage.setTitle("ArtigianHair - Parrucchiere Roma");
        SceneManager.changeScene("HomeGUI.fxml");
    }
}

