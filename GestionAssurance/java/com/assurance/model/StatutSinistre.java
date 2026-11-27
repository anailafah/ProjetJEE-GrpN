package com.assurance.model;

public enum StatutSinistre {

    DECLARE("Déclaré"),
    EN_ANALYSE("En cours d'analyse"),
    ACCEPTE("Accepté"),
    REFUSE("Refusé"),
    INDEMNISE("Indemnisé");

    private final String libelle;

    StatutSinistre(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}