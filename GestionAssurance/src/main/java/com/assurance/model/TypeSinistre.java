package com.assurance.model;

public enum TypeSinistre {

    ACCIDENT("Accident"),
    VOL("Vol"),
    INCENDIE("Incendie"),
    DEGATS_DES_EAUX("Dégâts des eaux"),
    MALADIE("Maladie / frais médicaux"),
    INCIDENT_VOYAGE("Incident de voyage"),
    AUTRE("Autre");

    private final String libelle;

    TypeSinistre(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
