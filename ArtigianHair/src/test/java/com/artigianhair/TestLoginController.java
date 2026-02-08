package com.artigianhair;

import com.artigianhair.bean.UserBean;
import com.artigianhair.controller.LoginController;
import com.artigianhair.engineering.exception.LoginException;
import com.artigianhair.model.Ruolo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class TestLoginController {

    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginController = new LoginController();
    }

    private UserBean createTestUser(String email) {
        UserBean bean = new UserBean();
        bean.setNome("Mario");
        bean.setCognome("Rossi");
        bean.setEmail(email);
        bean.setPassword("password123"); // 11 caratteri (Valida)
        bean.setRuolo(Ruolo.UTENTE);
        return bean;
    }
    //Verifica avvenuta registrazione
    @Test
    void testRegistrazioneSuccesso() {
        UserBean newUser = createTestUser("new_user@test.com");
        assertDoesNotThrow(() -> {
            assertTrue(loginController.registraUtente(newUser));
        });
    }

    //Verifica fallimento per password troppo corta
    @Test
    void testRegistrazioneFallitaPasswordCorta() {
        UserBean user = createTestUser("short_pass@test.com");
        user.setPassword("12345");

        LoginException exception = assertThrows(LoginException.class, () -> loginController.checkPassword(user.getPassword()));
        assertTrue(exception.getMessage().contains("almeno 8 caratteri"));
    }

    //Verifica fallimento per email senza @
    @Test
    void testRegistrazioneFallitaEmailInvalida() {
        UserBean user = createTestUser("email_senza_at.com");

        LoginException exception = assertThrows(LoginException.class, () ->
                loginController.checkEmail(user.getEmail())
        );
        assertTrue(exception.getMessage().contains("Email non valida"));
    }
    //Verifica successo del Login
    @Test
    void testLoginSuccesso() throws LoginException, IOException {
        String email = "login_success@test.com";
        loginController.registraUtente(createTestUser(email));

        UserBean loginBean = new UserBean();
        loginBean.setEmail(email);
        loginBean.setPassword("password123");

        assertTrue(loginController.login(loginBean), "Il login dovrebbe riuscire.");
    }
    //Test password errata
    @Test
    void testLoginFallitoPasswordErrata() throws LoginException, IOException {
        String email = "wrong_pass@test.com";
        loginController.registraUtente(createTestUser(email));

        UserBean loginBean = new UserBean();
        loginBean.setEmail(email);
        loginBean.setPassword("wrong_password");

        assertThrows(LoginException.class, () -> loginController.login(loginBean));
    }
    //Test email già esistente
    @Test
    void testRegistrazioneEmailDuplicata() throws LoginException, IOException {
        String email = "duplicate@test.com";
        UserBean user = createTestUser(email);
        loginController.registraUtente(user);

        assertThrows(LoginException.class, () -> loginController.registraUtente(user));
    }
}