<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des types d'assurance</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f4f6fa;
            color: #263449;
        }

        .topbar {
            background: #142d4e;
            color: white;
            padding: 22px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 21px;
            font-weight: bold;
        }

        .logo span {
            color: #45c3b1;
        }

        .back-link {
            color: white;
            text-decoration: none;
            font-size: 14px;
            padding: 10px 16px;
            border: 1px solid rgba(255,255,255,0.35);
            border-radius: 7px;
            transition: 0.2s;
        }

        .back-link:hover {
            background: #24466e;
        }

        .container {
            max-width: 1250px;
            margin: 40px auto;
            padding: 0 25px;
        }

        .page-heading {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
            margin-bottom: 28px;
        }

        .page-heading h1 {
            color: #142d4e;
            font-size: 27px;
            margin-bottom: 8px;
        }

        .page-heading p {
            color: #8490a0;
            font-size: 14px;
        }

        .btn-add {
            background: #168f91;
            color: white;
            text-decoration: none;
            padding: 13px 19px;
            border-radius: 8px;
            font-size: 14px;
            font-weight: bold;
            white-space: nowrap;
            transition: 0.2s;
        }

        .btn-add:hover {
            background: #117779;
        }

        .alert {
            padding: 15px 18px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .success {
            background: #e6f6ee;
            color: #20734a;
            border: 1px solid #c4ead5;
        }

        .error {
            background: #fff0f0;
            color: #b83232;
            border: 1px solid #f3cccc;
        }

        .table-container {
            background: white;
            border-radius: 12px;
            box-shadow: 0 3px 15px rgba(25,45,75,0.06);
            overflow-x: auto;
            border: 1px solid #edf0f5;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 850px;
        }

        thead {
            background: #f7f9fc;
        }

        th {
            text-align: left;
            padding: 18px 20px;
            color: #637187;
            font-size: 12px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            border-bottom: 1px solid #e9edf3;
        }

        td {
            padding: 18px 20px;
            border-bottom: 1px solid #edf0f5;
            font-size: 14px;
            color: #526176;
        }

        tbody tr:last-child td {
            border-bottom: none;
        }

        tbody tr:hover {
            background: #fafbfd;
        }

        .type-name {
            color: #142d4e;
            font-weight: bold;
        }

        .description {
            max-width: 280px;
            line-height: 1.5;
        }

        .badge {
            display: inline-block;
            padding: 6px 11px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }

        .badge-active {
            background: #e4f6ec;
            color: #23804d;
        }

        .badge-inactive {
            background: #f1f2f5;
            color: #737d8b;
        }

        .actions {
            display: flex;
            gap: 8px;
            align-items: center;
        }

        .actions form {
            margin: 0;
        }

        .btn-edit,
        .btn-disable {
            border: none;
            border-radius: 6px;
            padding: 9px 12px;
            font-size: 12px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            transition: 0.2s;
        }

        .btn-edit {
            background: #eaf2fc;
            color: #2864a0;
        }

        .btn-edit:hover {
            background: #d8e8fa;
        }

        .btn-disable {
            background: #fff0e7;
            color: #bd6533;
        }

        .btn-disable:hover {
            background: #ffe1cf;
        }

        .empty-state {
            text-align: center;
            padding: 45px 20px;
            color: #8490a0;
        }

        .empty-state h3 {
            color: #142d4e;
            margin-bottom: 10px;
        }

        .footer {
            text-align: center;
            color: #9aa5b4;
            font-size: 12px;
            padding: 30px;
        }

        @media (max-width: 650px) {
            .topbar {
                padding: 20px;
                flex-direction: column;
                gap: 18px;
                align-items: flex-start;
            }

            .page-heading {
                flex-direction: column;
                align-items: flex-start;
            }

            .container {
                margin-top: 25px;
                padding: 0 15px;
            }
        }
    </style>
</head>

<body>

    <header class="topbar">
        <div class="logo">
            Gestion<span>Assurance</span>
        </div>

        <a class="back-link"
           href="${pageContext.request.contextPath}/admin/dashboard">
            Retour au tableau de bord
        </a>
    </header>

    <main class="container">

        <section class="page-heading">
            <div>
                <h1>Types d'assurance</h1>
                <p>Consultez et gérez les différents produits d'assurance.</p>
            </div>

            <a class="btn-add"
               href="${pageContext.request.contextPath}/admin/ajouter-type-assurance.jsp">
                + Ajouter un type
            </a>
        </section>

        <%
            String messageSucces =
                (String) session.getAttribute("messageSucces");

            if (messageSucces != null) {
        %>
            <div class="alert success">
                <%= messageSucces %>
            </div>
        <%
                session.removeAttribute("messageSucces");
            }

            String messageErreur =
                (String) request.getAttribute("messageErreur");

            if (messageErreur != null) {
        %>
            <div class="alert error">
                <%= messageErreur %>
            </div>
        <%
            }
        %>

        <section class="table-container">

            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Description</th>
                        <th>Durée</th>
                        <th>Tarif de base</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>

                <tbody>

                    <c:choose>

                        <c:when test="${empty typesAssurance}">
                            <tr>
                                <td colspan="7">
                                    <div class="empty-state">
                                        <h3>Aucun type d'assurance</h3>
                                        <p>
                                            Aucun type n'est actuellement enregistré.
                                            Vous pouvez en ajouter un nouveau.
                                        </p>
                                    </div>
                                </td>
                            </tr>
                        </c:when>

                        <c:otherwise>

                            <c:forEach var="type" items="${typesAssurance}">

                                <tr>
                                    <td>${type.id}</td>

                                    <td class="type-name">
                                        <c:out value="${type.nom}"/>
                                    </td>

                                    <td class="description">
                                        <c:out value="${type.description}"/>
                                    </td>

                                    <td>
                                        ${type.dureeMois} mois
                                    </td>

                                    <td>
                                        ${type.tarifBase} €
                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${type.actif}">
                                                <span class="badge badge-active">
                                                    Actif
                                                </span>
                                            </c:when>

                                            <c:otherwise>
                                                <span class="badge badge-inactive">
                                                    Inactif
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <div class="actions">

                                            <form action="${pageContext.request.contextPath}/admin/modifier-type-assurance"
                                                  method="get">

                                                <input type="hidden"
                                                       name="id"
                                                       value="${type.id}">

                                                <button type="submit"
                                                        class="btn-edit">
                                                    Modifier
                                                </button>
                                            </form>

                                            <form action="${pageContext.request.contextPath}/admin/desactiver-type-assurance"
                                                  method="post"
                                                  onsubmit="return confirm('Voulez-vous vraiment désactiver ce type d’assurance ?');">

                                                <input type="hidden"
                                                       name="id"
                                                       value="${type.id}">

                                                <button type="submit"
                                                        class="btn-disable">
                                                    Désactiver
                                                </button>
                                            </form>

                                        </div>
                                    </td>

                                </tr>

                            </c:forEach>

                        </c:otherwise>

                    </c:choose>

                </tbody>
            </table>

        </section>

    </main>

    <footer class="footer">
        GestionAssurance — Administration
    </footer>

</body>
</html>