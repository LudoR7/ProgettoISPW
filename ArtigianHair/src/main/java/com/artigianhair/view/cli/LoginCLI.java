package com.artigianhair.view.cli;

import com.artigianhair.controller.LoginController;
import com.artigianhair.bean.UserBean;
import com.artigianhair.engineering.exception.LoginException;
import com.artigianhair.model.Ruolo;


public class LoginCLI {
    private final LoginController loginController = new LoginController();
    public void start(){
        boolean valido = true;

        while(valido){
            GestioneInputCLI.print("__________________________________________");
            GestioneInputCLI.print("[ Benvenuto da ArtigianHair ] \nScegli un'opzione:");
            GestioneInputCLI.print("\n1) Login");
            GestioneInputCLI.print("2) Registrazione");
            GestioneInputCLI.print("3) Esci");

            int input = GestioneInputCLI.leggiInt("...");
            switch (input){
                case 1:
                    GestioneInputCLI.print("\nInizio Login");
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
                    GestioneInputCLI.print("Nessun utente loggato al momento, impossibile eseguire il Logout.");
                    LoginCLI login = new LoginCLI();
                    login.start();
                    break;

                default:
                    GestioneInputCLI.print("Opzione non valida");

            }
        }
    }
    private boolean eseguiRegistrazione(){
        UserBean userBean = new UserBean();
        GestioneInputCLI.print("\nREGISTRAZIONE: ");
        userBean.setNome(GestioneInputCLI.leggiString("Nome:  "));
        userBean.setCognome(GestioneInputCLI.leggiString("Cognome:  "));

    // metti un CONTROLLO su Email e Password
        userBean.setEmail(GestioneInputCLI.leggiString("Email:  "));
        userBean.setPassword(GestioneInputCLI.leggiString("Password:  "));

        userBean.setRuolo(Ruolo.UTENTE);

        try{
            boolean b = loginController.registraUtente(userBean);
            if(b){
                GestioneInputCLI.print("\nRegistrazione eseguita");
                return true;
            }else{
                GestioneInputCLI.print("\nATTENZIONE: Credenziale non valide, riprova. ");
                return false;
            }
        }catch (Exception e){
            GestioneInputCLI.print(e.getMessage());
        }
        return false;
    }

    private boolean eseguiLogin(){
        UserBean loginBean = new UserBean();
        GestioneInputCLI.print("\nLOGIN:  ");
        loginBean.setEmail(GestioneInputCLI.leggiString("Email:  "));
        loginBean.setPassword(GestioneInputCLI.leggiString("Password:  "));

        try{
            boolean succes = loginController.login(loginBean);
            if(succes){
                GestioneInputCLI.print("\nLogin eseguito");
                return true;
            }else{
                GestioneInputCLI.print("\nATTENZIONE: Credenziale non valide, riprova. ");
                return false;
            }
        }catch (LoginException e) {
            GestioneInputCLI.print("\nErrore nel login. " + e.getMessage());
        }catch (Exception e){
            GestioneInputCLI.print("\nErrore nel caricamento dei dati");
        }
        return false;
    }
}
