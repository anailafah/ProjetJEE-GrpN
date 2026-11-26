<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion des garanties</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f6f9;
        }

        .sidebar {
            position: fixed;
            width: 240px;
            height: 100vh;
            background: #172b4d;
            color: white;
            padding: 25px 15px;
        }

        .sidebar h2 {
            text-align: center;
            margin-bottom: 35px;
        }

        .sidebar a {
            display: block;
            color: white;
            text-decoration: none;
            padding: 13px;
            margin-bottom: 8px;
            border-radius: 6px;
        }

        .sidebar a:hover,
        .sidebar a.active {
            background: #294875;
        }

        .main {
            margin-left: 240px;
            padding: 30px;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.06);
        }

        .btn-ajouter {
            display: inline-block;
            background: #198754;
            color: white;
            padding: 10px 16px;
            border-radius: 6px;
            text-decoration: none;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        th, td {
            padding: 13px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        th {
            background: #f1f4f8;
        }

        .badge {
            padding: 6px 10px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: bold;
        }

        .actif {
            background: #d1e7dd;
            color: #0f5132;
        }

        .inactif {
            background: #f8d7da;
            color: #842029;
        }

        .btn-modifier {
            background: #0d6efd;
            color: white;
            padding: 7px 10px;
            border-radius: 5px;
            text-decoration: none;
        }

        .btn-supprimer {
            background: #dc3545;
            color: white;
            padding: 7px 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .message {
            padding: 12px;
            margin-bottom: 15px;
            border-radius: 6px;
            background: #d1e7dd;
            color: #0f5132;
        }

        .message-erreur {
            background: #f8d7da;
            color: #842029;
        }
    </style>
</head>

<body>

<div class="sidebar">
    <h2>Assurance</h2>

    <a href="${pageContext.request.contextPath}/agent/dashboard">Tableau de bord</a>
    <a href="${pageContext.request.contextPath}/agent/clients">Clients</a>
    <a href="${pageContext.request.contextPath}/agent/contrats">Contrats</a>
    <a href="${pageContext.request.contextPath}/agent/sinistres">Sinistres</a>
    <a href="${pageContext.request.contextPath}/agent/garanties" class="active">Garanties</a>
    <a href="${pageContext.request.contextPath}/agent/paiements">Paiements</a>
    <a href="${pageContext.request.contextPath}/agent/statistiques">Statistiques</a>
    <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
</div>

<div class="main">

    <div class="topbar">
        <h1>Gestion des garanties</h1>
        <span>Espace Agent</span>
    </div>

    <div class="card">

        <c:if test="${not empty sessionScope.messageSucces}">
            <div class="message">
                <c:out value="${sessionScope.messageSucces}"/>
            </div>
            <c:remove var="messageSucces" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.messageErreur}">
            <div class="message message-erreur">
                <c:out value="${sessionScope.messageErreur}"/>
            </div>
            <c:remove var="messageErreur" scope="session"/>
        </c:if>

        <a class="btn-ajouter"
           href="${pageContext.request.contextPath}/agent/ajouter-garantie">
            + Ajouter une garantie
        </a>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Description</th>
                    <th>Montant maximal</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>

            <tbody>
                <c:forEach var="garantie" items="${garanties}">
                    <tr>
                        <td><c:out value="${garantie.id}"/></td>
                        <td><c:out value="${garantie.nom}"/></td>
                        <td><c:out value="${garantie.description}"/></td>
                        <td>
                            <c:out value="${garantie.montantCouverture}"/> €
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${garantie.actif}">
                                    <span class="badge actif">Active</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge inactif">Inactive</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <a class="btn-modifier"
                               href="${pageContext.request.contextPath}/agent/modifier-garantie?id=${garantie.id}">
                                Modifier
                            </a>
                            <c:if test="${garantie.actif}">
    <form action="${pageContext.request.contextPath}/agent/desactiver-garantie"
          method="post"
          style="display:inline;"
          onsubmit="return confirm('Désactiver cette garantie ?');">

        <input type="hidden" name="id" value="${garantie.id}">

        <button type="submit"
                style="background:#6c757d;color:white;padding:7px 10px;border:none;border-radius:5px;cursor:pointer;">
            Désactiver
        </button>
    </form>
</c:if>

                            <form action="${pageContext.request.contextPath}/agent/supprimer-garantie"
                                  method="post"
                                  style="display:inline;"
                                  onsubmit="return confirm('Voulez-vous vraiment supprimer cette garantie ?');">

                                <input type="hidden" name="id" value="${garantie.id}">

                                <button type="submit" class="btn-supprimer">
                                    Supprimer
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty garanties}">
                    <tr>
                        <td colspan="6" style="text-align:center;">
                            Aucune garantie disponible.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>

    </div>
</div>

</body>
</html>