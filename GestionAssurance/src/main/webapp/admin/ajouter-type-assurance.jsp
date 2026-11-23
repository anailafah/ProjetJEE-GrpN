<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Ajouter un type d'assurance</title>
    <link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body>

    <h1>Ajouter un type d'assurance</h1>

    <form action="types-assurance" method="post">

        <p>Nom :</p>
        <input type="text" name="nom">

        <p>Description :</p>
        <textarea name="description"></textarea>

        <p>Durée en mois :</p>
        <input type="number" name="dureeMois">

        <p>Tarif de base :</p>
        <input type="number" name="tarifBase" step="0.01">

        <p>
            <input type="submit" value="Ajouter">
        </p>

    </form>

</body>
</html>