package com.assurance.service;

/**
 * Erreur métier avec un message destiné à l'utilisateur final.
 * Les servlets attrapent cette exception et affichent getMessage() dans le formulaire.
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }
}
