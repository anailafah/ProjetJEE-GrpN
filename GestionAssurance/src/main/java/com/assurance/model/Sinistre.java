package com.assurance.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sinistres")
public class Sinistre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDate dateDeclaration;
    private String statut;
    private double montantEstime;

    @ManyToOne
    @JoinColumn(name = "contrat_id", nullable = false)
    private Contrat contrat;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public Sinistre() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDeclaration() { return dateDeclaration; }
    public void setDateDeclaration(LocalDate dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public double getMontantEstime() { return montantEstime; }
    public void setMontantEstime(double montantEstime) {
        this.montantEstime = montantEstime;
    }

    public Contrat getContrat() { return contrat; }
    public void setContrat(Contrat contrat) { this.contrat = contrat; }

    public Agent getAgent() { return agent; }
    public void setAgent(Agent agent) { this.agent = agent; }
}