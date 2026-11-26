<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GestionAssurance | Utilisateurs</title>

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

.sidebar {
    position: fixed;
    width: 255px;
    height: 100vh;
    background: #14233f;
    padding: 28px 18px;
    color: white;
    display: flex;
    flex-direction: column;
}

.brand {
    font-size: 21px;
    font-weight: bold;
    padding: 0 12px 38px;
}

.brand span { color: #75a0ff; }

.menu-title {
    color: #8998b3;
    font-size: 10px;
    padding: 0 12px;
    margin-bottom: 14px;
}

.menu a {
    display: block;
    padding: 13px;
    margin-bottom: 6px;
    border-radius: 9px;
    color: #bdc8db;
    text-decoration: none;
    font-size: 13px;
}

.menu a:hover, .menu a.active {
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

.logout { color: #ffb1b1 !important; }

.main {
    margin-left: 255px;
    padding: 30px 38px;
}

.topbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 25px;
}

h1 {
    font-size: 25px;
    color: #14233f;
}

.subtitle {
    color: #8290a5;
    font-size: 12px;
    margin-top: 7px;
}

.profile {
    background: white;
    padding: 12px 17px;
    border-radius: 10px;
    font-size: 12px;
    font-weight: bold;
}

.back {
    display: inline-block;
    margin-bottom: 22px;
    color: #315efb;
    text-decoration: none;
    font-size: 12px;
    font-weight: bold;
}

.intro {
    background: linear-gradient(115deg, #1c3154, #315efb);
    color: white;
    border-radius: 15px;
    padding: 25px;
    margin-bottom: 25px;
}

.intro h2 {
    font-size: 19px;
    margin-bottom: 8px;
}

.intro p {
    font-size: 12px;
    color: #dce5ff;
    line-height: 1.6;
}

.card {
    background: white;
    border: 1px solid #e9edf4;
    border-radius: 14px;
    padding: 24px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 15px;
    margin-bottom: 22px;
}

.card-header h2 {
    font-size: 17px;
}

.btn-add {
    background: #315efb;
    color: white;
    padding: 11px 17px;
    border-radius: 8px;
    text-decoration: none;
    font-size: 12px;
    font-weight: bold;
}

.btn-add:hover { background: #244bd1; }

.search-form {
    display: flex;
    gap: 9px;
    margin-bottom: 20px;
}

.search-form input {
    width: 300px;
    max-width: 100%;
    padding: 11px 13px;
    border: 1px solid #e1e6ef;
    border-radius: 8px;
    outline: none;
}

.search-form input:focus { border-color: #315efb; }

.search-form button {
    border: none;
    background: #14233f;
    color: white;
    padding: 10px 17px;
    border-radius: 8px;
    cursor: pointer;
}

.table-container { overflow-x: auto; }

table {
    width: 100%;
    border-collapse: collapse;
    min-width: 850px;
}

th {
    text-align: left;
    background: #f8f9fc;
    color: #8290a5;
    padding: 15px 12px;
    font-size: 10px;
    text-transform: uppercase;
}

td {
    padding: 15px 12px;
    border-bottom: 1px solid #edf0f5;
    font-size: 12px;
}

td strong { color: #14233f; }

.role {
    display: inline-block;
    padding: 6px 10px;
    border-radius: 20px;
    background: #edf2ff;
    color: #315efb;
    font-weight: bold;
    font-size: 10px;
}

.role-ADMIN { background: #f0eaff; color: #7044bd; }
.role-AGENT { background: #e5f8ee; color: #16834b; }
.role-CLIENT { background: #eaf3ff; color: #2873bd; }

.status {
    padding: 6px 10px;
    border-radius: 20px;
    font-size: 10px;
    font-weight: bold;
}

.status-active {
    background: #e5f8ee;
    color: #16834b;
}

.status-inactive {
    background: #ffe8e8;
    color: #c03939;
}

.actions {
    display: flex;
    gap: 7px;
}

.btn-edit, .btn-disable {
    border: none;
    padding: 8px 10px;
    border-radius: 7px;
    font-size: 11px;
    font-weight: bold;
    cursor: pointer;
    text-decoration: none;
}

.btn-edit {
    background: #edf2ff;
    color: #315efb;
}

.btn-disable {
    background: #ffe8e8;
    color: #c03939;
}

.empty {
    text-align: center;
    padding: 45px 15px;
    color: #8290a5;
    font-size: 13px;
}

.empty h3 {
    color: #26344d;
    margin-bottom: 8px;
}

.message {
    padding: 13px 16px;
    border-radius: 8px;
    margin-bottom: 18px;
    font-size: 13px;
}

.success {
    background: #e5f8ee;
    color: #16834b;
}

.error {
    background: #ffe8e8;
    color: #c03939;
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

    .brand span { font-size: 20px; }
    .menu-title { display: none; }
    .menu a { font-size: 0; text-align: center; }

    .main {
        margin-left: 75px;
        padding: 20px 14px;
    }
}
</style>
</head>

<body>

<aside class="sidebar">

    <div class="brand">Gestion<span>Assurance</span></div>

    <div class="menu-title">ADMINISTRATION</div>

    <nav class="menu">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Tableau de bord</a>
        <a href="${pageContext.request.contextPath}/admin/utilisateurs" class="active">Utilisateurs</a>
        <a href="${pageContext.request.contextPath}/admin/roles">Rôles</a>
        <a href="${pageContext.request.contextPath}/admin/types-assurance">Types d'assurance</a>
        <a href="${pageContext.request.contextPath}/admin/contrats">Contrats</a>
        <a href="${pageContext.request.contextPath}/admin/sinistres">Sinistres</a>
        <a href="${pageContext.request.contextPath}/statistiques">Statistiques</a>
    </nav>

    <div class="sidebar-bottom">
        <nav class="menu">
            <a href="${pageContext.request.contextPath}/logout" class="logout">Déconnexion</a>
        </nav>
    </div>

</aside>

<main class="main">

    <header class="topbar">
        <div>
            <h1>Gestion des utilisateurs</h1>
            <p class="subtitle">Administration des comptes de la plateforme.</p>
        </div>

        <div class="profile">
            <c:out value="${sessionScope.prenom}"/>
            <c:out value="${sessionScope.nom}"/>
        </div>
    </header>

    <a class="back" href="${pageContext.request.contextPath}/admin/dashboard">
        ← Retour au tableau de bord
    </a>

    <section class="intro">
        <h2>Gestion des comptes</h2>
        <p>
            Consultez les utilisateurs, gérez leurs informations
            et contrôlez leurs accès à la plateforme.
        </p>
    </section>

    <section class="card">

        <div class="card-header">
            <h2>Liste des utilisateurs</h2>

            <a class="btn-add"
               href="${pageContext.request.contextPath}/admin/ajouter-utilisateur">
                + Ajouter un utilisateur
            </a>
        </div>

        <c:if test="${not empty sessionScope.messageSucces}">
            <div class="message success">
                <c:out value="${sessionScope.messageSucces}"/>
            </div>
            <c:remove var="messageSucces" scope="session"/>
        </c:if>

        <c:if test="${not empty messageErreur}">
            <div class="message error">
                <c:out value="${messageErreur}"/>
            </div>
        </c:if>

        <form class="search-form"
              action="${pageContext.request.contextPath}/admin/utilisateurs"
              method="get">

            <input type="text"
                   name="recherche"
                   placeholder="Rechercher par nom, prénom ou email..."
                   value="${param.recherche}">

            <button type="submit">Rechercher</button>
        </form>

        <div class="table-container">

            <c:choose>
                <c:when test="${not empty utilisateurs}">

                    <table>
                        <thead>
                            <tr>
                                <th>Nom</th>
                                <th>Prénom</th>
                                <th>Email</th>
                                <th>Téléphone</th>
                                <th>Rôle</th>
                                <th>Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="utilisateur" items="${utilisateurs}">
                                <tr>
                                    <td><strong><c:out value="${utilisateur.nom}"/></strong></td>
                                    <td><c:out value="${utilisateur.prenom}"/></td>
                                    <td><c:out value="${utilisateur.email}"/></td>
                                    <td><c:out value="${utilisateur.telephone}"/></td>

                                    <td>
                                        <span class="role role-${utilisateur.role}">
                                            <c:out value="${utilisateur.role}"/>
                                        </span>
                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${utilisateur.actif}">
                                                <span class="status status-active">Actif</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status status-inactive">Inactif</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <div class="actions">

                                            <a class="btn-edit"
                                               href="${pageContext.request.contextPath}/admin/modifier-utilisateur?id=${utilisateur.id}">
                                                Modifier
                                            </a>

                                            <form action="${pageContext.request.contextPath}/admin/desactiver-utilisateur"
                                                  method="post"
                                                  onsubmit="return confirm('Confirmer le changement de statut de cet utilisateur ?');">

                                                <input type="hidden" name="id" value="${utilisateur.id}">

                                                <button type="submit" class="btn-disable">
                                                    Changer statut
                                                </button>
                                            </form>

                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                </c:when>

                <c:otherwise>
                    <div class="empty">
                        <h3>Aucun utilisateur trouvé</h3>
                        <p>Les utilisateurs apparaîtront ici après le chargement des données.</p>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </section>

</main>

</body>
</html>