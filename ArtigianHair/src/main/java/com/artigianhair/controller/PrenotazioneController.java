package com.artigianhair.controller;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.engineering.exception.PrenotazioneException;
import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.model.Appuntamento;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.artigianhair.view.cli.PrenotazioneCLI.MESI_VALIDI;

public class PrenotazioneController {
    Logger logger = Logger.getLogger(getClass().getName());
    public void confermaAppuntamento(AppuntamentoBean bean) throws PrenotazioneException {
        try {
            if (bean.getOrario() == null) {
                throw new PrenotazioneException("Fascia oraria non specificata.");
            }

            int giorno = Integer.parseInt(bean.getData().trim());
            int meseIndice = -1;
            for (int i = 0; i < MESI_VALIDI.size(); i++) {
                if (MESI_VALIDI.get(i).equalsIgnoreCase(bean.getMese())) {
                    meseIndice = i + 1;
                    break;
                }
            }

            if (meseIndice == -1) throw new PrenotazioneException("Mese non valido.");

            LocalDate dataScelta = LocalDate.of(LocalDate.now().getYear(), meseIndice, giorno);

            LocalTime orarioScelto = bean.getOrario().equalsIgnoreCase("M")
                    ? LocalTime.of(9, 0)
                    : LocalTime.of(13, 0);

            Appuntamento appuntamento = new Appuntamento(
                    dataScelta,
                    orarioScelto,
                    bean.getTrattamenti(),
                    bean.getClienteEmail()
            );

            DAOfactory.getAppuntamentoDAO().save(appuntamento);
            inviaEmailConferma(bean.getClienteEmail());
            notificaProprietaria(appuntamento);

        }catch (NumberFormatException e) {
            throw new PrenotazioneException("Il giorno inserito non è un numero valido.");
        }catch (Exception e){
            logger.info("erooresss");
            throw new PrenotazioneException("Erroresss");
        }
    }

    private void inviaEmailConferma(String email){
        logger.info( String.format("E-mail di conferma inviata a: %s",email));
    }
    private void notificaProprietaria(Appuntamento appuntamento){
        logger.info("Notifica appuntamento del: " + appuntamento.getData() + ", inviata alla proprietaria");
    }

    public List<AppuntamentoBean> recuperaAppuntamentiUtente(String email) throws Exception {
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
