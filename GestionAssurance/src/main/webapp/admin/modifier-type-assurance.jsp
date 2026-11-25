<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Modifier un type d'assurance</title>

    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f3f5f9;
            color: #263247;
        }

        .sidebar {
            position: fixed;
            top: 0;
            bottom: 0;
            left: 0;
            width: 240px;
            padding: 28px 18px;
            background: #17243b;
            color: white;
        }

        .brand {
            font-size: 21px;
            font-weight: bold;
            margin-bottom: 38px;
        }

        .sidebar a {
            display: block;
            padding: 13px 14px;
            margin: 6px 0;
            color: #dce5f3;
            text-decoration: none;
            border-radius: 7px;
        }

        .sidebar a:hover,
        .sidebar a.active {
            background: #293b59;
            color: white;
        }

        .main {
            margin-left: 240px;
            padding: 35px;
        }

        h1 { margin-top: 0; }

        .subtitle {
            color: #78849a;
            margin-bottom: 25px;
        }

        .card {
            max-width: 750px;
            background: white;
            padding: 28px;
            border-radius: 12px;
            box-shadow: 0 3px 15px rgba(20,35,60,.06);
        }

        .form-group { margin-bottom: 20px; }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            font-size: 14px;
        }

        input, textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #d8deea;
            border-radius: 6px;
            font-size: 14px;
        }

        textarea {
            min-height: 110px;
            resize: vertical;
        }

        .checkbox-group {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .checkbox-group input { width: auto; }
        .checkbox-group label { margin: 0; }

        .buttons {
            display: flex;
            gap: 12px;
            margin-top: 25px;
        }

        .btn {
            padding: 11px 18px;
            border: none;
            border-radius: 6px;
            background: #315caa;
            color: white;
            text-decoration: none;
            cursor: pointer;
            font-size: 14px;
        }

        .btn:hover { background: #244987; }
        .btn-secondary { background: #7c8798; }

        .message {
            padding: 12px 15px;
            margin-bottom: 18px;
            border-radius: 7px;
            background: #fde8e8;
            color: #a52626;
        }

        @media(max-width:800px) {
            .sidebar {
                position: static;
                width: 100%;
            }

            .main {
                margin-left: 0;
                padding: 20px;
            }
        }
    </style>
</head>

<body>

<aside class="sidebar">
    <div class="brand">GestionAssurance</div>

    <a href="${pageContext.request.contextPath}/admin/dashboard">Tableau de bord</a>
    <a href="${pageContext.request.contextPath}/admin/utilisateurs">Utilisateurs</a>
    <a href="${pageContext.request.contextPath}/admin/roles">Gestion des rôles</a>
    <a class="active" href="${pageContext.request.contextPath}/admin/types-assurance">Types d'assurance</a>
    <a href="${pageContext.request.contextPath}/admin/contrats">Contrats</a>
    <a href="${pageContext.request.contextPath}/admin/sinistres">Sinistres</a>
    <a href="${pageContext.request.contextPath}/statistiques">Statistiques</a>
    <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
</aside>

<main class="main">

    <h1>Modifier un type d'assurance</h1>
    <p class="subtitle">Modifiez les informations du type d'assurance sélectionné.</p>

    <c:if test="${not empty messageErreur}">
        <div class="message">
            <c:out value="${messageErreur}"/>
        </div>
    </c:if>

    <section class="card">

        <form method="post"
              action="${pageContext.request.contextPath}/admin/modifier-type-assurance">

            <input type="hidden" name="id" value="${type.id}">

            <div class="form-group">
                <label for="nom">Nom du type d'assurance *</label>
                <input type="text"
                       id="nom"
                       name="nom"
                       maxlength="100"
                       value="<c:out value='${type.nom}'/>"
                       required>
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description"
                          name="description"><c:out value="${type.description}"/></textarea>
            </div>

            <div class="form-group">
                <label for="dureeMois">Durée (en mois) *</label>
                <input type="number"
                       id="dureeMois"
                       name="dureeMois"
                       min="1"
                       max="1200"
                       value="${type.dureeMois}"
                       required>
            </div>

            <div class="form-group">
                <label for="tarifBase">Prix de base (€) *</label>
                <input type="number"
                       id="tarifBase"
                       name="tarifBase"
                       min="0.01"
                       step="0.01"
                       value="${type.tarifBase}"
                       required>
            </div>

            <div class="buttons">
                <button type="submit" class="btn">
                    Enregistrer les modifications
                </button>

                <a href="${pageContext.request.contextPath}/admin/types-assurance"
                   class="btn btn-secondary">Annuler</a>
            </div>

        </form>

    </section>

</main>

</body>
</html>