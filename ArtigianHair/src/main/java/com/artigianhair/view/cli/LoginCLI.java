package com.artigianhair.view.cli;

import com.artigianhair.controller.LoginController;
import com.artigianhair.bean.UserBean;
import com.artigianhair.engineering.exception.LoginException;
import com.artigianhair.model.Ruolo;

import java.util.logging.Logger;


public class LoginCLI {
    Logger logger = Logger.getLogger(getClass().getName());
    private final LoginController loginController = new LoginController();
    public void start(){
        boolean valido = true;

        while(valido){
            logger.info("__________________________________________");
            logger.info("[ Benvenuto da ArtigianHair ] \nScegli un'opzione:");
            logger.info("\n1) Login");
            logger.info("2) Registrazione");
            logger.info("3) Esci");

            int input = GestioneInputCLI.leggiInt("...");
            switch (input){
                case 1:
                    logger.info("\nInizio Login");
                    if(eseguiLogin()){
                        HomeCLI home = new HomeCLI();
                        home.start();
                    }
                    break;
                case 2:
                    if(eseguiRegistrazione()){
                        HomeCLI home = new HomeCLI();
                        home.start();
                    }
                    break;
                case 3:
                    valido = false;
                    break;
                case 4:
                    logger.info("Nessun utente loggato al momento, impossibile eseguire il Logout.");
                    LoginCLI login = new LoginCLI();
                    login.start();
                    break;

                default:
                    logger.info("Opzione non valida");

            }
        }
    }
    private boolean eseguiRegistrazione(){
        UserBean userBean = new UserBean();
        logger.info("\nREGISTRAZIONE: ");
        userBean.setNome(GestioneInputCLI.leggiString("Nome:  "));
        userBean.setCognome(GestioneInputCLI.leggiString("Cognome:  "));

    // metti un CONTROLLO su Email e Password
        userBean.setEmail(GestioneInputCLI.leggiString("Email:  "));
        userBean.setPassword(GestioneInputCLI.leggiString("Password:  "));

        userBean.setRuolo(Ruolo.UTENTE);

        try{
            boolean b = loginController.registraUtente(userBean);
            if(b){
                logger.info("\nRegistrazione eseguita");
                return true;
            }else{
                logger.info("\nATTENZIONE: Credenziale non valide, riprova. ");
                return false;
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return false;
    }

    private boolean eseguiLogin(){
        UserBean loginBean = new UserBean();
        logger.info("\nLOGIN:  ");
        loginBean.setEmail(GestioneInputCLI.leggiString("Email:  "));
        loginBean.setPassword(GestioneInputCLI.leggiString("Password:  "));

        try{
            boolean succes = loginController.login(loginBean);
            if(succes){
                logger.info("\nLogin eseguito");
                return true;
            }else{
                logger.info("\nATTENZIONE: Credenziale non valide, riprova. ");
                return false;
            }
        }catch (LoginException e) {
            logger.info("\nErrore nel login. " + e.getMessage());
        }catch (Exception e){
            logger.info("\nErrore nel caricamento dei dati");
        }
        return false;
    }
}
