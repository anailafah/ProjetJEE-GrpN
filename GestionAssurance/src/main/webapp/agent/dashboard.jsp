<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GestionAssurance | Espace Agent</title>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap');

:root {
    --navy: #14233f;
    --blue: #315efb;
    --background: #f4f6fb;
    --text: #26344d;
    --muted: #8290a5;
    --border: #e9edf4;
    --white: #ffffff;
}

* { box-sizing: border-box; margin: 0; padding: 0; }

body {
    font-family: 'Inter', sans-serif;
    background: var(--background);
    color: var(--text);
}

.layout { display: flex; min-height: 100vh; }

/* SIDEBAR */
.sidebar {
    width: 255px;
    background: var(--navy);
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
    display: flex;
    align-items: center;
    padding: 13px 14px;
    margin-bottom: 6px;
    border-radius: 9px;
    color: #bdc8db;
    text-decoration: none;
    font-size: 13px;
    font-weight: 500;
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
    margin-bottom: 32px;
}

.topbar h1 { font-size: 23px; font-weight: 800; }

.topbar p {
    font-size: 12px;
    color: var(--muted);
    margin-top: 6px;
}

.profile {
    display: flex;
    align-items: center;
    gap: 12px;
    background: white;
    padding: 9px 15px;
    border-radius: 12px;
    border: 1px solid var(--border);
}

.avatar {
    width: 38px;
    height: 38px;
    border-radius: 50%;
    background: #e7edff;
    color: var(--blue);
    display: grid;
    place-items: center;
    font-weight: 800;
}

.profile strong { font-size: 12px; display: block; }
.profile small { color: var(--muted); font-size: 10px; }

/* WELCOME */
.welcome {
    background: linear-gradient(115deg, #1c3154, #315efb);
    border-radius: 17px;
    padding: 30px 32px;
    color: white;
    margin-bottom: 28px;
    position: relative;
    overflow: hidden;
}

.welcome h2 { font-size: 23px; margin-bottom: 9px; }
.welcome p { font-size: 13px; color: #dce5ff; line-height: 1.7; }

/* STATS */
.section-title {
    font-size: 16px;
    font-weight: 800;
    margin-bottom: 17px;
}

.stats {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 17px;
    margin-bottom: 30px;
}

.stat-card {
    background: white;
    padding: 21px;
    border-radius: 14px;
    border: 1px solid var(--border);
    box-shadow: 0 4px 15px rgba(25,45,80,.03);
}

.stat-label { color: var(--muted); font-size: 11px; font-weight: 600; }
.stat-value { font-size: 27px; font-weight: 800; margin-top: 12px; color: var(--navy); }
.stat-note { font-size: 10px; color: var(--muted); margin-top: 7px; }

/* QUICK ACCESS */
.quick-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 17px;
    margin-bottom: 30px;
}

.quick-card {
    background: white;
    border: 1px solid var(--border);
    border-radius: 14px;
    padding: 23px;
    text-decoration: none;
    color: var(--text);
    transition: .2s;
}

.quick-card:hover {
    transform: translateY(-3px);
    box-shadow: 0 9px 25px rgba(25,45,80,.07);
}

.quick-icon {
    width: 43px;
    height: 43px;
    border-radius: 11px;
    background: #edf2ff;
    margin-bottom: 15px;
}

.quick-card h3 { font-size: 14px; margin-bottom: 7px; }
.quick-card p { color: var(--muted); font-size: 11px; line-height: 1.6; }
.quick-card span { display: block; margin-top: 15px; color: var(--blue); font-size: 11px; font-weight: 700; }

/* BOTTOM */
.bottom-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 18px;
}

.info-card {
    background: white;
    border: 1px solid var(--border);
    padding: 23px;
    border-radius: 14px;
}

.info-card h3 { font-size: 14px; margin-bottom: 12px; }
.info-card p { color: var(--muted); font-size: 12px; line-height: 1.8; }

@media(max-width: 1100px) {
    .stats { grid-template-columns: repeat(2, 1fr); }
    .quick-grid { grid-template-columns: repeat(2, 1fr); }
}

@media(max-width: 750px) {
    .sidebar { width: 75px; padding: 20px 8px; }
    .brand { font-size: 0; padding: 10px 5px 30px; }
    .brand span { font-size: 20px; }
    .menu-title { display: none; }
    .menu a { font-size: 0; justify-content: center; padding: 15px 5px; }
    .main { margin-left: 75px; width: calc(100% - 75px); padding: 20px 14px; }
    .stats, .quick-grid, .bottom-grid { grid-template-columns: 1fr; }
    .topbar { align-items: flex-start; gap: 12px; }
    .profile { padding: 7px; }
    .profile div:last-child { display: none; }
}
</style>
</head>

<body>

<div class="layout">

    <aside class="sidebar">
        <div class="brand">Gestion<span>Assurance</span></div>

        <div class="menu-title">MENU PRINCIPAL</div>

        <nav class="menu">
            <a href="${pageContext.request.contextPath}/agent/dashboard" class="active">Tableau de bord</a>
            <a href="${pageContext.request.contextPath}/agent/clients">Clients</a>
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
                <h1>Tableau de bord</h1>
                <p>Vue générale de votre activité</p>
            </div>

            <div class="profile">
                <div class="avatar">
                    <c:out value="${sessionScope.prenom.substring(0,1)}" default="A"/>
                </div>
                <div>
                    <strong>
                        <c:out value="${sessionScope.prenom}"/>
                        <c:out value="${sessionScope.nom}"/>
                    </strong>
                    <small>Agent</small>
                </div>
            </div>
        </header>

        <section class="welcome">
            <h2>Bienvenue, <c:out value="${sessionScope.prenom}" default="Agent"/> !</h2>
            <p>Retrouvez ici les informations essentielles de votre activité et accédez rapidement à vos outils de gestion.</p>
        </section>

        <h2 class="section-title">Vue d'ensemble</h2>

        <section class="stats">
            <div class="stat-card">
                <div class="stat-label">Clients</div>
                <div class="stat-value">${nombreClients}</div>
                <div class="stat-note">Clients enregistrés</div>
            </div>

            <div class="stat-card">
                <div class="stat-label">Contrats</div>
                <div class="stat-value">${nombreContrats}</div>
                <div class="stat-note">Contrats gérés</div>
            </div>

            <div class="stat-card">
                <div class="stat-label">Sinistres à traiter</div>
                <div class="stat-value">${nombreSinistres}</div>
                <div class="stat-note">Dossiers en attente</div>
            </div>

            <div class="stat-card">
                <div class="stat-label">Paiements</div>
                <div class="stat-value">${nombrePaiements}</div>
                <div class="stat-note">Paiements enregistrés</div>
            </div>
        </section>

        <h2 class="section-title">Accès rapide</h2>

        <section class="quick-grid">

            <a class="quick-card" href="${pageContext.request.contextPath}/agent/clients">
                <div class="quick-icon"></div>
                <h3>Gestion des clients</h3>
                <p>Consultez les informations et les dossiers des clients.</p>
                <span>Accéder aux clients →</span>
            </a>

            <a class="quick-card" href="${pageContext.request.contextPath}/agent/contrats">
                <div class="quick-icon"></div>
                <h3>Gestion des contrats</h3>
                <p>Créez, consultez et modifiez les contrats d'assurance.</p>
                <span>Accéder aux contrats →</span>
            </a>

            <a class="quick-card" href="${pageContext.request.contextPath}/agent/sinistres">
                <div class="quick-icon"></div>
                <h3>Gestion des sinistres</h3>
                <p>Examinez les déclarations et mettez à jour leur statut.</p>
                <span>Accéder aux sinistres →</span>
            </a>

            <a class="quick-card" href="${pageContext.request.contextPath}/agent/garanties">
                <div class="quick-icon"></div>
                <h3>Gestion des garanties</h3>
                <p>Consultez et gérez les garanties proposées.</p>
                <span>Accéder aux garanties →</span>
            </a>

            <a class="quick-card" href="${pageContext.request.contextPath}/agent/paiements">
                <div class="quick-icon"></div>
                <h3>Gestion des paiements</h3>
                <p>Enregistrez les paiements et consultez leur historique.</p>
                <span>Accéder aux paiements →</span>
            </a>

            <a class="quick-card" href="${pageContext.request.contextPath}/agent/statistiques">
                <div class="quick-icon"></div>
                <h3>Statistiques</h3>
                <p>Consultez les indicateurs de votre activité.</p>
                <span>Voir les statistiques →</span>
            </a>

        </section>

        <h2 class="section-title">Informations</h2>

        <section class="bottom-grid">
            <div class="info-card">
                <h3>Gestion de votre activité</h3>
                <p>Utilisez le menu pour accéder aux dossiers clients, contrats, sinistres, garanties et paiements.</p>
            </div>

            <div class="info-card">
                <h3>Suivi des dossiers</h3>
                <p>Veillez à mettre à jour les statuts des sinistres après leur analyse afin de garantir un suivi fiable.</p>
            </div>
        </section>

    </main>
</div>

</body>
</html>