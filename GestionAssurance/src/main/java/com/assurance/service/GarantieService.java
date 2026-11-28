package com.assurance.service;

import java.math.BigDecimal;
import java.util.List;

import com.assurance.dao.GarantieDAO;
import com.assurance.model.Garantie;

public class GarantieService {

    private GarantieDAO garantieDAO = new GarantieDAO();

    // Ajouter une garantie
    public void ajouter(Garantie garantie) {

        verifierGarantie(garantie);

        if (garantieDAO.nomExiste(garantie.getNom(), null)) {
            throw new IllegalArgumentException(
                "Une garantie portant ce nom existe déjà."
            );
        }

        garantie.setNom(garantie.getNom().trim());
        garantie.setActif(true);

        garantieDAO.ajouter(garantie);
    }

    // Lister toutes les garanties
    public List<Garantie> lister() {
        return garantieDAO.lister();
    }

    // Trouver une garantie par son identifiant
    public Garantie trouverParId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }

        return garantieDAO.trouverParId(id);
    }

    // Modifier une garantie
    public void modifier(Garantie garantie) {

        if (garantie == null || garantie.getId() == null) {
            throw new IllegalArgumentException("Garantie invalide.");
        }

        Garantie existante = trouverParId(garantie.getId());

        if (existante == null) {
            throw new IllegalArgumentException("Garantie introuvable.");
        }

        verifierGarantie(garantie);

        if (garantieDAO.nomExiste(garantie.getNom(), garantie.getId())) {
            throw new IllegalArgumentException(
                "Une autre garantie porte déjà ce nom."
            );
        }

        // Nettoyer le nom
        existante.setNom(garantie.getNom().trim());

        // Mettre à jour les informations
        existante.setDescription(garantie.getDescription());
        existante.setMontantCouverture(garantie.getMontantCouverture());

        // Conserver le statut actif/inactif existant
        garantieDAO.modifier(existante);
    }

    // Désactiver une garantie
    public void desactiver(Long id) {

        Garantie garantie = trouverParId(id);

        if (garantie == null) {
            throw new IllegalArgumentException("Garantie introuvable.");
        }

        if (!garantie.isActif()) {
            throw new IllegalArgumentException(
                "Cette garantie est déjà désactivée."
            );
        }

        garantie.setActif(false);

        garantieDAO.modifier(garantie);
    }

    // Réactiver une garantie
    public void activer(Long id) {

        Garantie garantie = trouverParId(id);

        if (garantie == null) {
            throw new IllegalArgumentException("Garantie introuvable.");
        }

        if (garantie.isActif()) {
            throw new IllegalArgumentException(
                "Cette garantie est déjà active."
            );
        }

        garantie.setActif(true);

        garantieDAO.modifier(garantie);
    }


    // Vérifier les informations d'une garantie
    private void verifierGarantie(Garantie garantie) {

        if (garantie == null) {
            throw new IllegalArgumentException("Garantie obligatoire.");
        }

        String nom = garantie.getNom();

        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }

        if (nom.trim().length() > 100) {
            throw new IllegalArgumentException(
                "Le nom ne doit pas dépasser 100 caractères."
            );
        }

        BigDecimal montant = garantie.getMontantCouverture();

        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Le montant doit être supérieur à zéro."
            );
        }

        if (montant.scale() > 2) {
            throw new IllegalArgumentException(
                "Le montant ne doit pas dépasser deux chiffres après la virgule."
            );
        }

        if (montant.precision() > 12) {
            throw new IllegalArgumentException(
                "Le montant dépasse la limite autorisée."
            );
        }
    }
}