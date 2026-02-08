package com.artigianhair.view.cli;

import com.artigianhair.controller.LoginController;
import com.artigianhair.bean.UserBean;
import com.artigianhair.engineering.exception.LoginException;
import com.artigianhair.model.Ruolo;

import java.io.IOException;


public class LoginCLI {
    private final LoginController loginController = new LoginController();
    public void start() throws LoginException, IOException {
        boolean valido = true;

        while(valido){
            GestioneInputCLI.print("__________________________________________");
            GestioneInputCLI.print("[ Benvenuto da ArtigianHair ] \nScegli un'opzione:");
            GestioneInputCLI.print("\n1) Login");
            GestioneInputCLI.print("2) Registrazione");
            GestioneInputCLI.print("3) Esci");

            // Legge l'opzione scelta dall'utente
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
                    eseguiRegistrazione();
                    HomeCLI home = new HomeCLI();
                    home.start();
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
    private void eseguiRegistrazione() throws LoginException, IOException {
        UserBean userBean = new UserBean();
        GestioneInputCLI.print("\nREGISTRAZIONE: ");
        userBean.setNome(GestioneInputCLI.leggiString("Nome:  "));
        userBean.setCognome(GestioneInputCLI.leggiString("Cognome:  "));

        //Controllo il formato dell'email
        String email;
        while (true) {
            try{
                email = GestioneInputCLI.leggiString("Email:  ");
                if (loginController.checkEmail(email)) {
                    userBean.setEmail(email);
                    break;
                }
            }catch(LoginException e){
                GestioneInputCLI.print(e.getMessage());
            }
        }

        // Controllo sulla lunghezza minima della password
        String password;
        while (true) {
            password= GestioneInputCLI.leggiString("Password:  ");
            if(loginController.checkPassword(password)){
                userBean.setPassword(password);
                break;
            }
        }

        // Di default, le nuove registrazioni tramite CLI sono impostate come UTENTE
        userBean.setRuolo(Ruolo.UTENTE);

        while (true) {
            try {
                //Controllo del controller
                if (loginController.registraUtente(userBean)) {
                    GestioneInputCLI.print("\nRegistrazione eseguita con successo!");
                    break;
                }
            } catch (LoginException e) {
                GestioneInputCLI.print("\nErrore: " + e.getMessage() + " Riprova l'inserimento dell'email.");
                userBean.setEmail(GestioneInputCLI.leggiString("Nuova Email:  "));
            }
        }
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
