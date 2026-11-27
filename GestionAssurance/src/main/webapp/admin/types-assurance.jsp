<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Types d'assurance | GestionAssurance</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f4f6fb;
            color: #26344d;
        }

        /* LAYOUT */

        .layout {
            display: flex;
            min-height: 100vh;
        }

        /* SIDEBAR */

        .sidebar {
            width: 255px;
            min-height: 100vh;
            background: #14233f;
            color: white;
            padding: 28px 18px;
            position: fixed;
            top: 0;
            left: 0;
            display: flex;
            flex-direction: column;
        }

        .brand {
            font-size: 21px;
            font-weight: 800;
            padding: 0 12px 38px;
        }

        .brand span {
            color: #75a0ff;
        }

        .menu-title {
            color: #8998b3;
            font-size: 10px;
            font-weight: bold;
            letter-spacing: 1.4px;
            padding: 0 12px;
            margin-bottom: 14px;
        }

        .menu a {
            display: block;
            padding: 13px 14px;
            margin-bottom: 6px;
            border-radius: 9px;
            color: #bdc8db;
            text-decoration: none;
            font-size: 13px;
            transition: .2s;
        }

        .menu a:hover,
        .menu a.active {
            background: #263d63;
            color: white;
        }

        .menu a.active {
            border-left: 3px solid #75a0ff;
        }

        .sidebar-bottom {
            margin-top: auto;
            border-top: 1px solid #34445e;
            padding-top: 18px;
        }

        .logout {
            color: #ffb1b1 !important;
        }

        /* MAIN */

        .main {
            margin-left: 255px;
            width: calc(100% - 255px);
            padding: 30px 38px;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .topbar h1 {
            font-size: 25px;
            color: #14233f;
        }

        .topbar p {
            color: #8290a5;
            font-size: 12px;
            margin-top: 7px;
        }

        .admin-profile {
            background: white;
            border: 1px solid #e9edf4;
            border-radius: 12px;
            padding: 11px 17px;
            font-size: 12px;
            font-weight: bold;
        }

        /* WELCOME */

        .welcome {
            background: linear-gradient(115deg, #1c3154, #315efb);
            border-radius: 16px;
            padding: 27px 30px;
            color: white;
            margin-bottom: 28px;
        }

        .welcome h2 {
            font-size: 21px;
            margin-bottom: 9px;
        }

        .welcome p {
            font-size: 13px;
            color: #dce5ff;
            line-height: 1.6;
        }

        /* CARD */

        .card {
            background: white;
            border: 1px solid #e9edf4;
            border-radius: 14px;
            padding: 23px;
            box-shadow: 0 4px 14px rgba(25,45,80,.03);
        }

        .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            margin-bottom: 22px;
        }

        .card-header h3 {
            font-size: 16px;
            color: #14233f;
        }

        /* BUTTONS */

        .btn {
            display: inline-block;
            padding: 10px 15px;
            border: none;
            border-radius: 7px;
            background: #315efb;
            color: white;
            text-decoration: none;
            font-size: 12px;
            font-weight: bold;
            cursor: pointer;
            transition: .2s;
        }

        .btn:hover {
            background: #244bd1;
        }

        .btn-danger {
            background: #e05252;
        }

        .btn-danger:hover {
            background: #c0392b;
        }

        .btn-success {
            background: #18a06b;
        }

        .btn-success:hover {
            background: #128052;
        }

        .btn-edit {
            background: #315efb;
        }

        .actions {
            display: flex;
            align-items: center;
            gap: 7px;
            flex-wrap: wrap;
        }

        .actions form {
            display: inline;
        }

        /* TABLE */

        .table-container {
            width: 100%;
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 750px;
        }

        th,
        td {
            padding: 15px 13px;
            text-align: left;
            border-bottom: 1px solid #edf0f5;
            font-size: 13px;
        }

        th {
            color: #8290a5;
            font-size: 11px;
            text-transform: uppercase;
            letter-spacing: .4px;
            background: #f9faff;
        }

        tbody tr:hover {
            background: #f8faff;
        }

        /* BADGES */

        .badge {
            display: inline-block;
            padding: 6px 11px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: bold;
        }

        .actif {
            background: #e2f5e9;
            color: #18723b;
        }

        .inactif {
            background: #fde8e8;
            color: #a52626;
        }

        /* MESSAGES */

        .message {
            padding: 13px 16px;
            margin-bottom: 18px;
            border-radius: 9px;
            background: #e2f5e9;
            color: #18723b;
            font-size: 13px;
        }

        .error {
            background: #fde8e8;
            color: #a52626;
        }

        .empty {
            text-align: center;
            color: #8290a5;
            padding: 30px;
        }

        .footer {
            text-align: center;
            color: #9aa5b5;
            font-size: 11px;
            padding: 25px 15px;
        }

        /* RESPONSIVE */

        @media(max-width: 1100px) {
            .main {
                padding: 25px;
            }
        }

        @media(max-width: 750px) {
            .sidebar {
                width: 75px;
                padding: 20px 8px;
            }

            .brand {
                font-size: 0;
                padding: 10px 5px 30px;
            }

            .brand span {
                font-size: 20px;
            }

            .menu-title {
                display: none;
            }

            .menu a {
                font-size: 0;
                text-align: center;
                padding: 13px 5px;
            }

            .main {
                margin-left: 75px;
                width: calc(100% - 75px);
                padding: 20px 14px;
            }

            .topbar {
                align-items: flex-start;
                gap: 15px;
                flex-direction: column;
            }

            .card-header {
                align-items: flex-start;
                flex-direction: column;
            }
        }
    </style>
</head>

<body>

<div class="layout">

    <!-- MENU LATERAL -->

    <aside class="sidebar">

        <div class="brand">
            Gestion<span>Assurance</span>
        </div>

        <div class="menu-title">ADMINISTRATION</div>

        <nav class="menu">

            <a href="${pageContext.request.contextPath}/admin/dashboard">
                Tableau de bord
            </a>

            <a href="${pageContext.request.contextPath}/admin/utilisateurs">
                Utilisateurs
            </a>

            <a href="${pageContext.request.contextPath}/admin/roles">
                Rôles
            </a>

            <a class="active"
               href="${pageContext.request.contextPath}/admin/types-assurance">
                Types d'assurance
            </a>

            <a href="${pageContext.request.contextPath}/admin/contrats">
                Contrats
            </a>

            <a href="${pageContext.request.contextPath}/admin/sinistres">
                Sinistres
            </a>

            <a href="${pageContext.request.contextPath}/statistiques">
                Statistiques
            </a>

        </nav>

        <div class="sidebar-bottom">
            <nav class="menu">
                <a href="${pageContext.request.contextPath}/logout"
                   class="logout">
                    Déconnexion
                </a>
            </nav>
        </div>

    </aside>


    <!-- CONTENU PRINCIPAL -->

    <main class="main">

        <!-- EN-TÊTE -->

        <header class="topbar">

            <div>
                <h1>Gestion des types d'assurance</h1>
                <p>Consultez et gérez les types d'assurance de la plateforme.</p>
            </div>

            <div class="admin-profile">
                Administrateur :
                <c:out value="${sessionScope.prenom}"/>
                <c:out value="${sessionScope.nom}"/>
            </div>

        </header>


        <!-- PRESENTATION -->

        <section class="welcome">

            <h2>Gestion des types d'assurance</h2>

            <p>
                Ajoutez de nouveaux types d'assurance, modifiez leurs informations
                et gérez leur disponibilité pour les nouvelles souscriptions.
                Les types désactivés sont conservés dans la base de données.
            </p>

        </section>


        <!-- MESSAGES -->

        <c:if test="${not empty sessionScope.messageSucces}">

            <div class="message">
                <c:out value="${sessionScope.messageSucces}"/>
            </div>

            <c:remove var="messageSucces" scope="session"/>

        </c:if>


        <c:if test="${not empty sessionScope.messageErreur}">

            <div class="message error">
                <c:out value="${sessionScope.messageErreur}"/>
            </div>

            <c:remove var="messageErreur" scope="session"/>

        </c:if>


        <!-- LISTE DES TYPES -->

        <section class="card">

            <div class="card-header">

                <h3>Liste des types d'assurance</h3>

                <a class="btn"
                   href="${pageContext.request.contextPath}/admin/ajouter-type-assurance">
                    + Ajouter un type
                </a>

            </div>


            <div class="table-container">

                <table>

                    <thead>
                        <tr>
                            <th>Nom</th>
                            <th>Description</th>
                            <th>Durée (mois)</th>
                            <th>Prix de base</th>
                            <th>Statut</th>
                            <th>Actions</th>
                        </tr>
                    </thead>


                    <tbody>

                        <c:forEach var="type" items="${typesAssurance}">

                            <tr>

                                <td>
                                    <c:out value="${type.nom}"/>
                                </td>

                                <td>
                                    <c:out value="${type.description}"/>
                                </td>

                                <td>
                                    <c:out value="${type.dureeMois}"/>
                                </td>

                                <td>
                                    <c:out value="${type.tarifBase}"/> €
                                </td>

                                <td>

                                    <c:choose>

                                        <c:when test="${type.actif}">
                                            <span class="badge actif">
                                                Actif
                                            </span>
                                        </c:when>

                                        <c:otherwise>
                                            <span class="badge inactif">
                                                Inactif
                                            </span>
                                        </c:otherwise>

                                    </c:choose>

                                </td>


                                <td>

                                    <div class="actions">

                                        <!-- MODIFICATION -->

                                        <a class="btn btn-edit"
                                           href="${pageContext.request.contextPath}/admin/modifier-type-assurance?id=${type.id}">
                                            Modifier
                                        </a>


                                        <!-- DESACTIVATION OU REACTIVATION -->

                                        <c:choose>

                                            <c:when test="${type.actif}">

                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/admin/desactiver-type-assurance"
                                                      onsubmit="return confirm('Voulez-vous vraiment désactiver ce type d’assurance ?');">

                                                    <input type="hidden"
                                                           name="id"
                                                           value="${type.id}">

                                                    <button class="btn btn-danger"
                                                            type="submit">
                                                        Désactiver
                                                    </button>

                                                </form>

                                            </c:when>


                                            <c:otherwise>

                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/admin/activer-type-assurance"
                                                      onsubmit="return confirm('Voulez-vous vraiment réactiver ce type d’assurance ?');">

                                                    <input type="hidden"
                                                           name="id"
                                                           value="${type.id}">

                                                    <button class="btn btn-success"
                                                            type="submit">
                                                        Réactiver
                                                    </button>

                                                </form>

                                            </c:otherwise>

                                        </c:choose>

                                    </div>

                                </td>

                            </tr>

                        </c:forEach>


                        <c:if test="${empty typesAssurance}">

                            <tr>
                                <td colspan="6" class="empty">
                                    Aucun type d'assurance enregistré.
                                </td>
                            </tr>

                        </c:if>

                    </tbody>

                </table>

            </div>

        </section>


        <footer class="footer">
            GestionAssurance — Espace Administrateur
        </footer>

    </main>

</div>

</body>
</html>