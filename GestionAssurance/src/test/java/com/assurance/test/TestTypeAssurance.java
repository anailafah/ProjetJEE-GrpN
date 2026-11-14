package com.assurance.test;

import java.math.BigDecimal;

import com.assurance.dao.GarantieDAO;
import com.assurance.model.Garantie;

public class TestTypeAssurance {

	public static void main(String[] args) {

	    GarantieDAO dao = new GarantieDAO();

	    try {
	        // Supprimer la garantie ayant l'ID 1
	        dao.supprimer(1L);

	        System.out.println("Suppression exécutée !");

	        // Vérifier que la garantie n'existe plus
	        Garantie garantie = dao.trouverParId(1L);

	        if (garantie == null) {
	            System.out.println("Vérification réussie : garantie supprimée.");
	        } else {
	            System.out.println("La garantie existe toujours !");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}