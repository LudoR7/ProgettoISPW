package com.artigianhair.view.cli;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.controller.AgendaController;

import java.io.IOException;
import java.util.List;

public class AgendaCLI {
    public void start(){
        GestioneInputCLI.print("AGENDA:   ");
        try {
            // Recupera la lista completa di tutti gli appuntamenti tramite il controller dedicato
            List<AppuntamentoBean> agenda = AgendaController.recuperaAppuntamenti();

            if(agenda.isEmpty()){
                GestioneInputCLI.print("Nessun appuntamento presente in agenda");
            } else {
                //Genera tabella per visualizzare gli appuntamenti
                String header = String.format("%-12s | %-8s | %-20s | %-20s", "DATA", "ORA", "CLIENTE", "TRATTAMENTI");
                GestioneInputCLI.print(header);
                GestioneInputCLI.print("-----------------------------------------------------------------------");

                //Stampa in dati per ogni riga
                for(AppuntamentoBean b : agenda){
                    String row = String.format("%-12s | %-8s | %-20s | %-20s", b.getData(), b.getOrario(), b.getClienteEmail(), String.join(", ", b.getTrattamenti()));
                    GestioneInputCLI.print(row);
                }
            }

        } catch (IOException e) {
            GestioneInputCLI.print("Errore nel recupero dei dati." + e.getMessage());
        }

        GestioneInputCLI.leggiString("\nPremi invio per tornare alla Home...");
    }
}