package com.artigianhair.view.cli;

import com.artigianhair.controller.LoginController;
import com.artigianhair.bean.UserBean;
import com.artigianhair.engineering.exception.LoginException;
import com.artigianhair.model.Ruolo;
import java.io.IOException;

public class LoginCLI {
    private final LoginController loginController = new LoginController();
    public void start(){
        boolean valido = true;

        while(valido){
            System.out.println("__________________________________________");
            System.out.println("[ Benvenuto da ArtigianHair ] \nScegli un'opzione:");
            System.out.println("\n1) Login");
            System.out.println("2) Registrazione");
            System.out.println("3) Logout");

            int input = GestioneInputCLI.leggiInt("...");
            switch (input){
                case 1:
                    System.out.println("\nInizio Login");
                    if(eseguiLogin()){
                        HomeCLI home = new HomeCLI();
                        home.start();
                    }
                    break;
                case 2:
                    eseguiRegistrazione();
                    break;
                case 3:
                    valido = false;
                case 4:
                    System.out.println("Nessun utente loggato al momento, impossibile eseguire il Logout.");
                    LoginCLI login = new LoginCLI();
                    login.start();

                default:
                    System.out.println("Opzione non valida");

            }
        }
    }
    private void eseguiRegistrazione(){
        UserBean userBean = new UserBean();
        System.out.println("\nREGISTRAZIONE: ");
        userBean.setNome(GestioneInputCLI.leggiString("Nome:  "));
        userBean.setCognome(GestioneInputCLI.leggiString("Cognome:  "));

    // metti un CONTROLLO su Email e Password
        userBean.setEmail(GestioneInputCLI.leggiString("Email:  "));
        userBean.setPassword(GestioneInputCLI.leggiString("Password:  "));

        userBean.setRuolo(Ruolo.UTENTE);

        try{
            loginController.registraUtente(userBean);
            System.out.println("\nRegistrazione eseguita. \nOra esegui il Login per accedere.");



        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private boolean eseguiLogin(){
        UserBean loginBean = new UserBean();
        System.out.println("\nLOGIN:  ");
        loginBean.setEmail(GestioneInputCLI.leggiString("Email:  "));
        loginBean.setPassword(GestioneInputCLI.leggiString("Password:  "));

        try{
            boolean succes = loginController.login(loginBean);
            if(succes){
                System.out.println("\nLogin eseguito");
                return true;
            }else{
                System.out.println("\nATTENZIONE: Credenziale non valide, riprova. ");
                return false;
            }
        }catch (LoginException e) {
            System.out.println("\nErrore nel login. " + e.getMessage());
        }catch (Exception e){
            System.out.println("\nErrore nel caricamento dei dati");
        }
        return false;
    }
}
