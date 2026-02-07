package com.artigianhair.engineering.singleton;

import com.artigianhair.bean.CarrelloBean;
import com.artigianhair.model.User;

public class SessioneAttuale {
    private static SessioneAttuale sessioneAttuale = null;
    private User currentUser;
    private final CarrelloBean carrello = new CarrelloBean();

    private SessioneAttuale() {}

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
    public User getCurrentUser() {
        return this.currentUser;
    }
    public CarrelloBean getCarrello() {
        return carrello;
    }
}
