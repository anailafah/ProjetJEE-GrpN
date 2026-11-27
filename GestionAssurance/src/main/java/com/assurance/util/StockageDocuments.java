package com.assurance.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

/**
 * Enregistrement des pièces justificatives des sinistres sur le disque.
 *
 * Les fichiers sont rangés hors du dossier de l'application (sinon ils seraient
 * effacés à chaque redéploiement), dans :
 *   - le dossier indiqué par la propriété Java -Dgestionassurance.uploads=...
 *   - sinon <dossier utilisateur>/GestionAssurance-uploads
 *
 * La base ne garde que le nom d'origine et un chemin relatif (ex. sinistre-12/xxxx.pdf).
 */
public final class StockageDocuments {

    public static final long TAILLE_MAX = 5L * 1024 * 1024;   // 5 Mo par fichier
    public static final int NOMBRE_MAX = 5;

    private static final Set<String> EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png");

    private StockageDocuments() {
    }

    public static Path dossierRacine() {
        String configure = System.getProperty("gestionassurance.uploads");

        Path racine = (configure != null && !configure.isBlank())
                ? Paths.get(configure)
                : Paths.get(System.getProperty("user.home"), "GestionAssurance-uploads");

        return racine.toAbsolutePath().normalize();
    }

    /**
     * Récupère les fichiers envoyés dans le champ "nomChamp" (les champs vides sont ignorés)
     * et vérifie leur nombre, leur taille et leur extension AVANT tout enregistrement.
     */
    public static List<Part> lireFichiers(HttpServletRequest request, String nomChamp)
            throws IOException, ServletException {

        List<Part> fichiers = new ArrayList<>();

        for (Part part : request.getParts()) {
            if (nomChamp.equals(part.getName())
                    && part.getSize() > 0
                    && part.getSubmittedFileName() != null
                    && !part.getSubmittedFileName().isBlank()) {
                fichiers.add(part);
            }
        }

        if (fichiers.size() > NOMBRE_MAX) {
            throw new IllegalArgumentException(
                "Vous pouvez joindre au maximum " + NOMBRE_MAX + " fichiers.");
        }

        for (Part fichier : fichiers) {
            String nom = nomOriginal(fichier);

            if (!EXTENSIONS.contains(extension(nom))) {
                throw new IllegalArgumentException(
                    "Le fichier « " + nom + " » n'est pas accepté (PDF, JPG ou PNG uniquement).");
            }

            if (fichier.getSize() > TAILLE_MAX) {
                throw new IllegalArgumentException(
                    "Le fichier « " + nom + " » dépasse 5 Mo.");
            }
        }

        return fichiers;
    }

    /**
     * Copie le fichier sur le disque et renvoie son chemin relatif à enregistrer en base.
     */
    public static String enregistrer(Long sinistreId, Part fichier) throws IOException {

        String dossierRelatif = "sinistre-" + sinistreId;
        Path dossier = dossierRacine().resolve(dossierRelatif);
        Files.createDirectories(dossier);

        // Nom aléatoire sur le disque : évite les collisions et les noms dangereux
        String nomDisque = UUID.randomUUID() + "." + extension(nomOriginal(fichier));

        try (InputStream contenu = fichier.getInputStream()) {
            Files.copy(contenu, dossier.resolve(nomDisque), StandardCopyOption.REPLACE_EXISTING);
        }

        return dossierRelatif + "/" + nomDisque;
    }

    /**
     * Retrouve le fichier à partir du chemin stocké en base,
     * en refusant tout chemin qui sortirait du dossier racine.
     */
    public static Path resoudre(String cheminRelatif) {

        Path racine = dossierRacine();
        Path fichier = racine.resolve(cheminRelatif).normalize();

        if (!fichier.startsWith(racine)) {
            throw new IllegalArgumentException("Chemin de fichier invalide.");
        }

        return fichier;
    }

    /** Nom du fichier tel qu'envoyé par le navigateur, sans le chemin du poste client. */
    public static String nomOriginal(Part fichier) {
        String nom = fichier.getSubmittedFileName();
        nom = nom.substring(Math.max(nom.lastIndexOf('/'), nom.lastIndexOf('\\')) + 1);
        return nom.length() > 200 ? nom.substring(nom.length() - 200) : nom;
    }

    public static String typeContenu(String nomFichier) {
        switch (extension(nomFichier)) {
            case "pdf":
                return "application/pdf";
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            default:
                return "application/octet-stream";
        }
    }

    private static String extension(String nom) {
        int point = nom.lastIndexOf('.');
        return point < 0 ? "" : nom.substring(point + 1).toLowerCase(Locale.ROOT);
    }
}
