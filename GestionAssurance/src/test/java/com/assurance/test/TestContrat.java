package com.assurance.test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.hibernate.Session;

import com.assurance.dao.ContratDAO;
import com.assurance.model.Client;
import com.assurance.model.Contrat;
import com.assurance.model.Garantie;
import com.assurance.model.TypeAssurance;
import com.assurance.util.HibernateUtil;

public class TestContrat {

	public static void main(String[] args) {

	    ContratDAO dao = new ContratDAO();

	    try {
	        dao.supprimer(1L);

	        Contrat contrat = dao.trouverParId(1L);

	        if (contrat == null) {
	            System.out.println("Contrat supprimé avec succès !");
	        } else {
	            System.out.println("Le contrat existe encore.");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        HibernateUtil.shutdown();
	    }
	}
}