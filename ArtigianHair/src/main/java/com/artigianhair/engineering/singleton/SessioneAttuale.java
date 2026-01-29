package com.artigianhair.engineering.singleton;

import com.artigianhair.model.User;

public class SessioneAttuale {
    private static SessioneAttuale sessioneAttuale = null;
    private User currentUser;
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
    }
    public User getCurrentUser() {
        return this.currentUser;
    }
}
