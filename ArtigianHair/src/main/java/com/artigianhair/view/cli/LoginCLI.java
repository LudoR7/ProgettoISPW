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
        System.out.println("Benvenuto da ArtigianHair...");
        while(valido){
            System.out.println("\n1) Login");
            System.out.println("2) Registrazione");
            System.out.println("3) Logout");

            int input = GestioneInputCLI.leggiInt(" Scegli un'opzione: ");
            switch (input){
                case 1:
                    System.out.println("Inizio Login");
                    eseguiLogin();
                    break;
                case 2:
                    eseguiRegistrazione();
                    break;
                case 3:
                    valido = false;
                default:
                    System.out.println("opzione non valida");

            }
        }
    }
    private void eseguiRegistrazione(){
        UserBean userBean = new UserBean();
        System.out.println("REGISTRAZIONE: ");
        userBean.setNome(GestioneInputCLI.leggiString("Nome:  "));
        userBean.setCognome(GestioneInputCLI.leggiString("Cognome:  "));
        userBean.setEmail(GestioneInputCLI.leggiString("Email:  "));
        userBean.setPassword(GestioneInputCLI.leggiString("Password:  "));

        userBean.setRuolo(Ruolo.UTENTE);

        try{
            loginController.registraUtente(userBean);
            System.out.println("Registrazione eseguita");
        }catch (Exception e){
            System.out.println("Errore nella registrazione" + e.getMessage());
        }
    }

    private void eseguiLogin(){
        UserBean loginBean = new UserBean();
        System.out.println("LOGIN:  ");
        loginBean.setEmail(GestioneInputCLI.leggiString("Email:  "));
        loginBean.setPassword(GestioneInputCLI.leggiString("Password:  "));

        try{
            UserBean userLoggato = loginController.login(loginBean);
            System.out.println("Benvenuto: " + userLoggato.getNome() + " " + userLoggato.getCognome() + ".");
            new HomeCLI(userLoggato).start();
        }catch (LoginException e) {
            System.out.println("Errore nel login" + e.getMessage());
        }catch (Exception e){
            System.out.println("Errore nel caricamento dei dati");
        }
    }
}
