package com.artigianhair.view.cli;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.controller.AgendaController;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AgendaCLI {
    Logger logger = Logger.getLogger(getClass().getName());

    public void start(){
        logger.info("AGENDA:   ");
        try {
            List<AppuntamentoBean> agenda = AgendaController.recuperaAppuntamenti();

            if(agenda.isEmpty()){
                logger.info("Nessun appuntamento presente in agenda");
            } else {
                String header = String.format("%-12s | %-8s | %-20s | %-20s", "DATA", "ORA", "CLIENTE", "TRATTAMENTI");
                logger.info(header);
                logger.info("-----------------------------------------------------------------------");
                for(AppuntamentoBean b : agenda){
                    String row = String.format("%-12s | %-8s | %-20s | %-20s", b.getData(), b.getOrario(), b.getClienteEmail(), String.join(", ", b.getTrattamenti()));
                    logger.info(row);
                }
            }

        } catch (IOException e) {
            logger.info("Errore nel recupero dei dati." + e.getMessage());
        }

        GestioneInputCLI.leggiString("\nPremi invio per tornare alla Home...");
    }
}