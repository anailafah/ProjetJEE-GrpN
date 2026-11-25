package com.assurance.model;

import java.util.Map;

public class Statistiques {

    private int nombreUtilisateurs;
    private int nombreClients;
    private int nombreContrats;
    private int contratsActifs;
    private int nombreSinistres;
    private int sinistresEnAttente;
    private int nombrePaiements;
    private double montantTotalPaiements;
    private int echeancesImpayees;

    // Données des graphiques
    private Map<String, Integer> contratsParType;
    private Map<String, Integer> contratsParMois;
    private Map<String, Integer> sinistresParStatut;

    public Statistiques() {
    }

    public int getNombreUtilisateurs() {
        return nombreUtilisateurs;
    }

    public void setNombreUtilisateurs(int nombreUtilisateurs) {
        this.nombreUtilisateurs = nombreUtilisateurs;
    }

    public int getNombreClients() {
        return nombreClients;
    }

    public void setNombreClients(int nombreClients) {
        this.nombreClients = nombreClients;
    }

    public int getNombreContrats() {
        return nombreContrats;
    }

    public void setNombreContrats(int nombreContrats) {
        this.nombreContrats = nombreContrats;
    }

    public int getContratsActifs() {
        return contratsActifs;
    }

    public void setContratsActifs(int contratsActifs) {
        this.contratsActifs = contratsActifs;
    }

    public int getNombreSinistres() {
        return nombreSinistres;
    }

    public void setNombreSinistres(int nombreSinistres) {
        this.nombreSinistres = nombreSinistres;
    }

    public int getSinistresEnAttente() {
        return sinistresEnAttente;
    }

    public void setSinistresEnAttente(int sinistresEnAttente) {
        this.sinistresEnAttente = sinistresEnAttente;
    }

    public int getNombrePaiements() {
        return nombrePaiements;
    }

    public void setNombrePaiements(int nombrePaiements) {
        this.nombrePaiements = nombrePaiements;
    }

    public double getMontantTotalPaiements() {
        return montantTotalPaiements;
    }

    public void setMontantTotalPaiements(double montantTotalPaiements) {
        this.montantTotalPaiements = montantTotalPaiements;
    }

    public int getEcheancesImpayees() {
        return echeancesImpayees;
    }

    public void setEcheancesImpayees(int echeancesImpayees) {
        this.echeancesImpayees = echeancesImpayees;
    }

    public Map<String, Integer> getContratsParType() {
        return contratsParType;
    }

    public void setContratsParType(Map<String, Integer> contratsParType) {
        this.contratsParType = contratsParType;
    }

    public Map<String, Integer> getContratsParMois() {
        return contratsParMois;
    }

    public void setContratsParMois(Map<String, Integer> contratsParMois) {
        this.contratsParMois = contratsParMois;
    }

    public Map<String, Integer> getSinistresParStatut() {
        return sinistresParStatut;
    }

    public void setSinistresParStatut(Map<String, Integer> sinistresParStatut) {
        this.sinistresParStatut = sinistresParStatut;
    }
}