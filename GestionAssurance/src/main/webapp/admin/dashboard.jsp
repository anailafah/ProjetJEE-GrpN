<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Administration - AssurPlus</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f7fb;
        }

        header {
            background: #142d4e;
            color: white;
            padding: 25px 40px;
        }

        main {
            padding: 40px;
        }

        .cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }

        .card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.08);
        }

        .card a {
            color: #13a99a;
            text-decoration: none;
            font-weight: bold;
        }

        .logout {
            color: white;
            float: right;
            text-decoration: none;
        }
    </style>
</head>

<body>

<header>
    <a class="logout"
       href="${pageContext.request.contextPath}/logout">
       Déconnexion
    </a>

    <h1>Administration AssurPlus</h1>
    <p>
        Bienvenue
        ${sessionScope.prenom}
        ${sessionScope.nom}
    </p>
</header>

<main>

    <h2>Tableau de bord administrateur</h2>
    <p>Gérez les différentes fonctionnalités de votre application.</p>

    <div class="cards">

        <div class="card">
            <h3>Gestion des utilisateurs</h3>
            <a href="${pageContext.request.contextPath}/admin/utilisateurs">
                Accéder →
            </a>
        </div>

        <div class="card">
            <h3>Types d'assurance</h3>
            <a href="${pageContext.request.contextPath}/admin/types-assurance">
                Accéder →
            </a>
        </div>

        <div class="card">
            <h3>Gestion des garanties</h3>
            <a href="${pageContext.request.contextPath}/admin/garanties">
                Accéder →
            </a>
        </div>

        <div class="card">
            <h3>Gestion des contrats</h3>
            <a href="${pageContext.request.contextPath}/admin/contrats">
                Accéder →
            </a>
        </div>

    </div>

</main>

</body>
</html>