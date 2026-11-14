<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Modifier un type d'assurance</title>
</head>

<body>

    <h1>Modifier un type d'assurance</h1>

    <form action="modifier-type-assurance" method="post">

        <input type="hidden" name="id" value="${type.id}">

        <p>Nom :</p>
        <input type="text" name="nom" value="${type.nom}" required>

        <p>Description :</p>
        <textarea name="description" rows="4">${type.description}</textarea>

        <p>Durée en mois :</p>
        <input type="number" name="dureeMois" value="${type.dureeMois}" min="1" required>

        <p>Tarif de base :</p>
        <input type="number" name="tarifBase" value="${type.tarifBase}" min="0.01" step="0.01" required>

        <p>
            <input type="submit" value="Enregistrer les modifications">
        </p>

    </form>

</body>
</html>ml>