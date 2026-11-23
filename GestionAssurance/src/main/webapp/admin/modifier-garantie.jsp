<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifier une garantie</title>
    <link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>

    <h1>Modifier une garantie</h1>

    <form action="modifier-garantie" method="post">

        <input type="hidden" name="id" value="${garantie.id}">

        <label>Nom :</label>
        <input type="text" name="nom"
               value="${garantie.nom}" required>
        <br><br>

        <label>Description :</label>
        <textarea name="description">${garantie.description}</textarea>
        <br><br>

        <label>Montant maximal :</label>
        <input type="number" name="montantCouverture"
               value="${garantie.montantCouverture}"
               step="0.01" min="0.01" required>
        <br><br>

        <button type="submit">Enregistrer les modifications</button>

    </form>

</body>
</html>l>