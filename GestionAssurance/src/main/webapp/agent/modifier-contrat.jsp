<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifier un contrat</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            padding: 35px;
            background: #f4f6fa;
            font-family: Arial, sans-serif;
            color: #263445;
        }

        .container {
            max-width: 750px;
            margin: 20px auto;
            padding: 30px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
        }

        h1 {
            margin-bottom: 25px;
            color: #183b65;
            text-align: center;
        }

        label {
            display: block;
            margin: 16px 0 7px;
            font-weight: bold;
        }

        input, select {
            width: 100%;
            padding: 11px;
            border: 1px solid #ccd3dc;
            border-radius: 6px;
            font-size: 14px;
        }

        select[multiple] {
            min-height: 130px;
        }

        .info {
            margin-top: 7px;
            color: #6b7280;
            font-size: 13px;
        }

        .actions {
            display: flex;
            gap: 12px;
            margin-top: 25px;
        }

        button, .btn-retour {
            padding: 12px 20px;
            border: none;
            border-radius: 6px;
            text-decoration: none;
            font-size: 14px;
            cursor: pointer;
        }

        button {
            background: #1769aa;
            color: white;
        }

        button:hover {
            background: #105487;
        }

        .btn-retour {
            background: #e5e7eb;
            color: #263445;
        }
    </style>
</head>

<body>

<div class="container">

    <h1>Modifier le contrat</h1>

    <form action="${pageContext.request.contextPath}/agent/modifier-contrat"
          method="post">

        <input type="hidden" name="id" value="${contrat.id}">

        <label>Numéro du contrat :</label>
        <input type="text" name="numeroContrat"
               value="${contrat.numeroContrat}" required>

        <label>Client :</label>
        <select name="clientId" required>
            <option value="">Choisir un client</option>
            <c:forEach var="client" items="${clients}">
                <option value="${client.id}"
                    <c:if test="${client.id == contrat.client.id}">
                        selected
                    </c:if>>
                    ${client.nom} ${client.prenom}
                </option>
            </c:forEach>
        </select>

        <label>Type d'assurance :</label>
        <select name="typeAssuranceId" required>
            <option value="">Choisir un type</option>
            <c:forEach var="type" items="${types}">
                <c:if test="${type.actif}">
                    <option value="${type.id}"
                        <c:if test="${type.id == contrat.typeAssurance.id}">
                            selected
                        </c:if>>
                        ${type.nom}
                    </option>
                </c:if>
            </c:forEach>
        </select>

        <label>Date de début :</label>
        <input type="date" name="dateDebut"
               value="${contrat.dateDebut}" required>

        <label>Date de fin :</label>
        <input type="date" name="dateFin"
               value="${contrat.dateFin}" required>

        <label>Montant :</label>
        <input type="number" name="montant"
               value="${contrat.montant}"
               step="0.01" min="0.01" required>
<label>Garanties :</label>

<div style="margin: 10px 0 20px 0;">

    "<c:forEach var="garantie" items="${garanties}">
        <c:if test="${garantie.actif}">
            <label style="display:block; margin-bottom:10px;">

                <input type="checkbox"
                       name="garantieIds"
                       value="${garantie.id}"
                       style="width:auto;"
                       ${contrat.garanties.contains(garantie) ? 'checked' : ''}>

                <c:out value="${garantie.nom}"/>

            </label>
        </c:if>
    </c:forEach>

</div>

        <div class="actions">
            <button type="submit">Enregistrer les modifications</button>

            <a class="btn-retour"
               href="${pageContext.request.contextPath}/agent/contrats">
                Annuler
            </a>
        </div>

    </form>

</div>

</body>
</html>