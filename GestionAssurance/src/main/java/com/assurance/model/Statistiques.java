package com.assurance.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Indicateurs affichés dans les tableaux de bord (admin et agent).
 * Rempli par StatistiquesDAO.
 */
public class Statistiques {

    // Indicateurs généraux
    private int nombreUtilisateurs;
    private int nombrePaiements;
    private int echeancesImpayees;

    // Statistiques clients
    private int nombreClients;
    private int nouveauxClients;          // inscrits durant les 30 derniers jours

    // Statistiques contrats
    private int nombreContrats;
    private int contratsActifs;
    private int contratsExpires;
    private int contratsResilies;

    // Statistiques sinistres
    private int nombreSinistres;
    private int sinistresEnAttente;

    // Statistiques financières
    private double montantTotalPaiements; // tous paiements confondus
    private double chiffreAffaires;       // paiements effectués
    private double montantPrimes;         // primes des contrats actifs
    private double montantIndemnisations; // sinistres indemnisés

    // Données des graphiques
    private Map<String, Integer> contratsParType = new LinkedHashMap<>();
    private Map<String, Integer> clientsParType = new LinkedHashMap<>();
    private Map<String, Integer> contratsParMois = new LinkedHashMap<>();
    private Map<String, Integer> sinistresParStatut = new LinkedHashMap<>();
    private Map<String, Double> revenusParMois = new LinkedHashMap<>();

    public Statistiques() {
    }

    public int getNombreUtilisateurs() {
        return nombreUtilisateurs;
    }

    public void setNombreUtilisateurs(int nombreUtilisateurs) {
        this.nombreUtilisateurs = nombreUtilisateurs;
    }

    public int getNombrePaiements() {
        return nombrePaiements;
    }

    public void setNombrePaiements(int nombrePaiements) {
        this.nombrePaiements = nombrePaiements;
    }

    public int getEcheancesImpayees() {
        return echeancesImpayees;
    }

    public void setEcheancesImpayees(int echeancesImpayees) {
        this.echeancesImpayees = echeancesImpayees;
    }

    public int getNombreClients() {
        return nombreClients;
    }

    public void setNombreClients(int nombreClients) {
        this.nombreClients = nombreClients;
    }

    public int getNouveauxClients() {
        return nouveauxClients;
    }

    public void setNouveauxClients(int nouveauxClients) {
        this.nouveauxClients = nouveauxClients;
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

    public int getContratsExpires() {
        return contratsExpires;
    }

    public void setContratsExpires(int contratsExpires) {
        this.contratsExpires = contratsExpires;
    }

    public int getContratsResilies() {
        return contratsResilies;
    }

    public void setContratsResilies(int contratsResilies) {
        this.contratsResilies = contratsResilies;
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

    public double getMontantTotalPaiements() {
        return montantTotalPaiements;
    }

    public void setMontantTotalPaiements(double montantTotalPaiements) {
        this.montantTotalPaiements = montantTotalPaiements;
    }

    public double getChiffreAffaires() {
        return chiffreAffaires;
    }

    public void setChiffreAffaires(double chiffreAffaires) {
        this.chiffreAffaires = chiffreAffaires;
    }

    public double getMontantPrimes() {
        return montantPrimes;
    }

    public void setMontantPrimes(double montantPrimes) {
        this.montantPrimes = montantPrimes;
    }

    public double getMontantIndemnisations() {
        return montantIndemnisations;
    }

    public void setMontantIndemnisations(double montantIndemnisations) {
        this.montantIndemnisations = montantIndemnisations;
    }

    public Map<String, Integer> getContratsParType() {
        return contratsParType;
    }

    public void setContratsParType(Map<String, Integer> contratsParType) {
        this.contratsParType = contratsParType;
    }

    public Map<String, Integer> getClientsParType() {
        return clientsParType;
    }

    public void setClientsParType(Map<String, Integer> clientsParType) {
        this.clientsParType = clientsParType;
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

    public Map<String, Double> getRevenusParMois() {
        return revenusParMois;
    }

    public void setRevenusParMois(Map<String, Double> revenusParMois) {
        this.revenusParMois = revenusParMois;
    }
}
