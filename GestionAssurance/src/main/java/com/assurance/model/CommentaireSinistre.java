package com.assurance.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * Commentaire ajouté sur un sinistre, en général par l'agent qui l'analyse.
 */
@Entity
@Table(name = "commentaires_sinistre")
public class CommentaireSinistre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sinistre_id", nullable = false)
    private Sinistre sinistre;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur auteur;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String commentaire;

    @Column(name = "date_commentaire")
    private LocalDateTime dateCommentaire;

    public CommentaireSinistre() {
    }

    @PrePersist
    protected void avantEnregistrement() {
        if (dateCommentaire == null) {
            dateCommentaire = LocalDateTime.now();
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

    public Utilisateur getAuteur() {
        return auteur;
    }

    public void setAuteur(Utilisateur auteur) {
        this.auteur = auteur;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDateTime getDateCommentaire() {
        return dateCommentaire;
    }

    public void setDateCommentaire(LocalDateTime dateCommentaire) {
        this.dateCommentaire = dateCommentaire;
    }
}
