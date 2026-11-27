package com.assurance.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * Pièce justificative jointe à un sinistre (photo, facture, constat...).
 * Le fichier est stocké sur le disque, la base ne garde que son nom et son chemin.
 */
@Entity
@Table(name = "documents_sinistre")
public class DocumentSinistre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sinistre_id", nullable = false)
    private Sinistre sinistre;

    @Column(name = "nom_fichier", nullable = false, length = 255)
    private String nomFichier;

    @Column(name = "chemin_fichier", nullable = false, length = 500)
    private String cheminFichier;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    public DocumentSinistre() {
    }

    @PrePersist
    protected void avantEnregistrement() {
        if (dateAjout == null) {
            dateAjout = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sinistre getSinistre() {
        return sinistre;
    }

    public void setSinistre(Sinistre sinistre) {
        this.sinistre = sinistre;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public LocalDateTime getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
    }
}
