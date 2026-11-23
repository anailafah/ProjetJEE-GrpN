<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ajouter une garantie</title>
    <link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>

    <h1>Ajouter une garantie</h1>

    <form action="garanties" method="post">

        <label>Nom :</label>
        <input type="text" name="nom" required>
        <br><br>

        <label>Description :</label>
        <textarea name="description"></textarea>
        <br><br>

        <label>Montant maximal :</label>
        <input type="number" name="montantCouverture"
               step="0.01" min="0.01" required>
        <br><br>

        <button type="submit">Ajouter</button>

    </form>

</body>
</html>