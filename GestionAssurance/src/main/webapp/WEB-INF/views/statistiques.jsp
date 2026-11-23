<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Statistiques - Gestion Assurance</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/statistiques.css">
</head>
<body>

    <h1>Statistiques de Gestion Assurance</h1>
    <p>Vue globale des données de l'application.</p>

    <div class="dashboard">

        <div class="card">
            <h3>Nombre d'utilisateurs</h3>
            <p>${stats.nombreUtilisateurs}</p>
        </div>

        <div class="card">
            <h3>Nombre de contrats</h3>
            <p>${stats.nombreContrats}</p>
        </div>

        <div class="card">
            <h3>Nombre de sinistres</h3>
            <p>${stats.nombreSinistres}</p>
        </div>

        <div class="card">
            <h3>Nombre de paiements</h3>
            <p>${stats.nombrePaiements}</p>
        </div>

        <div class="card">
            <h3>Échéances impayées</h3>
            <p>${stats.echeancesImpayees}</p>
        </div>

    </div>

</body>
</html>