<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter un contrat</title>

    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f3f6fb;
            color: #263247;
            padding: 35px 18px;
        }

        .container {
            max-width: 750px;
            margin: auto;
        }

        .back-link {
            display: inline-block;
            margin-bottom: 25px;
            color: #52647c;
            text-decoration: none;
            font-weight: 600;
        }

        .back-link:hover {
            color: #087f8c;
        }

        .form-card {
            background: white;
            padding: 35px;
            border-radius: 14px;
            box-shadow: 0 5px 20px rgba(30, 50, 80, 0.06);
        }

        h1 {
            color: #172b4d;
            font-size: 27px;
            margin-bottom: 9px;
        }

        .subtitle {
            color: #78869a;
            margin-bottom: 28px;
            line-height: 1.5;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #34445c;
            font-weight: 600;
            font-size: 14px;
        }

        input, select {
            width: 100%;
            padding: 12px 13px;
            border: 1px solid #dce3ed;
            border-radius: 8px;
            font-size: 14px;
            color: #263247;
            background: white;
            outline: none;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        input:focus, select:focus {
            border-color: #087f8c;
            box-shadow: 0 0 0 3px rgba(8, 127, 140, 0.10);
        }

        select[multiple] {
            min-height: 150px;
            padding: 8px;
        }

        select[multiple] option {
            padding: 8px;
        }

        .help-text {
            margin-top: 8px;
            color: #78869a;
            font-size: 12px;
        }

        .form-actions {
            display: flex;
            gap: 12px;
            margin-top: 28px;
            flex-wrap: wrap;
        }

        .btn {
            display: inline-block;
            border: none;
            border-radius: 8px;
            padding: 12px 20px;
            font-size: 14px;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            transition: 0.2s;
        }

        .btn-primary {
            background: #087f8c;
            color: white;
        }

        .btn-primary:hover {
            background: #066875;
        }

        .btn-secondary {
            background: #edf1f6;
            color: #52647c;
        }

        .btn-secondary:hover {
            background: #dfe6ef;
        }

        @media (max-width: 600px) {
            .form-card {
                padding: 23px 18px;
            }

            h1 {
                font-size: 23px;
            }
        }
    </style>
</head>

<body>

<div class="container">

    <a class="back-link"
       href="${pageContext.request.contextPath}/admin/contrats">
        Retour à la liste des contrats
    </a>

    <div class="form-card">

        <h1>Créer un nouveau contrat</h1>
        <p class="subtitle">
            Renseignez les informations nécessaires pour enregistrer un contrat d'assurance.
        </p>

        <form action="${pageContext.request.contextPath}/admin/ajouter-contrat"
              method="post">

            <div class="form-group">
                <label for="numeroContrat">Numéro du contrat</label>
                <input type="text" id="numeroContrat" name="numeroContrat" required>
            </div>

            <div class="form-group">
                <label for="clientId">Client</label>
                <select id="clientId" name="clientId" required>
                    <option value="">Choisir un client</option>

                    <c:forEach var="client" items="${clients}">
                        <option value="${client.id}">
                            <c:out value="${client.nom}"/>
                            <c:out value="${client.prenom}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="typeAssuranceId">Type d'assurance</label>
                <select id="typeAssuranceId" name="typeAssuranceId" required>
                    <option value="">Choisir un type</option>

                    <c:forEach var="type" items="${types}">
                        <c:if test="${type.actif}">
                            <option value="${type.id}">
                                <c:out value="${type.nom}"/>
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="dateDebut">Date de début</label>
                <input type="date" id="dateDebut" name="dateDebut" required>
            </div>

            <div class="form-group">
                <label for="dateFin">Date de fin</label>
                <input type="date" id="dateFin" name="dateFin" required>
            </div>

            <div class="form-group">
                <label for="montant">Montant (€)</label>
                <input type="number" id="montant" name="montant"
                       step="0.01" min="0.01" required>
            </div>

            <div class="form-group">
                <label for="garantieIds">Garanties</label>
                <select id="garantieIds" name="garantieIds" multiple required>
                    <c:forEach var="garantie" items="${garanties}">
                        <c:if test="${garantie.actif}">
                            <option value="${garantie.id}">
                                <c:out value="${garantie.nom}"/>
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
                <p class="help-text">
                    Maintenez Ctrl (ou Cmd sur Mac) pour sélectionner plusieurs garanties.
                </p>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    Créer le contrat
                </button>

                <a href="${pageContext.request.contextPath}/admin/contrats"
                   class="btn btn-secondary">
                    Annuler
                </a>
            </div>

        </form>

    </div>
</div>

</body>
</html>