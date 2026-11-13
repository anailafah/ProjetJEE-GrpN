package com.assurance.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double montant;
    private LocalDate datePaiement;
    private String modePaiement;
    private String statut;

    @ManyToOne
    @JoinColumn(name = "contrat_id", nullable = false)
    private Contrat contrat;

    public Paiement() {
    }

    public Long getId() { 
    	return id; 
    }
    
    public void setId(Long id) { 
    	this.id = id;
    }

    public double getMontant() { 
    	return montant;
    }
    
    public void setMontant(double montant) { 
    	this.montant = montant;
    }

    public LocalDate getDatePaiement() { 
    	return datePaiement; 
    }
    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getModePaiement() {
    	return modePaiement; 
    }
    
    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getStatut(){ 
    	return statut; 
    }
    public void setStatut(String statut) { 
    	this.statut = statut; 
    }

    public Contrat getContrat() { 
    	return contrat; 
    }
    public void setContrat(Contrat contrat) { 
    	this.contrat = contrat; 
    }
}