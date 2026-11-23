package com.assurance.service;

import com.assurance.dao.StatistiquesDAO;
import com.assurance.model.Statistiques;

public class StatistiquesService {

    private StatistiquesDAO statistiquesDAO = new StatistiquesDAO();

    public Statistiques obtenirStatistiques() {
        return statistiquesDAO.recupererStatistiques();
    }
}