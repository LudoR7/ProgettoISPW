package com.artigianhair.controller;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.engineering.exception.PrenotazioneException;
import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.model.Appuntamento;


import java.time.LocalDate;
import java.time.LocalTime;

import static com.artigianhair.view.cli.PrenotazioneCLI.MESI_VALIDI;

public class PrenotazioneController {
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

            // Trasforma la fascia "M"/"P" in LocalTime per il modello
            LocalTime orarioScelto = bean.getOrario().equalsIgnoreCase("M")
                    ? LocalTime.of(9, 0)
                    : LocalTime.of(13, 0);

            Appuntamento appuntamento = new Appuntamento(
                    dataScelta,
                    orarioScelto,
                    bean.getTrattamenti(),
                    bean.getClienteEmail()
            );


            /*
            int giorno = Integer.parseInt(bean.getData().trim());
            int meseIndice = -1;
            for (int i = 0; i < MESI_VALIDI.size(); i++) {
                if (MESI_VALIDI.get(i).equalsIgnoreCase(bean.getMese())) {
                    meseIndice = i + 1;
                    break;
                }
            }
            LocalDate dataScelta = LocalDate.of(LocalDate.now().getYear(), meseIndice, giorno);

            LocalTime orarioScelto = bean.getOrario().equalsIgnoreCase("M")
                    ? LocalTime.of(9, 0)
                    : LocalTime.of(15, 0);

            Appuntamento appuntamento = new Appuntamento(
                    dataScelta,
                    orarioScelto,
                    bean.getTrattamenti(),
                    bean.getClienteEmail()
            );
            DAOfactory.getAppuntamentoDAO().save(appuntamento);
            */
            /*Appuntamento appuntamento = new Appuntamento(mese, giorno, fascia, bean.getTrattamenti(), bean.getClienteEmail());
            AppuntamentoDAO dao = DAOfactory.getAppuntamentoDAO();
            dao.save(appuntamento);
            int meseIndice = -1;
            for (int i = 0; i < MESI_VALIDI.size(); i++) {
                if (MESI_VALIDI.get(i).equalsIgnoreCase(bean.getMese())) {
                    meseIndice = i + 1;
                    break;
                }
            }

            int giorno = Integer.parseInt(bean.getData());
            int anno = LocalDate.now().getYear();
            LocalDate dataScelta = LocalDate.of(anno, meseIndice, giorno);

            // 3. Trasformazione Fascia Oraria in LocalTime
            // Se l'utente ha inserito "M" usiamo le 09:00, altrimenti (P) le 15:00
            LocalTime orarioScelto = bean.getOrario().equalsIgnoreCase("M")
                    ? LocalTime.of(9, 0)
                    : LocalTime.of(15, 0);

            // 4. Creazione Appuntamento (Costruttore a 4 parametri)
            Appuntamento appuntamento = new Appuntamento(dataScelta, orarioScelto, bean.getTrattamenti(), bean.getClienteEmail());

            // 5. Salvataggio tramite DAO
            AppuntamentoDAO dao = DAOfactory.getAppuntamentoDAO();
            dao.save(appuntamento);*/

            DAOfactory.getAppuntamentoDAO().save(appuntamento);
            inviaEmailConferma(bean.getClienteEmail());
            notificaProprietaria(appuntamento);

        }catch (NumberFormatException e) {
            throw new PrenotazioneException("Il giorno inserito non è un numero valido.");
        }catch (Exception e){
            System.out.println("erooresss");
            throw new PrenotazioneException("Erroresss");
        }
    }
    private void inviaEmailConferma(String email){
        System.out.println("E-mail di conferma inviata a: " + email);
    }
    private void notificaProprietaria(Appuntamento appuntamento){
        System.out.println("Notifica appuntamento del: " + appuntamento.getData() + ", inviata alla proprietaria");
    }
}
