 <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Ajouter un contrat</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f1f4f9;
            margin: 0;
            padding: 40px 20px;
            color: #263238;
        }

        .container {
            max-width: 750px;
            margin: auto;
        }

        h1 {
            color: #17365d;
            margin-bottom: 25px;
        }

        .form-card {
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 8px;
        }

        input, select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccd5df;
            border-radius: 6px;
            font-size: 14px;
        }

        input:focus, select:focus {
            outline: none;
            border-color: #1769aa;
        }

        select[multiple] {
            min-height: 130px;
        }

        .help {
            font-size: 13px;
            color: #687786;
            margin-top: 7px;
        }

        .btn {
            display: inline-block;
            padding: 12px 20px;
            border: none;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
            cursor: pointer;
            font-size: 14px;
        }

        .btn-primary {
            background: #1769aa;
            color: white;
        }

        .btn-primary:hover {
            background: #104d80;
        }

        .btn-secondary {
            background: #e4eaf1;
            color: #263238;
        }

        .btn-secondary:hover {
            background: #d2dce8;
        }

        .actions {
            display: flex;
            gap: 12px;
            margin-top: 25px;
        }

        .message-error {
            background: #fdeaea;
            color: #b42318;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 20px;
        }

        @media(max-width: 600px) {
            body {
                padding: 20px 12px;
            }

            .form-card {
                padding: 20px;
            }

            .actions {
                flex-direction: column;
            }
        }
    </style>
</head>

<body>

<div class="container">

    <h1>Créer un nouveau contrat</h1>

    <c:if test="${not empty sessionScope.messageErreur}">
        <div class="message-error">
            ${sessionScope.messageErreur}
        </div>
        <c:remove var="messageErreur" scope="session"/>
    </c:if>

    <div class="form-card">

        <form action="${pageContext.request.contextPath}/agent/ajouter-contrat"
              method="post">

            <div class="form-group">
                <label>Numéro du contrat :</label>
                <input type="text" name="numeroContrat" required>
            </div>

            <div class="form-group">
                <label>Client :</label>
                <select name="clientId" required>
                    <option value="">Choisir un client</option>
                    <c:forEach var="client" items="${clients}">
                        <option value="${client.id}">
                            ${client.nom} ${client.prenom}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Type d'assurance :</label>
                <select name="typeAssuranceId" required>
                    <option value="">Choisir un type</option>
                    <c:forEach var="type" items="${types}">
                        <c:if test="${type.actif}">
                            <option value="${type.id}">
                                ${type.nom}
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Date de début :</label>
                <input type="date" name="dateDebut" required>
            </div>

            <div class="form-group">
                <label>Date de fin :</label>
                <input type="date" name="dateFin" required>
            </div>

            <div class="form-group">
                <label>Montant :</label>
                <input type="number" name="montant"
                       step="0.01" min="0.01" required>
            </div>

            <div class="form-group">
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
                <p class="help">
                    Maintiens Ctrl pour sélectionner plusieurs garanties.
                </p>
            </div>

            <div class="actions">
                <button type="submit" class="btn btn-primary">
                    Créer le contrat
                </button>

                <a href="${pageContext.request.contextPath}/agent/contrats"
                   class="btn btn-secondary">
                    Annuler
                </a>
            </div>

        </form>

    </div>

</div>

</body>
</html>