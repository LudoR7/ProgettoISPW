package com.artigianhair.view.cli;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.controller.AgendaController;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AgendaCLI {
    Logger logger = Logger.getLogger(getClass().getName());
    private final AgendaController agendaController = new AgendaController();

    public void start(){
        logger.info("AGENDA:   ");
        try{
            List<AppuntamentoBean> agenda = agendaController.recuperaAppuntamenti();

            if(agenda.isEmpty()){
                logger.info("Nessun appuntamento presente in agenda");
            }else{
                System.out.printf("%-12s | %-8s | %-20s | %-20s%n", "DATA", "ORA", "CLIENTE", "TRATTAMENTI");
                logger.info("-----------------------------------------------------------------------");
                for(AppuntamentoBean b : agenda){
                    System.out.printf("%-12s | %-8s | %-20s | %-20s%n", b.getData(), b.getOrario(), b.getClienteEmail(), String.join(", ", b.getTrattamenti()));
                }
            }



        } catch (IOException e) {
            logger.info("Errore nel recupero dei dati." + e.getMessage());
        }

        GestioneInputCLI.leggiString("\nPremi invio per tornare alla Home...");
    }
}
