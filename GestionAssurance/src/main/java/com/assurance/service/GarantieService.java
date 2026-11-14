package com.assurance.service;

import java.math.BigDecimal;
import java.util.List;

import com.assurance.dao.GarantieDAO;
import com.assurance.model.Garantie;

public class GarantieService {

    private GarantieDAO garantieDAO = new GarantieDAO();

    public void ajouter(Garantie garantie) {

        verifierGarantie(garantie);

        garantie.setActif(true);

        garantieDAO.ajouter(garantie);
    }

    public List<Garantie> lister() {
        return garantieDAO.lister();
    }

    public Garantie trouverParId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }

        return garantieDAO.trouverParId(id);
    }

    public void modifier(Garantie garantie) {

        if (garantie == null || garantie.getId() == null) {
            throw new IllegalArgumentException("Garantie invalide.");
        }

        Garantie existante = garantieDAO.trouverParId(garantie.getId());

        if (existante == null) {
            throw new IllegalArgumentException("Garantie introuvable.");
        }

        verifierGarantie(garantie);

        garantieDAO.modifier(garantie);
    }

    public void desactiver(Long id) {

        Garantie garantie = trouverParId(id);

        if (garantie == null) {
            throw new IllegalArgumentException("Garantie introuvable.");
        }

        garantie.setActif(false);

        garantieDAO.modifier(garantie);
    }

    private void verifierGarantie(Garantie garantie) {

        if (garantie == null) {
            throw new IllegalArgumentException("Garantie obligatoire.");
        }

        if (garantie.getNom() == null || garantie.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }

        BigDecimal montant = garantie.getMontantCouverture();

        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro.");
        }
    }
}