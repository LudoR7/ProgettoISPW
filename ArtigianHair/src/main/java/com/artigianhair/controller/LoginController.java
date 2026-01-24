package com.artigianhair.controller;

import com.artigianhair.bean.UserBean;
import com.artigianhair.engineering.exception.LoginException;
import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.model.User;
import com.artigianhair.persistence.dao.UserDAO;
import java.io.IOException;

public class LoginController {

    public void registraUtente(UserBean userBean) throws IOException {

        UserDAO userDAO = DAOfactory.getUser();

        User newUser = new User(
                userBean.getNome(),
                userBean.getCognome(),
                userBean.getEmail(),
                userBean.getPassword(),
                userBean.getRuolo()
        );

        userDAO.saveUser(newUser);
    }

    public UserBean login(UserBean loginBean) throws LoginException, IOException {
        UserDAO userDAO = DAOfactory.getUser();
        User user = userDAO.findUserByEmail(loginBean.getEmail());
        if(user == null || !user.getPassword().equals(loginBean.getPassword())) {
            throw new LoginException("Credenziali non valide");
        }

        UserBean sessionBean = new UserBean();
        sessionBean.setNome(loginBean.getNome());
        sessionBean.setCognome(loginBean.getCognome());
        sessionBean.setEmail(loginBean.getEmail());
        sessionBean.setPassword(loginBean.getPassword());

        return sessionBean;
    }
}