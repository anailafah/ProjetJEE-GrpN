<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ajouter un contrat</title>
</head>
<body>

    <h1>Créer un nouveau contrat</h1>

    <form action="ajouter-contrat" method="post">

        <label>Numéro du contrat :</label>
        <input type="text" name="numeroContrat" required>
        <br><br>

        <label>Client :</label>
        <select name="clientId" required>
            <option value="">Choisir un client</option>
            <c:forEach var="client" items="${clients}">
                <option value="${client.id}">
                    ${client.nom} ${client.prenom}
                </option>
            </c:forEach>
        </select>
        <br><br>

        <label>Type d'assurance :</label>
        <select name="typeAssuranceId" required>
            <option value="">Choisir un type</option>
            <c:forEach var="type" items="${types}">
                <c:if test="${type.actif}">
                    <option value="${type.id}">${type.nom}</option>
                </c:if>
            </c:forEach>
        </select>
        <br><br>

        <label>Date de début :</label>
        <input type="date" name="dateDebut" required>
        <br><br>

        <label>Date de fin :</label>
        <input type="date" name="dateFin" required>
        <br><br>

        <label>Montant :</label>
        <input type="number" name="montant"
               step="0.01" min="0.01" required>
        <br><br>

        <label>Garanties :</label>
        <select name="garantieIds" multiple required>
            <c:forEach var="garantie" items="${garanties}">
                <c:if test="${garantie.actif}">
                    <option value="${garantie.id}">
                        ${garantie.nom}
                    </option>
                </c:if>
            </c:forEach>
        </select>
        <p>Maintiens Ctrl pour sélectionner plusieurs garanties.</p>

        <button type="submit">Créer le contrat</button>

    </form>

</body>
</html>