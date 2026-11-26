<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Ajouter un utilisateur | GestionAssurance</title>

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
    padding: 35px 15px;
}

.container {
    max-width: 750px;
    margin: auto;
    background: white;
    padding: 35px;
    border-radius: 15px;
    box-shadow: 0 5px 25px rgba(20, 35, 63, .08);
}

h1 {
    color: #14233f;
    margin-bottom: 10px;
    font-size: 25px;
}

.subtitle {
    color: #8290a5;
    margin-bottom: 25px;
    font-size: 14px;
}

.form-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 18px;
}

.form-group {
    margin-bottom: 18px;
}

label {
    display: block;
    margin-bottom: 8px;
    font-size: 13px;
    font-weight: bold;
}

input, select {
    width: 100%;
    padding: 12px;
    border: 1px solid #dce2ed;
    border-radius: 8px;
    font-size: 14px;
    outline: none;
}

input:focus, select:focus {
    border-color: #315efb;
}

.section-role {
    display: none;
    background: #f7f9ff;
    padding: 20px;
    border-radius: 10px;
    margin: 10px 0 20px;
}

.section-role h3 {
    margin-bottom: 18px;
    color: #315efb;
    font-size: 15px;
}

.section-role.visible {
    display: block;
}

.error {
    background: #fff0f0;
    color: #bd3030;
    padding: 13px;
    border-radius: 8px;
    margin-bottom: 20px;
    font-size: 13px;
}

.buttons {
    display: flex;
    gap: 12px;
    margin-top: 25px;
}

button, .btn-retour {
    padding: 13px 20px;
    border-radius: 8px;
    border: none;
    font-size: 13px;
    font-weight: bold;
    cursor: pointer;
    text-decoration: none;
    text-align: center;
}

button {
    background: #315efb;
    color: white;
    flex: 1;
}

button:hover {
    background: #244bd4;
}

.btn-retour {
    background: #e9edf5;
    color: #26344d;
}

@media(max-width: 600px) {
    .form-grid {
        grid-template-columns: 1fr;
        gap: 0;
    }

    .container {
        padding: 22px;
    }
}
</style>
</head>

<body>

<div class="container">

    <h1>Ajouter un utilisateur</h1>
    <p class="subtitle">
        Créez un nouveau compte dans la plateforme GestionAssurance.
    </p>

    <c:if test="${not empty messageErreur}">
        <div class="error">
            <c:out value="${messageErreur}"/>
        </div>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/admin/ajouter-utilisateur">

        <div class="form-grid">

            <div class="form-group">
                <label for="nom">Nom *</label>
                <input type="text" id="nom" name="nom" required maxlength="100">
            </div>

            <div class="form-group">
                <label for="prenom">Prénom *</label>
                <input type="text" id="prenom" name="prenom" required maxlength="100">
            </div>

            <div class="form-group">
                <label for="email">Adresse email *</label>
                <input type="email" id="email" name="email" required maxlength="150">
            </div>

            <div class="form-group">
                <label for="telephone">Téléphone</label>
                <input type="tel" id="telephone" name="telephone" maxlength="20">
            </div>

            <div class="form-group">
                <label for="motDePasse">Mot de passe *</label>
                <input type="password" id="motDePasse"
                       name="motDePasse" required minlength="8" maxlength="72">
            </div>

            <div class="form-group">
                <label for="role">Rôle *</label>
                <select id="role" name="role" required onchange="afficherChampsRole()">
                    <option value="CLIENT">Client</option>
                    <option value="AGENT">Agent</option>
                    <option value="ADMIN">Administrateur</option>
                </select>
            </div>

        </div>

        <!-- Champs spécifiques au client -->
        <div id="champsClient" class="section-role visible">
            <h3>Informations du client</h3>

            <div class="form-group">
                <label for="adresse">Adresse</label>
                <input type="text" id="adresse" name="adresse" maxlength="255">
            </div>

            <div class="form-group">
                <label for="dateNaissance">Date de naissance</label>
                <input type="date" id="dateNaissance" name="dateNaissance">
            </div>
        </div>

        <!-- Champs spécifiques à l'agent -->
        <div id="champsAgent" class="section-role">
            <h3>Informations de l'agent</h3>

            <div class="form-group">
                <label for="matricule">Matricule *</label>
                <input type="text" id="matricule" name="matricule" maxlength="50">
            </div>

            <div class="form-group">
                <label for="service">Service *</label>
                <input type="text" id="service" name="service" maxlength="100">
            </div>
        </div>

        <div class="buttons">
            <a class="btn-retour"
               href="${pageContext.request.contextPath}/admin/utilisateurs">
                Retour
            </a>

            <button type="submit">Créer l'utilisateur</button>
        </div>

    </form>

</div>

<script>
function afficherChampsRole() {

    const role = document.getElementById("role").value;

    const client = document.getElementById("champsClient");
    const agent = document.getElementById("champsAgent");
    const admin = document.getElementById("champsAdmin");

    const matricule = document.getElementById("matricule");
    const service = document.getElementById("service");

    client.classList.toggle("visible", role === "CLIENT");
    agent.classList.toggle("visible", role === "AGENT");
    admin.classList.toggle("visible", role === "ADMIN");

    matricule.required = role === "AGENT";
    service.required = role === "AGENT";
    niveauAcces.required = role === "ADMIN";
}

afficherChampsRole();
</script>

</body>
</html>