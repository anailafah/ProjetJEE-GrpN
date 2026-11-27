<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GestionAssurance | Ajouter un utilisateur</title>
<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: Arial, sans-serif; background: #f4f6fb; color: #26344d; padding: 30px 38px; }
h1 { font-size: 25px; color: #14233f; margin-bottom: 8px; }
.subtitle { color: #8290a5; font-size: 12px; margin-bottom: 20px; }
.back { display: inline-block; margin-bottom: 22px; color: #315efb; text-decoration: none; font-size: 12px; font-weight: bold; }
.card { background: white; border: 1px solid #e9edf4; border-radius: 14px; padding: 24px; max-width: 620px; }
label { display: block; font-size: 12px; font-weight: bold; margin: 14px 0 6px; }
input, select { width: 100%; padding: 11px 13px; border: 1px solid #e1e6ef; border-radius: 8px; font-size: 13px; }
input:focus, select:focus { border-color: #315efb; outline: none; }
.row { display: flex; gap: 14px; }
.row > div { flex: 1; }
.groupe { display: none; border-top: 1px solid #edf0f5; margin-top: 18px; padding-top: 4px; }
.groupe.visible { display: block; }
.btn { margin-top: 22px; border: none; background: #315efb; color: white; padding: 12px 20px; border-radius: 8px; font-weight: bold; cursor: pointer; }
.btn:hover { background: #244bd1; }
.message { padding: 13px 16px; border-radius: 8px; margin-bottom: 18px; font-size: 13px; }
.error { background: #ffe8e8; color: #c03939; }
.hint { color: #8290a5; font-size: 11px; margin-top: 4px; }
</style>
</head>
<body>

<a class="back" href="${pageContext.request.contextPath}/admin/utilisateurs">← Retour à la liste des utilisateurs</a>

<h1>Ajouter un utilisateur</h1>
<p class="subtitle">Créer un compte client, agent ou administrateur.</p>

<section class="card">

    <c:if test="${not empty erreur}">
        <div class="message error"><c:out value="${erreur}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/admin/ajouter-utilisateur">
        <input type="hidden" name="csrf" value="${csrfToken}">

        <label for="role">Rôle</label>
        <select id="role" name="role" onchange="afficherGroupe()">
            <option value="">-- Choisir --</option>
            <option value="CLIENT" ${param.role == 'CLIENT' ? 'selected' : ''}>Client</option>
            <option value="AGENT" ${param.role == 'AGENT' ? 'selected' : ''}>Agent</option>
            <option value="ADMIN" ${param.role == 'ADMIN' ? 'selected' : ''}>Administrateur</option>
        </select>

        <div class="row">
            <div>
                <label for="nom">Nom</label>
                <input type="text" id="nom" name="nom" value="<c:out value='${param.nom}'/>" required>
            </div>
            <div>
                <label for="prenom">Prénom</label>
                <input type="text" id="prenom" name="prenom" value="<c:out value='${param.prenom}'/>" required>
            </div>
        </div>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<c:out value='${param.email}'/>" required>

        <label for="motDePasse">Mot de passe</label>
        <input type="password" id="motDePasse" name="motDePasse" required>
        <p class="hint">8 caractères minimum, avec au moins une lettre et un chiffre.</p>

        <label for="telephone">Téléphone (facultatif)</label>
        <input type="text" id="telephone" name="telephone" value="<c:out value='${param.telephone}'/>">

        <!-- Champs spécifiques au CLIENT -->
        <div id="groupe-CLIENT" class="groupe">
            <label for="adresse">Adresse (facultatif)</label>
            <input type="text" id="adresse" name="adresse" value="<c:out value='${param.adresse}'/>">

            <label for="dateNaissance">Date de naissance (facultatif)</label>
            <input type="date" id="dateNaissance" name="dateNaissance" value="<c:out value='${param.dateNaissance}'/>">
        </div>

        <!-- Champs spécifiques à l'AGENT -->
        <div id="groupe-AGENT" class="groupe">
            <label for="matricule">Matricule</label>
            <input type="text" id="matricule" name="matricule" value="<c:out value='${param.matricule}'/>">

            <label for="serviceAgent">Service</label>
            <input type="text" id="serviceAgent" name="serviceAgent" value="<c:out value='${param.serviceAgent}'/>">
        </div>

        <!-- Champs spécifiques à l'ADMINISTRATEUR -->
        <div id="groupe-ADMIN" class="groupe">
            <label for="niveauAcces">Niveau d'accès</label>
            <input type="text" id="niveauAcces" name="niveauAcces"
                   value="<c:out value='${empty param.niveauAcces ? "TOTAL" : param.niveauAcces}'/>">
        </div>

        <button type="submit" class="btn">Créer le compte</button>
    </form>
</section>

<script>
function afficherGroupe() {
    var role = document.getElementById('role').value;
    ['CLIENT', 'AGENT', 'ADMIN'].forEach(function (r) {
        document.getElementById('groupe-' + r).classList.toggle('visible', r === role);
    });
}
afficherGroupe();
</script>

</body>
</html>