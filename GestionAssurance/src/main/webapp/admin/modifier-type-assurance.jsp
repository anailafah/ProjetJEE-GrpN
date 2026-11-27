<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Modifier un type d'assurance | GestionAssurance</title>

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
            gap: 15px;
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

        /* PRESENTATION */

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
            max-width: 850px;
            background: white;
            border: 1px solid #e9edf4;
            border-radius: 14px;
            padding: 28px;
            box-shadow: 0 4px 14px rgba(25,45,80,.03);
        }

        .card-header {
            margin-bottom: 25px;
        }

        .card-header h3 {
            font-size: 17px;
            color: #14233f;
        }

        .card-header p {
            color: #8290a5;
            font-size: 12px;
            margin-top: 7px;
        }

        /* FORM */

        .form-group {
            margin-bottom: 22px;
        }

        label {
            display: block;
            margin-bottom: 9px;
            color: #26344d;
            font-size: 13px;
            font-weight: bold;
        }

        .required {
            color: #e05252;
        }

        input,
        textarea {
            width: 100%;
            padding: 13px 14px;
            border: 1px solid #dce2ed;
            border-radius: 8px;
            font-size: 13px;
            font-family: Arial, sans-serif;
            color: #26344d;
            background: #fff;
            transition: .2s;
        }

        input:focus,
        textarea:focus {
            outline: none;
            border-color: #315efb;
            box-shadow: 0 0 0 3px rgba(49,94,251,.10);
        }

        textarea {
            min-height: 120px;
            resize: vertical;
        }

        .form-help {
            display: block;
            margin-top: 7px;
            color: #8290a5;
            font-size: 11px;
        }

        /* BUTTONS */

        .buttons {
            display: flex;
            align-items: center;
            gap: 12px;
            flex-wrap: wrap;
            margin-top: 28px;
            padding-top: 20px;
            border-top: 1px solid #edf0f5;
        }

        .btn {
            display: inline-block;
            padding: 12px 18px;
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

        .btn-secondary {
            background: #7c8798;
        }

        .btn-secondary:hover {
            background: #626d7e;
        }

        /* MESSAGE */

        .message {
            padding: 13px 16px;
            margin-bottom: 20px;
            border-radius: 9px;
            background: #fde8e8;
            color: #a52626;
            font-size: 13px;
        }

        /* FOOTER */

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
                flex-direction: column;
            }

            .card {
                padding: 20px;
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
                <h1>Modifier un type d'assurance</h1>
                <p>Actualisez les informations du type d'assurance sélectionné.</p>
            </div>

            <div class="admin-profile">
                Administrateur :
                <c:out value="${sessionScope.prenom}"/>
                <c:out value="${sessionScope.nom}"/>
            </div>

        </header>


        <!-- PRESENTATION -->

        <section class="welcome">

            <h2>Modification du type d'assurance</h2>

            <p>
                Modifiez les informations ci-dessous.
                Les changements seront enregistrés après validation du formulaire.
            </p>

        </section>


        <!-- MESSAGE ERREUR -->

        <c:if test="${not empty messageErreur}">

            <div class="message">
                <c:out value="${messageErreur}"/>
            </div>

        </c:if>


        <!-- FORMULAIRE -->

        <section class="card">

            <div class="card-header">

                <h3>Informations du type d'assurance</h3>

                <p>Les champs marqués d'un astérisque sont obligatoires.</p>

            </div>


            <form method="post"
                  action="${pageContext.request.contextPath}/admin/modifier-type-assurance">

                <input type="hidden" name="id" value="${type.id}">


                <div class="form-group">

                    <label for="nom">
                        Nom du type d'assurance <span class="required">*</span>
                    </label>

                    <input type="text"
                           id="nom"
                           name="nom"
                           maxlength="100"
                           value="<c:out value='${type.nom}'/>"
                           required>

                </div>


                <div class="form-group">

                    <label for="description">
                        Description
                    </label>

                    <textarea id="description"
                              name="description"><c:out value="${type.description}"/></textarea>

                </div>


                <div class="form-group">

                    <label for="dureeMois">
                        Durée (en mois) <span class="required">*</span>
                    </label>

                    <input type="number"
                           id="dureeMois"
                           name="dureeMois"
                           min="1"
                           max="1200"
                           value="${type.dureeMois}"
                           required>

                    <span class="form-help">
                        Indiquez la durée du type d'assurance en mois.
                    </span>

                </div>


                <div class="form-group">

                    <label for="tarifBase">
                        Prix de base (€) <span class="required">*</span>
                    </label>

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
                       class="btn btn-secondary">
                        Annuler
                    </a>

                </div>

            </form>

        </section>


        <footer class="footer">
            GestionAssurance — Espace Administrateur
        </footer>

    </main>

</div>

</body>
</html>