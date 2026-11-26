<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription - GestionAssurance</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inscription.css">
</head>
<body>

<div class="register-container">
    <div class="register-box">

        <h1>GestionAssurance</h1>
        <h2>Créer un compte client</h2>
        <p class="subtitle">Rejoignez notre espace client</p>

        <% if (request.getAttribute("erreur") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("erreur") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/inscription" method="post">

            <div class="form-group">
                <label for="nom">Nom *</label>
                <input type="text" id="nom" name="nom" required>
            </div>

            <div class="form-group">
                <label for="prenom">Prénom *</label>
                <input type="text" id="prenom" name="prenom" required>
            </div>

            <div class="form-group">
                <label for="email">Adresse e-mail *</label>
                <input type="email" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="telephone">Téléphone</label>
                <input type="tel" id="telephone" name="telephone">
            </div>

            <div class="form-group">
                <label for="motDePasse">Mot de passe *</label>
                <input type="password" id="motDePasse" name="motDePasse"
                       minlength="8" required>
            </div>

            <div class="form-group">
                <label for="confirmation">Confirmer le mot de passe *</label>
                <input type="password" id="confirmation" name="confirmation"
                       minlength="8" required>
            </div>

            <button type="submit">Créer mon compte</button>

        </form>

        <p class="login-link">
            Vous avez déjà un compte ?
            <a href="${pageContext.request.contextPath}/login">Se connecter</a>
        </p>

        <a class="home-link" href="${pageContext.request.contextPath}/accueil">
            Retour à l'accueil
        </a>

    </div>
</div>

</body>
</html>