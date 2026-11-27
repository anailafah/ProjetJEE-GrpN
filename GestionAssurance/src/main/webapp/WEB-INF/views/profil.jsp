<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GestionAssurance | Mon profil</title>
<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: Arial, sans-serif; background: #f4f6fb; color: #26344d; padding: 30px 38px; }
h1 { font-size: 25px; color: #14233f; margin-bottom: 8px; }
h2 { font-size: 17px; margin-bottom: 4px; }
.subtitle { color: #8290a5; font-size: 12px; margin-bottom: 20px; }
.back { display: inline-block; margin-bottom: 22px; color: #315efb; text-decoration: none; font-size: 12px; font-weight: bold; }
.card { background: white; border: 1px solid #e9edf4; border-radius: 14px; padding: 24px; max-width: 620px; margin-bottom: 22px; }
label { display: block; font-size: 12px; font-weight: bold; margin: 14px 0 6px; }
input { width: 100%; padding: 11px 13px; border: 1px solid #e1e6ef; border-radius: 8px; font-size: 13px; }
input:focus { border-color: #315efb; outline: none; }
input[readonly] { background: #f4f6fb; color: #8290a5; }
.row { display: flex; gap: 14px; }
.row > div { flex: 1; }
.btn { margin-top: 22px; border: none; background: #315efb; color: white; padding: 12px 20px; border-radius: 8px; font-weight: bold; cursor: pointer; }
.btn:hover { background: #244bd1; }
.message { padding: 13px 16px; border-radius: 8px; margin-bottom: 18px; font-size: 13px; max-width: 620px; }
.success { background: #e5f8ee; color: #16834b; }
.error { background: #ffe8e8; color: #c03939; }
.hint { color: #8290a5; font-size: 11px; margin-top: 4px; }
</style>
</head>
<body>

<a class="back" href="${pageContext.request.contextPath}${urlDashboard}">← Retour au tableau de bord</a>

<h1>Mon profil</h1>
<p class="subtitle">Rôle : <strong><c:out value="${roleLibelle}"/></strong></p>

<c:if test="${not empty succes}">
    <div class="message success"><c:out value="${succes}"/></div>
</c:if>
<c:if test="${not empty erreur}">
    <div class="message error"><c:out value="${erreur}"/></div>
</c:if>

<section class="card">
    <h2>Mes informations</h2>
    <form method="post" action="${pageContext.request.contextPath}/profil">
        <input type="hidden" name="csrf" value="${csrfToken}">
        <input type="hidden" name="action" value="infos">

        <div class="row">
            <div>
                <label for="nom">Nom</label>
                <input type="text" id="nom" name="nom" value="<c:out value='${utilisateur.nom}'/>" required>
            </div>
            <div>
                <label for="prenom">Prénom</label>
                <input type="text" id="prenom" name="prenom" value="<c:out value='${utilisateur.prenom}'/>" required>
            </div>
        </div>

        <label for="email">Email</label>
        <input type="email" id="email" value="<c:out value='${utilisateur.email}'/>" readonly>
        <p class="hint">L'email sert d'identifiant de connexion, il ne peut pas être modifié ici.</p>

        <label for="telephone">Téléphone</label>
        <input type="text" id="telephone" name="telephone" value="<c:out value='${utilisateur.telephone}'/>">

        <button type="submit" class="btn">Enregistrer</button>
    </form>
</section>

<section class="card">
    <h2>Changer mon mot de passe</h2>
    <form method="post" action="${pageContext.request.contextPath}/profil">
        <input type="hidden" name="csrf" value="${csrfToken}">
        <input type="hidden" name="action" value="motdepasse">

        <label for="ancienMotDePasse">Mot de passe actuel</label>
        <input type="password" id="ancienMotDePasse" name="ancienMotDePasse" required>

        <label for="nouveauMotDePasse">Nouveau mot de passe</label>
        <input type="password" id="nouveauMotDePasse" name="nouveauMotDePasse" required>
        <p class="hint">8 caractères minimum, avec au moins une lettre et un chiffre.</p>

        <label for="confirmation">Confirmer le nouveau mot de passe</label>
        <input type="password" id="confirmation" name="confirmation" required>

        <button type="submit" class="btn">Changer le mot de passe</button>
    </form>
</section>

</body>
</html>