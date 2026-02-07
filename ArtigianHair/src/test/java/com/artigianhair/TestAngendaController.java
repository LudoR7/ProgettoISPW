package com.artigianhair;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.controller.AgendaController;
import com.artigianhair.controller.PrenotazioneController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TestAgendaController {

    private PrenotazioneController prenotazioneController;

    @BeforeEach
    void setUp() {
        prenotazioneController = new PrenotazioneController();
    }

    @Test
    void testRecuperoAppuntamentiSalvati() throws Exception {
        AppuntamentoBean bean = new AppuntamentoBean();
        bean.setData("20");
        bean.setMese("Giugno");
        bean.setOrario("P");
        bean.setClienteEmail("agenda@test.com");
        bean.addTrattamento("Taglio");

        prenotazioneController.confermaAppuntamento(bean);

        List<AppuntamentoBean> lista = AgendaController.recuperaAppuntamenti();

        boolean trovato = lista.stream().anyMatch(a -> a.getClienteEmail().equals("agenda@test.com") && a.getData().equals("20"));

        assertTrue(trovato, "L'appuntamento salvato dovrebbe essere presente nell'agenda.");
    }
}
