<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Tableau de bord - Gestion Assurance</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>

<body>

	<h1>Tableau de bord</h1>
	<p>Bienvenue dans votre espace d'administration.</p>

<div class="dashboard">

    <div class="card">
        <h3>Utilisateurs</h3>
        <p>${stats.nombreUtilisateurs}</p>
    </div>

    <div class="card">
        <h3>Contrats</h3>
        <p>${stats.nombreContrats}</p>
    </div>

    <div class="card">
        <h3>Sinistres</h3>
        <p>${stats.nombreSinistres}</p>
    </div>

    <div class="card">
        <h3>Paiements</h3>
        <p>${stats.nombrePaiements}</p>
    </div>

    <div class="card">
        <h3>Échéances impayées</h3>
        <p>${stats.echeancesImpayees}</p>
    </div>

</div>

</body>
</html>