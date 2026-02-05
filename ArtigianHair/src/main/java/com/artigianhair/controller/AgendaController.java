package com.artigianhair.controller;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.model.Appuntamento;
import com.artigianhair.persistence.dao.AppuntamentoDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.List;

import static com.artigianhair.view.cli.PrenotazioneCLI.MESI_VALIDI;

public class AgendaController {

    private AgendaController() {
        // Costruttore di default
    }
    public static List<AppuntamentoBean> recuperaAppuntamenti() throws IOException {
        List<AppuntamentoBean> beans = new ArrayList<>();
        AppuntamentoDAO dao = DAOfactory.getAppuntamentoDAO();
        List<Appuntamento> agenda = dao.findAll();

        for(Appuntamento a : agenda){
            AppuntamentoBean bean = new AppuntamentoBean();
            bean.setData(String.valueOf(a.getData().getDayOfMonth()));

            int indiceMese = a.getData().getMonthValue() - 1;
            bean.setMese(MESI_VALIDI.get(indiceMese));

            bean.setOrario(a.getFasciaOraria());
            bean.setClienteEmail(a.getClienteEmail());

            for (String t : a.getTrattamenti()) {
                bean.addTrattamento(t);
            }

            beans.add(bean);
        }
        return beans;
    }

    public static void cancellaAppuntamento(AppuntamentoBean bean) throws IOException {
        AppuntamentoDAO dao = DAOfactory.getAppuntamentoDAO();

        int giorno = Integer.parseInt(bean.getData().trim());
        int meseIndice = -1;
        for (int i = 0; i < MESI_VALIDI.size(); i++) {
            if (MESI_VALIDI.get(i).equalsIgnoreCase(bean.getMese())) {
                meseIndice = i + 1;
                break;
            }
        }

        LocalDate data = LocalDate.of(LocalDate.now().getYear(), meseIndice, giorno);
        LocalTime orario = bean.getOrario().equalsIgnoreCase("M") ? LocalTime.of(9, 0) : LocalTime.of(13, 0);

        Appuntamento appuntamento = new Appuntamento(data, orario, bean.getTrattamenti(), bean.getClienteEmail());
        dao.delete(appuntamento);
    }
}

