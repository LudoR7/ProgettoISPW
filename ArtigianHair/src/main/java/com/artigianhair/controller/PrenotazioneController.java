package com.artigianhair.controller;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.engineering.exception.PrenotazioneException;
import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.model.Appuntamento;
import com.artigianhair.persistence.dao.AppuntamentoDAO;

import java.time.LocalDate;
import java.time.LocalTime;

public class PrenotazioneController {
    public void confermaAppuntamento(AppuntamentoBean bean) throws PrenotazioneException {
        if(bean.getTrattamenti().isEmpty()||bean.getTrattamenti().size()>=4){
            throw new PrenotazioneException("Puoi selezionare massimo 3 trattamenti. ");
        }
        try{
            Appuntamento appuntamento = new Appuntamento(LocalDate.parse(bean.getData()), LocalTime.parse(bean.getOrario()), bean.getTrattamenti(), bean.getClienteEmail());
            AppuntamentoDAO dao = DAOfactory.getAppuntamentoDAO();
            dao.save(appuntamento);

            inviaEmailConferma(bean.getClienteEmail());
            notificaProprietaria(appuntamento);

        }catch (Exception e){
            throw new PrenotazioneException("Dati inseriti non validi");
        }
    }
    private void inviaEmailConferma(String email){
        System.out.println("E-mail di conferma inviata a: " + email);
    }
    private void notificaProprietaria(Appuntamento appuntamento){
        System.out.println("Notifica appuntamento del: " + appuntamento.getData() + ", inviata alla proprietaria");
    }
}
