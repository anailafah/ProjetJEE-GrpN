<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Administration - GestionAssurance</title>

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

        .layout {
            display: flex;
            min-height: 100vh;
        }

        /* BARRE LATERALE */
        .sidebar {
            width: 250px;
            background: #142d4e;
            color: white;
            padding: 28px 18px;
            display: flex;
            flex-direction: column;
            flex-shrink: 0;
        }

        .logo {
            font-size: 21px;
            font-weight: bold;
            padding: 0 12px 35px;
            border-bottom: 1px solid rgba(255,255,255,0.15);
        }

        .logo span {
            color: #45c3b1;
        }

        .menu-title {
            color: #9eafc4;
            font-size: 11px;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin: 30px 12px 12px;
        }

        .menu a {
            display: block;
            color: #dce5f0;
            text-decoration: none;
            padding: 13px 12px;
            border-radius: 8px;
            margin-bottom: 5px;
            font-size: 14px;
            transition: 0.2s;
        }

        .menu a:hover,
        .menu a.active {
            background: #24466e;
            color: white;
        }

        .sidebar-bottom {
            margin-top: auto;
            padding-top: 25px;
            border-top: 1px solid rgba(255,255,255,0.15);
        }

        .sidebar-bottom a {
            display: block;
            color: #ffcccc;
            text-decoration: none;
            padding: 12px;
            font-size: 14px;
        }

        /* CONTENU PRINCIPAL */
        .main-content {
            flex: 1;
            min-width: 0;
        }

        .topbar {
            height: 75px;
            background: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 35px;
            border-bottom: 1px solid #e8edf3;
        }

        .topbar h2 {
            font-size: 19px;
            color: #142d4e;
        }

        .admin-profile {
            font-size: 14px;
            color: #526176;
        }

        .admin-profile strong {
            color: #142d4e;
        }

        .content {
            padding: 35px;
        }

        .welcome {
            margin-bottom: 30px;
        }

        .welcome h1 {
            font-size: 27px;
            color: #142d4e;
            margin-bottom: 9px;
        }

        .welcome p {
            color: #78869a;
            font-size: 14px;
        }

        /* TITRES */
        .section-title {
            font-size: 18px;
            color: #142d4e;
            margin-bottom: 18px;
        }

        /* INDICATEURS */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 18px;
            margin-bottom: 35px;
        }

        .stat-card {
            background: white;
            padding: 23px;
            border-radius: 12px;
            box-shadow: 0 3px 12px rgba(25,45,75,0.05);
            border: 1px solid #edf0f5;
        }

        .stat-card p {
            color: #78869a;
            font-size: 13px;
            margin-bottom: 14px;
        }

        .stat-card h3 {
            color: #142d4e;
            font-size: 27px;
        }

        .stat-note {
            font-size: 12px;
            color: #9aa5b4;
            margin-top: 10px;
        }

        .stat-icon {
            width: 38px;
            height: 38px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 9px;
            background: #eaf2fc;
            color: #2864a0;
            font-size: 18px;
            margin-bottom: 16px;
        }

        /* GRAPHIQUES */
        .charts-section {
            margin-bottom: 35px;
        }

        .charts-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 20px;
        }

        .chart-card {
            background: white;
            padding: 24px;
            border-radius: 12px;
            border: 1px solid #edf0f5;
            box-shadow: 0 3px 12px rgba(25,45,75,0.05);
            min-width: 0;
        }

        .chart-card h3 {
            color: #142d4e;
            font-size: 16px;
            margin-bottom: 20px;
        }

        .chart-container {
            position: relative;
            height: 280px;
        }

        /* GESTION */
        .management-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 18px;
        }

        .management-card {
            background: white;
            padding: 24px;
            border-radius: 12px;
            border: 1px solid #edf0f5;
            box-shadow: 0 3px 12px rgba(25,45,75,0.05);
            transition: 0.2s;
        }

        .management-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 7px 20px rgba(25,45,75,0.09);
        }

        .management-card h3 {
            color: #142d4e;
            font-size: 16px;
            margin-bottom: 10px;
        }

        .management-card p {
            color: #8490a0;
            font-size: 13px;
            line-height: 1.6;
            min-height: 42px;
            margin-bottom: 17px;
        }

        .management-card a {
            color: #168f91;
            text-decoration: none;
            font-weight: bold;
            font-size: 13px;
        }

        .management-card a:hover {
            text-decoration: underline;
        }

        .footer {
            text-align: center;
            padding: 25px;
            color: #9aa5b4;
            font-size: 12px;
        }

        /* RESPONSIVE */
        @media (max-width: 1000px) {
            .stats-grid,
            .management-grid,
            .charts-grid {
                grid-template-columns: repeat(2, minmax(0, 1fr));
            }
        }

        @media (max-width: 650px) {
            .layout {
                flex-direction: column;
            }

            .sidebar {
                width: 100%;
            }

            .sidebar-bottom {
                margin-top: 10px;
            }

            .topbar {
                padding: 0 18px;
            }

            .content {
                padding: 22px 16px;
            }

            .stats-grid,
            .management-grid,
            .charts-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>

<body>

<div class="layout">

    <!-- MENU LATERAL -->
    <aside class="sidebar">

        <div class="logo">
            Gestion<span>Assurance</span>
        </div>

        <div class="menu-title">Menu principal</div>

        <nav class="menu">

            <a href="${pageContext.request.contextPath}/admin/dashboard"
               class="active">
                Tableau de bord
            </a>

            <a href="${pageContext.request.contextPath}/admin/utilisateurs">
                Utilisateurs
            </a>

            <a href="${pageContext.request.contextPath}/admin/types-assurance">
                Types d'assurance
            </a>

            <a href="${pageContext.request.contextPath}/admin/garanties">
                Garanties
            </a>

            <a href="${pageContext.request.contextPath}/admin/contrats">
                Contrats
            </a>

            <a href="${pageContext.request.contextPath}/admin/sinistres">
                Sinistres
            </a>

        </nav>

        <div class="sidebar-bottom">
            <a href="${pageContext.request.contextPath}/logout">
                Déconnexion
            </a>
        </div>

    </aside>

    <!-- CONTENU PRINCIPAL -->
    <div class="main-content">

        <header class="topbar">

            <h2>Tableau de bord</h2>

            <div class="admin-profile">
                Administrateur :
                <strong>${sessionScope.prenom} ${sessionScope.nom}</strong>
            </div>

        </header>

        <main class="content">

            <!-- BIENVENUE -->
            <section class="welcome">

                <h1>Bienvenue dans votre espace administrateur</h1>

                <p>
                    Gérez les utilisateurs, les assurances, les contrats
                    et suivez l'activité de votre application.
                </p>

            </section>

            <!-- INDICATEURS -->
            <h2 class="section-title">Vue d'ensemble</h2>

            <section class="stats-grid">

                <div class="stat-card">
                    <div class="stat-icon">U</div>
                    <p>Nombre de clients</p>
                    <h3>${empty statistiques ? '—' : statistiques.nombreClients}</h3>
                    <div class="stat-note">Clients enregistrés</div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">C</div>
                    <p>Nombre de contrats</p>
                    <h3>${empty statistiques ? '—' : statistiques.nombreContrats}</h3>
                    <div class="stat-note">Contrats enregistrés</div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">A</div>
                    <p>Contrats actifs</p>
                    <h3>${empty statistiques ? '—' : statistiques.contratsActifs}</h3>
                    <div class="stat-note">Contrats actuellement actifs</div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">S</div>
                    <p>Sinistres en attente</p>
                    <h3>${empty statistiques ? '—' : statistiques.sinistresEnAttente}</h3>
                    <div class="stat-note">Demandes à traiter</div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">P</div>
                    <p>Nombre de paiements</p>
                    <h3>${empty statistiques ? '—' : statistiques.nombrePaiements}</h3>
                    <div class="stat-note">Paiements enregistrés</div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">€</div>
                    <p>Montant total des paiements</p>
                    <h3>
                        ${empty statistiques ? '—' : statistiques.montantTotalPaiements}
                    </h3>
                    <div class="stat-note">Montant cumulé</div>
                </div>

            </section>

            <!-- GRAPHIQUES STATISTIQUES -->
            <section class="charts-section">

                <h2 class="section-title">Statistiques et analyses</h2>

                <div class="charts-grid">

                    <!-- CONTRATS PAR TYPE -->
                    <div class="chart-card">

                        <h3>Contrats par type d'assurance</h3>

                        <div class="chart-container">
                            <canvas id="contratsParType"></canvas>
                        </div>

                        <div id="dataTypes" hidden>
                            <c:forEach var="item"
                                       items="${statistiques.contratsParType}">
                                <span
                                    data-label="${item.key}"
                                    data-value="${item.value}">
                                </span>
                            </c:forEach>
                        </div>

                    </div>

                    <!-- EVOLUTION DES CONTRATS -->
                    <div class="chart-card">

                        <h3>Évolution des contrats (6 derniers mois)</h3>

                        <div class="chart-container">
                            <canvas id="contratsParMois"></canvas>
                        </div>

                        <div id="dataMois" hidden>
                            <c:forEach var="item"
                                       items="${statistiques.contratsParMois}">
                                <span
                                    data-label="${item.key}"
                                    data-value="${item.value}">
                                </span>
                            </c:forEach>
                        </div>

                    </div>

                    <!-- SINISTRES PAR STATUT -->
                    <div class="chart-card">

                        <h3>Répartition des sinistres par statut</h3>

                        <div class="chart-container">
                            <canvas id="sinistresParStatut"></canvas>
                        </div>

                        <div id="dataSinistres" hidden>
                            <c:forEach var="item"
                                       items="${statistiques.sinistresParStatut}">
                                <span
                                    data-label="${item.key}"
                                    data-value="${item.value}">
                                </span>
                            </c:forEach>
                        </div>

                    </div>

                </div>

            </section>

            <!-- GESTION DE L'APPLICATION -->
            <h2 class="section-title">Gestion de l'application</h2>

            <section class="management-grid">

                <div class="management-card">
                    <h3>Gestion des utilisateurs</h3>
                    <p>
                        Consultez et gérez les comptes clients,
                        agents et administrateurs.
                    </p>
                    <a href="${pageContext.request.contextPath}/admin/utilisateurs">
                        Accéder à la gestion →
                    </a>
                </div>

                <div class="management-card">
                    <h3>Types d'assurance</h3>
                    <p>
                        Gérez les différentes catégories de produits d'assurance.
                    </p>
                    <a href="${pageContext.request.contextPath}/admin/types-assurance">
                        Accéder à la gestion →
                    </a>
                </div>

                <div class="management-card">
                    <h3>Garanties</h3>
                    <p>
                        Ajoutez, modifiez et consultez les garanties proposées.
                    </p>
                    <a href="${pageContext.request.contextPath}/admin/garanties">
                        Accéder à la gestion →
                    </a>
                </div>

                <div class="management-card">
                    <h3>Contrats</h3>
                    <p>
                        Consultez et suivez les contrats d'assurance enregistrés.
                    </p>
                    <a href="${pageContext.request.contextPath}/admin/contrats">
                        Accéder à la gestion →
                    </a>
                </div>

                <div class="management-card">
                    <h3>Sinistres</h3>
                    <p>
                        Consultez les déclarations et suivez leur traitement.
                    </p>
                    <a href="${pageContext.request.contextPath}/admin/sinistres">
                        Accéder à la gestion →
                    </a>
                </div>

            </section>

        </main>

        <footer class="footer">
            GestionAssurance — Espace d'administration
        </footer>

    </div>

</div>

<!-- BIBLIOTHEQUE CHART.JS -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>

    // Récupération des données transmises par le DAO
    function lireDonnees(id) {

        const elements = document.querySelectorAll("#" + id + " span");

        return {
            labels: Array.from(elements).map(e => e.dataset.label),
            values: Array.from(elements).map(e => Number(e.dataset.value))
        };
    }

    // GRAPHIQUE 1 : CONTRATS PAR TYPE
    const types = lireDonnees("dataTypes");

    new Chart(document.getElementById("contratsParType"), {

        type: "doughnut",

        data: {
            labels: types.labels,

            datasets: [{
                data: types.values,
                backgroundColor: [
                    "#2563eb",
                    "#14b8a6",
                    "#f59e0b",
                    "#8b5cf6",
                    "#ef4444",
                    "#64748b"
                ]
            }]
        },

        options: {
            responsive: true,
            maintainAspectRatio: false
        }

    });

    // GRAPHIQUE 2 : EVOLUTION DES CONTRATS
    const mois = lireDonnees("dataMois");

    new Chart(document.getElementById("contratsParMois"), {

        type: "line",

        data: {
            labels: mois.labels,

            datasets: [{
                label: "Nombre de contrats",
                data: mois.values,
                borderColor: "#2563eb",
                backgroundColor: "rgba(37, 99, 235, 0.12)",
                fill: true,
                tension: 0.35
            }]
        },

        options: {
            responsive: true,
            maintainAspectRatio: false,

            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        precision: 0
                    }
                }
            }
        }

    });

    // GRAPHIQUE 3 : SINISTRES PAR STATUT
    const sinistres = lireDonnees("dataSinistres");

    new Chart(document.getElementById("sinistresParStatut"), {

        type: "bar",

        data: {
            labels: sinistres.labels,

            datasets: [{
                label: "Nombre de sinistres",
                data: sinistres.values,
                backgroundColor: "#14b8a6",
                borderRadius: 6
            }]
        },

        options: {
            responsive: true,
            maintainAspectRatio: false,

            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        precision: 0
                    }
                }
            }
        }

    });

</script>

</body>
</html>