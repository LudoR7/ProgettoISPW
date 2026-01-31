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
        if (userDAO.findUserByEmail(userBean.getEmail(), userBean.getPassword()) != null) {
            throw new LoginException("\nATTENZIONE: l'email - " + userBean.getEmail() + " - esiste già.");
        }
        User newUser = new User(
                userBean.getNome(),
                userBean.getCognome(),
                userBean.getEmail(),
                userBean.getPassword(),
                userBean.getRuolo()
        );

        userDAO.saveUser(newUser);

        UserDAO userRegistratoDAO = DAOfactory.getUser();
        User user = userRegistratoDAO.findUserByEmail(userBean.getEmail(), userBean.getPassword());
        SessioneAttuale.getInstance().login(user);

        if(user.getPassword().equals(userBean.getPassword())) {
            SessioneAttuale.getInstance().login(user);
            return true;
        }
        return false;
    }

    public boolean login(UserBean loginBean) throws LoginException, IOException {
        UserDAO userDAO = DAOfactory.getUser();
        User user = userDAO.findUserByEmail(loginBean.getEmail(), loginBean.getPassword());
        if(user == null) {
            throw new LoginException("Credenziali non valide");
        }

        if(user.getPassword().equals(loginBean.getPassword())) {
            SessioneAttuale.getInstance().login(user);
            return true;
        }
        return false;
    }
}