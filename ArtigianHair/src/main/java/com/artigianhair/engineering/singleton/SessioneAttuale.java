package com.artigianhair.engineering.singleton;

import com.artigianhair.bean.CarrelloBean;
import com.artigianhair.model.User;


//Classe per l'implementazione del pattern Singleton
public class SessioneAttuale {

    // L'unica istanza della classe, inizialmente nulla
    private static SessioneAttuale sessioneAttuale = null;

    // L'utente correntemente loggato nel sistema
    private User currentUser;
    private final CarrelloBean carrello = new CarrelloBean();

    private SessioneAttuale() {}

    //Metodo statico per ottenere l'unica istanza della classe
    public static SessioneAttuale getInstance() {
        if (sessioneAttuale == null) {
            sessioneAttuale = new SessioneAttuale();
        }
        return sessioneAttuale;
    }
    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
        this.carrello.svuota();
    }

    //Restituisce l'utente attualmente autenticato
    public User getCurrentUser() {
        return this.currentUser;
    }
    public CarrelloBean getCarrello() {
        return carrello;
    }
}
