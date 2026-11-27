package com.assurance.service;

import java.math.BigDecimal;
import java.util.List;

import com.assurance.dao.TypeAssuranceDAO;
import com.assurance.model.TypeAssurance;

public class TypeAssuranceService {

    private TypeAssuranceDAO typeAssuranceDAO = new TypeAssuranceDAO();

    public void ajouter(TypeAssurance type) {

        verifierTypeAssurance(type);

        type.setActif(true);

        typeAssuranceDAO.ajouter(type);
    }

    public List<TypeAssurance> lister() {
        return typeAssuranceDAO.lister();
    }

    public TypeAssurance trouverParId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }

        return typeAssuranceDAO.trouverParId(id);
    }

    public void modifier(TypeAssurance type) {

        if (type == null || type.getId() == null) {
            throw new IllegalArgumentException("Type d'assurance invalide.");
        }

        TypeAssurance existant = typeAssuranceDAO.trouverParId(type.getId());

        if (existant == null) {
            throw new IllegalArgumentException("Type d'assurance introuvable.");
        }

        verifierTypeAssurance(type);

        typeAssuranceDAO.modifier(type);
    }

    public void desactiver(Long id) {

        TypeAssurance type = trouverParId(id);

        if (type == null) {
            throw new IllegalArgumentException("Type d'assurance introuvable.");
        }

        type.setActif(false);

        typeAssuranceDAO.modifier(type);
    }

    private void verifierTypeAssurance(TypeAssurance type) {

        if (type == null) {
            throw new IllegalArgumentException("Type d'assurance obligatoire.");
        }

        if (type.getNom() == null || type.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }

        if (type.getDureeMois() <= 0) {
            throw new IllegalArgumentException("La durée doit être supérieure à zéro.");
        }

        BigDecimal tarif = type.getTarifBase();

        if (tarif == null || tarif.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le tarif doit être supérieur à zéro.");
        }
    }
}