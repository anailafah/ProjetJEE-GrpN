package com.assurance.model;

import java.io.Serializable;

/**
 * Un message de la conversation avec l'assistant IA.
 * L'historique est conservé en session (non persisté en base).
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Auteur { CLIENT, ASSISTANT }

    private final Auteur auteur;
    private final String texte;

    public Message(Auteur auteur, String texte) {
        this.auteur = auteur;
        this.texte = texte;
    }

    public Auteur getAuteur() {
        return auteur;
    }

    public String getTexte() {
        return texte;
    }

    /** Pratique dans les JSP : ${message.client}. */
    public boolean isClient() {
        return auteur == Auteur.CLIENT;
    }
}
