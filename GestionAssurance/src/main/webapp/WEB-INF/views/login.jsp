<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - GestionAssurance</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/login.css">
</head>

<body>

<div class="login-container">

    <div class="login-box">

        <h1>GestionAssurrance</h1>
        <h2>Connexion</h2>

        <p class="subtitle">
            Accédez à votre espace personnel
        </p>

        <% if (request.getAttribute("erreur") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("erreur") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login"
              method="post">

            <div class="form-group">
                <label for="email">Adresse e-mail</label>
                <input type="email"
                       id="email"
                       name="email"
                       placeholder="Votre adresse e-mail"
                       required>
            </div>

            <div class="form-group">
                <label for="motDePasse">Mot de passe</label>
                <input type="password"
                       id="motDePasse"
                       name="motDePasse"
                       placeholder="Votre mot de passe"
                       required>
            </div>

            <button type="submit">Se connecter</button>

        </form>

        <p class="register-link">
            Vous n'avez pas de compte ?
            <a href="${pageContext.request.contextPath}/inscription">
                Créer un compte
            </a>
        </p>

        <a class="home-link"
           href="${pageContext.request.contextPath}/accueil">
            Retour à l'accueil
        </a>

    </div>

</div>

</body>
</html>