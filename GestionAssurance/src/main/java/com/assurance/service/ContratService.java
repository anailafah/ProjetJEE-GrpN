package com.assurance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import com.assurance.util.HibernateUtil;

import com.assurance.dao.ContratDAO;
import com.assurance.dao.TypeAssuranceDAO;
import com.assurance.dao.GarantieDAO;
import com.assurance.model.Contrat;
import com.assurance.model.Client;
import com.assurance.model.TypeAssurance;
import com.assurance.model.Garantie;

public class ContratService {

    private ContratDAO contratDAO = new ContratDAO();
    private TypeAssuranceDAO typeAssuranceDAO = new TypeAssuranceDAO();
    private GarantieDAO garantieDAO = new GarantieDAO();

    public void ajouter(Contrat contrat) {

        verifierContrat(contrat);

        contrat.setStatut("ACTIF");

        contratDAO.ajouter(contrat);
    }

    public List<Contrat> lister() {
        return contratDAO.lister();
    }

    public Contrat trouverParId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }

        return contratDAO.trouverParId(id);
    }

    public void modifier(Contrat contrat) {

        if (contrat == null || contrat.getId() == null) {
            throw new IllegalArgumentException("Contrat invalide.");
        }

        Contrat existant = contratDAO.trouverParId(contrat.getId());

        if (existant == null) {
            throw new IllegalArgumentException("Contrat introuvable.");
        }

        verifierContrat(contrat);

        contratDAO.modifier(contrat);
    }

    private void verifierContrat(Contrat contrat) {

        if (contrat == null) {
            throw new IllegalArgumentException("Contrat obligatoire.");
        }

        if (contrat.getNumeroContrat() == null ||
            contrat.getNumeroContrat().trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro du contrat est obligatoire.");
        }

        if (contrat.getClient() == null || contrat.getClient().getId() == null) {
            throw new IllegalArgumentException("Un client doit être sélectionné.");
        }

        if (contrat.getTypeAssurance() == null ||
            contrat.getTypeAssurance().getId() == null) {
            throw new IllegalArgumentException("Un type d'assurance doit être sélectionné.");
        }

        if (contrat.getDateDebut() == null || contrat.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates sont obligatoires.");
        }

        if (!contrat.getDateFin().isAfter(contrat.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }

        if (contrat.getDateDebut().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date de début ne peut pas être dans le passé.");
        }

        BigDecimal montant = contrat.getMontant();

        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro.");
        }

        TypeAssurance type = typeAssuranceDAO.trouverParId(
            contrat.getTypeAssurance().getId()
        );

        if (type == null || !type.isActif()) {
            throw new IllegalArgumentException("Type d'assurance inexistant ou inactif.");
        }
        verifierClient(contrat.getClient().getId());
        verifierGaranties(contrat.getGaranties());

    }
 
    private void verifierClient (Long clientId) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Client client = session.get(Client.class, clientId);

            if (client == null) {
                throw new IllegalArgumentException("Client introuvable.");
            }

            if (!client.isActif()) {
                throw new IllegalArgumentException("Ce client est désactivé.");
            }
        }
    }
    private void verifierGaranties(List<Garantie> garanties) {

        if (garanties == null || garanties.isEmpty()) {
            throw new IllegalArgumentException("Au moins une garantie doit être sélectionnée.");
        }

        for (Garantie garantie : garanties) {

            if (garantie == null || garantie.getId() == null) {
                throw new IllegalArgumentException("Garantie invalide.");
            }

            Garantie existante = garantieDAO.trouverParId(garantie.getId());

            if (existante == null || !existante.isActif()) {
                throw new IllegalArgumentException(
                    "Une garantie sélectionnée est inexistante ou désactivée."
                );
            }
        }
    }
}