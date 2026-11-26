<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Types d'assurance - GestionAssurance</title>

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
.brand { font-size: 21px; font-weight: bold; margin-bottom: 38px; }
.sidebar a {
    display: block;
    padding: 13px 14px;
    margin: 6px 0;
    color: #dce5f3;
    text-decoration: none;
    border-radius: 7px;
}
.sidebar a:hover, .sidebar a.active {
    background: #293b59;
    color: white;
}
.main { margin-left: 240px; padding: 35px; }
h1 { margin-top: 0; }
.subtitle { color: #78849a; margin-bottom: 25px; }
.card {
    background: white;
    padding: 24px;
    border-radius: 12px;
    box-shadow: 0 3px 15px rgba(20,35,60,.06);
}
.actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}
.btn {
    display: inline-block;
    padding: 10px 16px;
    border: none;
    border-radius: 6px;
    background: #315caa;
    color: white;
    text-decoration: none;
    cursor: pointer;
}
.btn:hover { background: #244987; }
.btn-danger { background: #c0392b; }
.btn-danger:hover { background: #a93226; }
table { width: 100%; border-collapse: collapse; }
th, td {
    padding: 14px 12px;
    text-align: left;
    border-bottom: 1px solid #edf0f5;
}
th { color: #68758b; font-size: 13px; }
.badge {
    padding: 5px 9px;
    border-radius: 15px;
    font-size: 12px;
}
.actif { background: #e2f5e9; color: #18723b; }
.inactif { background: #fde8e8; color: #a52626; }
.message {
    padding: 12px 15px;
    margin-bottom: 18px;
    border-radius: 7px;
    background: #e2f5e9;
    color: #18723b;
}
.error { background: #fde8e8; color: #a52626; }
.empty { text-align: center; color: #8490a3; padding: 25px; }
@media(max-width:800px) {
    .sidebar { position: static; width: 100%; }
    .main { margin-left: 0; padding: 20px; }
    .card { overflow-x: auto; }
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

    <h1>Gestion des types d'assurance</h1>
    <p class="subtitle">Consultez et gérez les différents types d'assurance.</p>

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

    <section class="card">

        <div class="actions">
            <h3>Liste des types d'assurance</h3>

            <a class="btn"
               href="${pageContext.request.contextPath}/admin/ajouter-type-assurance">
                + Ajouter un type
            </a>
        </div>

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
                        <td><c:out value="${type.nom}"/></td>
                        <td><c:out value="${type.description}"/></td>
                        <td><c:out value="${type.dureeMois}"/></td>
                        <td><c:out value="${type.tarifBase}"/> €</td>

                        <td>
                            <c:choose>
                                <c:when test="${type.actif}">
                                    <span class="badge actif">Actif</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge inactif">Inactif</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <a class="btn"
                               href="${pageContext.request.contextPath}/admin/modifier-type-assurance?id=${type.id}">
                                Modifier
                            </a>

                            <form method="post"
                                  action="${pageContext.request.contextPath}/admin/desactiver-type-assurance"
                                  style="display:inline;">

                                <input type="hidden" name="id" value="${type.id}">

                                <button class="btn btn-danger" type="submit">
                                    Changer statut
                                </button>
                            </form>
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

    </section>
</main>
</body>
</html>