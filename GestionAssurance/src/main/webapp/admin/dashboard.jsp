<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GestionAssurance | Administration</title>


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

/* STATISTICS */

.section-title {
    font-size: 17px;
    font-weight: bold;
    margin-bottom: 17px;
    color: #14233f;
}

.stats-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 17px;
    margin-bottom: 30px;
}

.stat-card {
    background: white;
    border: 1px solid #e9edf4;
    border-radius: 14px;
    padding: 22px;
    box-shadow: 0 4px 14px rgba(25,45,80,.03);
}

.stat-label {
    color: #8290a5;
    font-size: 12px;
    margin-bottom: 14px;
}

.stat-value {
    color: #14233f;
    font-size: 27px;
    font-weight: 800;
}

.stat-description {
    color: #9aa5b5;
    font-size: 11px;
    margin-top: 9px;
}

/* CHARTS */

.charts-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
    margin-bottom: 30px;
}

.chart-card {
    background: white;
    border: 1px solid #e9edf4;
    border-radius: 14px;
    padding: 23px;
    min-width: 0;
}

.chart-card h3 {
    font-size: 14px;
    margin-bottom: 20px;
    color: #26344d;
}

.chart-container {
    height: 270px;
    position: relative;
}

.chart-full {
    grid-column: 1 / -1;
}

/* MANAGEMENT */

.management-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 17px;
    margin-bottom: 30px;
}

.management-card {
    background: white;
    border: 1px solid #e9edf4;
    border-radius: 14px;
    padding: 23px;
    transition: .2s;
}

.management-card:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba(25,45,80,.07);
}

.management-card h3 {
    font-size: 15px;
    color: #14233f;
    margin-bottom: 10px;
}

.management-card p {
    color: #8290a5;
    font-size: 12px;
    line-height: 1.6;
    margin-bottom: 18px;
}

.management-card a {
    display: inline-block;
    color: #315efb;
    font-size: 12px;
    font-weight: bold;
    text-decoration: none;
}

.management-card a:hover {
    text-decoration: underline;
}

.footer {
    text-align: center;
    color: #9aa5b5;
    font-size: 11px;
    padding: 15px;
}

/* RESPONSIVE */

@media(max-width: 1100px) {
    .stats-grid {
        grid-template-columns: repeat(2, 1fr);
    }

    .management-grid {
        grid-template-columns: repeat(2, 1fr);
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

    .charts-grid {
        grid-template-columns: 1fr;
    }

    .chart-full {
        grid-column: auto;
    }

    .management-grid {
        grid-template-columns: 1fr;
    }

    .topbar {
        align-items: flex-start;
        gap: 15px;
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
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="active">
                Tableau de bord
            </a>

            <a href="${pageContext.request.contextPath}/admin/utilisateurs">
                Utilisateurs
            </a>

            <a href="${pageContext.request.contextPath}/admin/roles">
                Rôles
            </a>

            <a href="${pageContext.request.contextPath}/admin/types-assurance">
                Types d'assurance
            </a>

            <a href="${pageContext.request.contextPath}/admin/contrats">
                Contrats
            </a>

            <a href="${pageContext.request.contextPath}/admin/sinistres">
                Sinistres
            </a>
        </nav>

        <div class="sidebar-bottom">
            <nav class="menu">
                <a href="${pageContext.request.contextPath}/logout" class="logout">
                    Déconnexion
                </a>
            </nav>
        </div>

    </aside>

    <!-- CONTENU PRINCIPAL -->
    <main class="main">

        <header class="topbar">
            <div>
                <h1>Tableau de bord</h1>
                <p>Vue globale de la plateforme GestionAssurance</p>
            </div>

            <div class="admin-profile">
                Administrateur :
                <c:out value="${sessionScope.prenom}"/>
                <c:out value="${sessionScope.nom}"/>
            </div>
        </header>

        <!-- ACCUEIL -->
        <section class="welcome">
            <h2>Bienvenue dans votre espace d'administration</h2>
            <p>
                Gérez les utilisateurs, les rôles et les types d'assurance.
                Consultez les contrats, les sinistres et les performances globales.
            </p>
        </section>

        <!-- STATISTIQUES (fragment partagé avec l'espace agent) -->
        <%@ include file="/WEB-INF/views/statistiques/tableau.jspf" %>

        <!-- ACCÈS AUX GESTIONS -->
        <h2 class="section-title">Gestion de la plateforme</h2>

        <section class="management-grid">

            <div class="management-card">
                <h3>Gestion des utilisateurs</h3>
                <p>
                    Consultez les comptes, ajoutez des utilisateurs,
                    modifiez leurs informations ou désactivez-les.
                </p>
                <a href="${pageContext.request.contextPath}/admin/utilisateurs">
                    Accéder aux utilisateurs →
                </a>
            </div>

            <div class="management-card">
                <h3>Gestion des rôles</h3>
                <p>
                    Consultez et gérez les rôles et les accès
                    des différents utilisateurs.
                </p>
                <a href="${pageContext.request.contextPath}/admin/roles">
                    Gérer les rôles →
                </a>
            </div>

            <div class="management-card">
                <h3>Types d'assurance</h3>
                <p>
                    Ajoutez, modifiez ou supprimez les types
                    d'assurance proposés par la plateforme.
                </p>
                <a href="${pageContext.request.contextPath}/admin/types-assurance">
                    Gérer les types →
                </a>
            </div>

            <div class="management-card">
                <h3>Consultation des contrats</h3>
                <p>
                    Consultez l'ensemble des contrats
                    enregistrés dans la plateforme.
                </p>
                <a href="${pageContext.request.contextPath}/admin/contrats">
                    Consulter les contrats →
                </a>
            </div>

            <div class="management-card">
                <h3>Consultation des sinistres</h3>
                <p>
                    Consultez l'ensemble des sinistres
                    déclarés par les clients.
                </p>
                <a href="${pageContext.request.contextPath}/admin/sinistres">
                    Consulter les sinistres →
                </a>
            </div>

        </section>

        <footer class="footer">
            GestionAssurance — Espace Administrateur
        </footer>

    </main>

</div>

</body>
