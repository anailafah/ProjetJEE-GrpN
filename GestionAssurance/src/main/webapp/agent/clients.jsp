<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GestionAssurance | Clients</title>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap');

* { box-sizing: border-box; margin: 0; padding: 0; }

body {
    font-family: 'Inter', sans-serif;
    background: #f4f6fb;
    color: #26344d;
}

.layout { display: flex; min-height: 100vh; }

.sidebar {
    width: 255px;
    background: #14233f;
    color: white;
    padding: 28px 18px;
    position: fixed;
    height: 100vh;
    display: flex;
    flex-direction: column;
}

.brand {
    font-size: 21px;
    font-weight: 800;
    padding: 0 12px 35px;
}

.brand span { color: #75a0ff; }

.menu-title {
    color: #8998b3;
    font-size: 10px;
    font-weight: 700;
    letter-spacing: 1.5px;
    padding: 0 12px;
    margin-bottom: 13px;
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

.menu a:hover, .menu a.active {
    background: #263d63;
    color: white;
}

.menu a.active { border-left: 3px solid #75a0ff; }

.sidebar-bottom {
    margin-top: auto;
    border-top: 1px solid #34445e;
    padding-top: 18px;
}

.logout { color: #ffb1b1 !important; }

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

.topbar h1 { font-size: 24px; font-weight: 800; }

.topbar p {
    font-size: 12px;
    color: #8290a5;
    margin-top: 7px;
}

.profile {
    background: white;
    padding: 10px 16px;
    border: 1px solid #e9edf4;
    border-radius: 12px;
    font-size: 12px;
    font-weight: 700;
}

.welcome {
    background: linear-gradient(115deg, #1c3154, #315efb);
    border-radius: 15px;
    padding: 25px 28px;
    color: white;
    margin-bottom: 28px;
}

.welcome h2 { font-size: 19px; margin-bottom: 8px; }
.welcome p { font-size: 12px; color: #dce5ff; }

.content-card {
    background: white;
    border-radius: 15px;
    padding: 25px;
    border: 1px solid #e9edf4;
    box-shadow: 0 4px 15px rgba(25,45,80,.03);
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 15px;
    margin-bottom: 23px;
    flex-wrap: wrap;
}

.card-header h2 { font-size: 17px; }

.search-box {
    display: flex;
    gap: 10px;
}

.search-box input {
    width: 280px;
    padding: 11px 14px;
    border: 1px solid #e1e6ef;
    border-radius: 9px;
    outline: none;
    font-family: inherit;
    font-size: 12px;
}

.search-box input:focus { border-color: #315efb; }

.search-box button {
    background: #315efb;
    color: white;
    border: none;
    padding: 11px 18px;
    border-radius: 9px;
    cursor: pointer;
    font-weight: 600;
}

.table-container { overflow-x: auto; }

table {
    width: 100%;
    border-collapse: collapse;
    min-width: 650px;
}

th {
    text-align: left;
    color: #8290a5;
    font-size: 10px;
    text-transform: uppercase;
    letter-spacing: .6px;
    padding: 15px 12px;
    background: #f8f9fc;
}

td {
    padding: 16px 12px;
    border-bottom: 1px solid #edf0f5;
    font-size: 12px;
}

td strong { color: #14233f; }

.empty-state {
    text-align: center;
    padding: 55px 20px;
    color: #8290a5;
    font-size: 13px;
}

.empty-state h3 {
    color: #26344d;
    font-size: 16px;
    margin-bottom: 8px;
}

.back-link {
    display: inline-block;
    margin-bottom: 20px;
    color: #315efb;
    font-size: 12px;
    font-weight: 700;
    text-decoration: none;
}

@media(max-width: 750px) {
    .sidebar { width: 75px; padding: 20px 8px; }
    .brand { font-size: 0; padding: 10px 5px 30px; }
    .brand span { font-size: 20px; }
    .menu-title { display: none; }
    .menu a { font-size: 0; text-align: center; }
    .main { margin-left: 75px; width: calc(100% - 75px); padding: 20px 14px; }
    .search-box { width: 100%; }
    .search-box input { width: 100%; }
}
</style>
</head>

<body>

<div class="layout">

<aside class="sidebar">
    <div class="brand">Gestion<span>Assurance</span></div>

    <div class="menu-title">MENU PRINCIPAL</div>

    <nav class="menu">
        <a href="${pageContext.request.contextPath}/agent/dashboard">Tableau de bord</a>
        <a href="${pageContext.request.contextPath}/agent/clients" class="active">Clients</a>
        <a href="${pageContext.request.contextPath}/agent/contrats">Contrats</a>
        <a href="${pageContext.request.contextPath}/agent/sinistres">Sinistres</a>
        <a href="${pageContext.request.contextPath}/agent/garanties">Garanties</a>
        <a href="${pageContext.request.contextPath}/agent/paiements">Paiements</a>
        <a href="${pageContext.request.contextPath}/agent/statistiques">Statistiques</a>
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
            <h1>Gestion des clients</h1>
            <p>Consultez les informations des clients de l'agence.</p>
        </div>

        <div class="profile">
            <c:out value="${sessionScope.prenom}"/>
            <c:out value="${sessionScope.nom}"/>
        </div>
    </header>

    <a class="back-link" href="${pageContext.request.contextPath}/agent/dashboard">
        ← Retour au tableau de bord
    </a>

    <section class="welcome">
        <h2>Espace clients</h2>
        <p>Retrouvez les informations personnelles et les coordonnées des clients.</p>
    </section>

    <section class="content-card">

        <div class="card-header">
            <h2>Liste des clients</h2>

            <form class="search-box" action="${pageContext.request.contextPath}/agent/clients" method="get">
                <input type="text" name="recherche" placeholder="Rechercher un client..."
                       value="<c:out value='${param.recherche}'/>">
                <button type="submit">Rechercher</button>
            </form>
        </div>

        <div class="table-container">

            <c:choose>
                <c:when test="${not empty clients}">
                    <table>
                        <thead>
                            <tr>
                                <th>Nom</th>
                                <th>Prénom</th>
                                <th>Email</th>
                                <th>Téléphone</th>
                                <th>Adresse</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="client" items="${clients}">
                                <tr>
                                    <td><strong><c:out value="${client.nom}"/></strong></td>
                                    <td><c:out value="${client.prenom}"/></td>
                                    <td><c:out value="${client.email}"/></td>
                                    <td><c:out value="${client.telephone}"/></td>
                                    <td><c:out value="${client.adresse}"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>

                <c:otherwise>
                    <div class="empty-state">
                        <h3>Aucun client à afficher</h3>
                        <p>Les clients apparaîtront ici lorsque les données seront chargées.</p>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>

    </section>

</main>
</div>

</body>
</html>