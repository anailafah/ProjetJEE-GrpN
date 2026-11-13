package com.assurance.model;

import jakarta.persistence.*;

@Entity
@Table(name = "garanties")
public class Garantie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;
    private double montantCouverture;

    public Garantie() {
    }

    public Garantie(String nom, String description, double montantCouverture) {
        this.nom = nom;
        this.description = description;
        this.montantCouverture = montantCouverture;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getMontantCouverture() { return montantCouverture; }
    public void setMontantCouverture(double montantCouverture) {
        this.montantCouverture = montantCouverture;
    }
}