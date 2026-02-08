package com.artigianhair.controller;

import com.artigianhair.bean.UserBean;
import com.artigianhair.engineering.exception.LoginException;
import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.engineering.singleton.SessioneAttuale;
import com.artigianhair.model.User;
import com.artigianhair.persistence.dao.UserDAO;
import java.io.IOException;

public class LoginController {

    public boolean registraUtente(UserBean userBean) throws IOException, LoginException {

        UserDAO userDAO = DAOfactory.getUser();
        if (userDAO.findUserByEmail(userBean.getEmail()) != null) {
            throw new LoginException("\nATTENZIONE: l'email - " + userBean.getEmail() + " - esiste già.");
        }

        // Crea l'oggetto di tipo User a partire dai dati nel Bean
        User newUser = new User(userBean.getNome(), userBean.getCognome(), userBean.getEmail(), userBean.getPassword(), userBean.getRuolo());

        // Salvataggio dell'utente e gestione della sessione globale
        userDAO.saveUser(newUser);
        SessioneAttuale.getInstance().login(newUser);
        return true;
    }

    public boolean login(UserBean loginBean) throws LoginException, IOException {
        UserDAO userDAO = DAOfactory.getUser();
        User user = userDAO.findUserByEmailAndPassword(loginBean.getEmail(), loginBean.getPassword());
        if(user == null) {
            throw new LoginException("Credenziali non valide");
        }

        if(!user.getPassword().equals(loginBean.getPassword())) {
            throw new LoginException("Credenziali non valide: password errata.");

        }
        SessioneAttuale.getInstance().login(user);
        return true;
    }
    public boolean checkEmail(String email) throws LoginException {
        if (email == null || !email.contains("@")) {
            throw new LoginException("Email non valida, deve contenere una '@'. Riprova:");
        }else{
            return true;
        }
    }
    public boolean checkPassword(String password) throws LoginException {
        if (password == null || password.length() < 8) {
            throw new LoginException("La password deve avere almeno 8 caratteri");
        }else{
            return true;
        }
    }
}