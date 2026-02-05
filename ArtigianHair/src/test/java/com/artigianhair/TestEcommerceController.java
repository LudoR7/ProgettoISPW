package com.artigianhair;

import com.artigianhair.controller.EcommerceController;
import com.artigianhair.bean.CarrelloBean;
import com.artigianhair.model.Prodotto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestEcommerceController {

    private EcommerceController ecommerceController;

    @BeforeEach
    void setUp() {
        ecommerceController = new EcommerceController();
    }

    @Test
    void testCreaOrdineCarrelloVuoto() {
        CarrelloBean carrelloVuoto = new CarrelloBean();
        carrelloVuoto.setEmailCliente("test@cliente.it");


        assertDoesNotThrow(() -> {
            boolean result = ecommerceController.processaOrdine(carrelloVuoto);
            assertFalse(result, "L'ordine non dovrebbe essere processato se il carrello è vuoto.");

        });
    }
    @Test
    void testProcessaOrdineSuccesso() {
        CarrelloBean carrello = new CarrelloBean();
        carrello.setEmailCliente("cliente@successo.it");
        carrello.addProdotto(new Prodotto("Shampoo", "Hydra-Soft", "Desc", 15.0));

        boolean esito = ecommerceController.processaOrdine(carrello);

        assertTrue(esito, "L'ordine con prodotti ed email dovrebbe avere successo.");
    }
}
