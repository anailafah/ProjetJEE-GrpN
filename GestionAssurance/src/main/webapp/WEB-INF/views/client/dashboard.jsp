<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Espace client - AssurPlus</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/client-dashboard.css">
</head>
<body>

<header class="header">
    <h1>GestionAssurance</h1>
    <div>
        <span>Bonjour, ${sessionScope.prenom} ${sessionScope.nom}</span>
        <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
    </div>
</header>

<main class="container">
    <h2>Mon espace client</h2>
    <p class="intro">Bienvenue dans votre espace personnel AssurPlus.</p>

    <div class="cards">
        <div class="card">
            <h3>Mes contrats</h3>
            <p>Consultez vos contrats d'assurance.</p>
            <span class="status">Bientôt disponible</span>
        </div>

        <div class="card">
            <h3>Mes garanties</h3>
            <p>Découvrez les garanties associées à vos contrats.</p>
            <span class="status">Bientôt disponible</span>
        </div>

        <div class="card">
            <h3>Mes sinistres</h3>
            <p>Déclarez un sinistre et suivez son traitement.</p>
            <span class="status">Bientôt disponible</span>
        </div>

        <div class="card">
            <h3>Mes paiements</h3>
            <p>Consultez vos paiements et échéances.</p>
            <span class="status">Bientôt disponible</span>
        </div>

        <div class="card">
            <h3>Assistant IA</h3>
            <p>Posez vos questions à notre assistant.</p>
            <a href="${pageContext.request.contextPath}/assistant-ia">
                Accéder à l'assistant
            </a>
        </div>

        <div class="card">
            <h3>Mon profil</h3>
            <p>Consultez et gérez vos informations personnelles.</p>
            <span class="status">Bientôt disponible</span>
        </div>
    </div>
</main>

</body>
</html>>