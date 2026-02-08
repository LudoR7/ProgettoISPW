package com.artigianhair.controller;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.engineering.exception.AppuntamentoException;
import com.artigianhair.engineering.exception.PrenotazioneException;
import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.model.Appuntamento;
import com.artigianhair.view.cli.GestioneInputCLI;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PrenotazioneController {
    protected static final List<String> MESI_VALIDI = Arrays.asList("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre");

    //Gestisce la conferma di un appuntamento ricevuto tramite Bean. Valida i dati e crea l'oggetto.
    public void confermaAppuntamento(AppuntamentoBean bean) throws PrenotazioneException {
        try {
            if (bean.getOrario() == null) {
                throw new PrenotazioneException("Fascia oraria non specificata.");
            }

            // Converte il giorno testuale in intero
            int giorno = Integer.parseInt(bean.getData().trim());
            int meseIndice = -1;

            // Trova l'indice del mese corrispondente alla stringa nel bean
            for (int i = 0; i < MESI_VALIDI.size(); i++) {
                if (MESI_VALIDI.get(i).equalsIgnoreCase(bean.getMese())) {
                    meseIndice = i + 1;
                    break;
                }
            }

            if (meseIndice == -1) throw new PrenotazioneException("Mese non valido.");
            // Costruisce la data dell'appuntamento usando l'anno corrente
            LocalDate dataScelta = LocalDate.of(LocalDate.now().getYear(), meseIndice, giorno);
            // Determina l'orario effettivo
            LocalTime orarioScelto = bean.getOrario().equalsIgnoreCase("M")
                    ? LocalTime.of(9, 0)
                    : LocalTime.of(13, 0);
            // Crea l'istanza del modello Appuntamento
            Appuntamento appuntamento = new Appuntamento(dataScelta, orarioScelto, bean.getTrattamenti(), bean.getClienteEmail());
            // Persistenza tramite DAO ottenuto dalla Factory
            DAOfactory.getAppuntamentoDAO().save(appuntamento);
            // Invio notifiche
            inviaEmailConferma(bean.getClienteEmail());
            notificaProprietaria(bean);

        }catch (NumberFormatException e) {
            throw new PrenotazioneException("Il giorno inserito non è un numero valido.");
        }catch (Exception e){
            GestioneInputCLI.print("ERRORE");
            throw new PrenotazioneException("ERRORE");
        }
    }
    // Simula l'invio della mail di conferma
    private void inviaEmailConferma(String email){
        String s = String.format("E-mail di conferma inviata a: %s",email);
        GestioneInputCLI.print(s);
    }
    // Simula l'invio di una notifica
    private void notificaProprietaria(AppuntamentoBean appuntamento){
        GestioneInputCLI.print("Notifica appuntamento del: [ " + appuntamento.getData() + " ], inviata alla proprietaria");
    }
    // Recupera tutti gli appuntamenti associati a una specifica email.
    public List<AppuntamentoBean> recuperaAppuntamentiUtente(String email) throws AppuntamentoException, IOException {
        List<AppuntamentoBean> tuttiAppuntamenti = AgendaController.recuperaAppuntamenti();
        List<AppuntamentoBean> iMieiAppuntamenti = new ArrayList<>();

        for (AppuntamentoBean b : tuttiAppuntamenti) {
            if (b.getClienteEmail().equalsIgnoreCase(email)) {
                iMieiAppuntamenti.add(b);
            }
        }
        return iMieiAppuntamenti;
    }
}
