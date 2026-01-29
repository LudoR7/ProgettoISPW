package com.artigianhair.controller;

import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.engineering.factory.DAOfactory;
import com.artigianhair.model.Appuntamento;
import com.artigianhair.persistence.dao.AppuntamentoDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.artigianhair.view.cli.PrenotazioneCLI.MESI_VALIDI;

public class AgendaController {
    public static List<AppuntamentoBean> recuperaAppuntamenti() throws IOException {
        List<AppuntamentoBean> beans = new ArrayList<>();
        AppuntamentoDAO dao = DAOfactory.getAppuntamentoDAO();
        List<Appuntamento> agenda = dao.findAll();

        for(Appuntamento a : agenda){
            AppuntamentoBean bean = new AppuntamentoBean();
            bean.setData(a.getData().toString());
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
}

