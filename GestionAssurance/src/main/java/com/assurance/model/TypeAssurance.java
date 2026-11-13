package com.assurance.model;

import jakarta.persistence.*;

@Entity
@Table(name = "types_assurance")
public class TypeAssurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;
    private double tarifBase;

    public TypeAssurance() {
    }

    public TypeAssurance(String nom, String description, double tarifBase) {
        this.nom = nom;
        this.description = description;
        this.tarifBase = tarifBase;
    }

    public Long getId() { return id; }
    
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    
    public void setDescription(String description) { this.description = description; }

    public double getTarifBase() { return tarifBase; }
    
    public void setTarifBase(double tarifBase) { this.tarifBase = tarifBase; }
}