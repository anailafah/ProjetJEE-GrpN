<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GestionAssurance | Enregistrer un paiement</title>

<style>

* { box-sizing: border-box; margin: 0; padding: 0; }

body {
    font-family: Arial, sans-serif;
    background: #f4f6fb;
    color: #26344d;
}

.sidebar {
    position: fixed;
    width: 255px;
    height: 100vh;
    background: #14233f;
    padding: 28px 18px;
    color: white;
}

.brand {
    font-size: 21px;
    font-weight: bold;
    padding: 0 12px 35px;
}

.brand span { color: #75a0ff; }

.menu-title {
    color: #8998b3;
    font-size: 11px;
    padding: 0 12px;
    margin-bottom: 15px;
}

.menu a {
    display: block;
    padding: 13px;
    margin-bottom: 6px;
    border-radius: 8px;
    color: #bdc8db;
    text-decoration: none;
    font-size: 13px;
}

.menu a:hover, .menu a.active {
    background: #263d63;
    color: white;
}

.menu a.active { border-left: 3px solid #75a0ff; }

.sidebar-bottom {
    position: absolute;
    bottom: 25px;
    left: 18px;
    right: 18px;
}

.logout { color: #ffb1b1 !important; }

.main {
    margin-left: 255px;
    padding: 30px 38px;
}

.topbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 25px;
}

h1 { font-size: 25px; }

.subtitle {
    color: #8290a5;
    font-size: 13px;
    margin-top: 7px;
}

.profile {
    background: white;
    padding: 12px 18px;
    border-radius: 10px;
    font-size: 13px;
    font-weight: bold;
}

.back {
    display: inline-block;
    color: #315efb;
    text-decoration: none;
    font-size: 13px;
    font-weight: bold;
    margin-bottom: 22px;
}

.banner {
    background: linear-gradient(115deg, #1c3154, #315efb);
    color: white;
    padding: 25px;
    border-radius: 14px;
    margin-bottom: 25px;
}

.banner h2 { font-size: 19px; margin-bottom: 8px; }
.banner p { font-size: 13px; color: #dce5ff; }

.card {
    background: white;
    border-radius: 14px;
    padding: 25px;
    border: 1px solid #e9edf4;
}

.card h2 {
    font-size: 18px;
    margin-bottom: 22px;
}

.table-container { overflow-x: auto; }

table {
    width: 100%;
    border-collapse: collapse;
    min-width: 850px;
}

th {
    background: #f8f9fc;
    color: #8290a5;
    text-align: left;
    padding: 15px 12px;
    font-size: 11px;
    text-transform: uppercase;
}

td {
    padding: 16px 12px;
    border-bottom: 1px solid #edf0f5;
    font-size: 12px;
}

td strong { color: #14233f; }

.badge {
    display: inline-block;
    padding: 6px 10px;
    border-radius: 20px;
    font-size: 10px;
    font-weight: bold;
    background: #edf2ff;
    color: #315efb;
}

.badge-actif { background: #e5f8ee; color: #16834b; }
.badge-suspendu { background: #fff2dc; color: #a56800; }
.badge-expire, .badge-resilie { background: #ffe8e8; color: #c03939; }
.badge-en_attente { background: #edf2ff; color: #315efb; }

.empty {
    text-align: center;
    padding: 45px 15px;
    color: #8290a5;
    font-size: 13px;
}

.empty h3 {
    color: #26344d;
    margin-bottom: 8px;
}

@media(max-width: 750px) {
    .sidebar { width: 75px; padding: 20px 8px; }
    .brand { font-size: 0; padding: 10px 5px 30px; }
    .brand span { font-size: 20px; }
    .menu-title { display: none; }
    .menu a { font-size: 0; text-align: center; }
    .main { margin-left: 75px; padding: 20px 14px; }
}

.btn-ajouter {
        display: inline-block;
        background: #1769aa;
        color: white;
        padding: 12px 20px;
        border: none;
        border-radius: 7px;
        text-decoration: none;
        font-weight: bold;
        cursor: pointer;
        margin-bottom: 20px;
        transition: 0.3s;
}

    .btn-ajouter:hover {
        background: #104d80;
        transform: translateY(-2px);
    }
    
    .btn-modifier {
    display: inline-block;
    padding: 8px 13px;
    background: #f0a52b;
    color: white;
    border-radius: 6px;
    text-decoration: none;
    font-size: 12px;
    font-weight: bold;
    transition: 0.3s;
}

.btn-modifier:hover {
    background: #d58b12;
}

/* ---- Ajouts Paiements / Échéances ---- */
.onglets { display: flex; gap: 8px; margin-bottom: 22px; }
.onglets a {
    padding: 10px 18px; border-radius: 8px; text-decoration: none;
    font-size: 13px; font-weight: bold; background: #e9edf4; color: #26344d;
}
.onglets a.actif { background: #315efb; color: white; }

.alerte { padding: 12px 16px; border-radius: 8px; margin-bottom: 18px; font-size: 13px; }
.alerte-succes { background: #e5f8ee; color: #16834b; }
.alerte-erreur { background: #ffe8e8; color: #c03939; }

.filtres { display: flex; gap: 10px; margin-bottom: 20px; align-items: center; flex-wrap: wrap; }
.filtres select, .filtres input {
    padding: 12px; border: 1px solid #ddd; border-radius: 7px; font-size: 13px;
}
.filtres input { flex: 1; min-width: 160px; }

.btn {
    display: inline-block; padding: 10px 16px; border: none; border-radius: 7px;
    font-size: 13px; font-weight: bold; text-decoration: none; cursor: pointer;
}
.btn-bleu { background: #1769aa; color: white; }
.btn-bleu:hover { background: #104d80; }
.btn-gris { background: #e5e7eb; color: #26344d; }
.btn-vert { background: #28a745; color: white; }
.btn-petit { padding: 7px 12px; font-size: 12px; }

.badge-effectue, .badge-payee { background: #e5f8ee; color: #16834b; }
.badge-en_retard { background: #ffe8e8; color: #c03939; }

.resume { color: #8290a5; font-size: 13px; margin-bottom: 15px; }
.resume strong { color: #14233f; }

.formulaire { max-width: 520px; }
.formulaire label { display: block; font-size: 13px; font-weight: bold; margin: 16px 0 6px; }
.formulaire input, .formulaire select {
    width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 7px; font-size: 13px;
}
.formulaire .choix { display: flex; gap: 18px; font-size: 13px; }
.formulaire .choix label { display: flex; align-items: center; gap: 6px; font-weight: normal; margin: 0; }
.formulaire .choix input { width: auto; }
.formulaire .actions { display: flex; gap: 10px; margin-top: 24px; }

.infos { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-bottom: 10px; }
.infos div { background: #f8f9fc; border-radius: 10px; padding: 14px; font-size: 12px; color: #8290a5; }
.infos strong { display: block; font-size: 16px; color: #14233f; margin-top: 4px; }
</style>
</head>
<body>

<aside class="sidebar">
    <div class="brand">Gestion<span>Assurance</span></div>

    <div class="menu-title">MENU PRINCIPAL</div>

    <nav class="menu">
        <a href="${pageContext.request.contextPath}/agent/dashboard">Tableau de bord</a>
        <a href="${pageContext.request.contextPath}/agent/clients">Clients</a>
        <a href="${pageContext.request.contextPath}/agent/contrats">Contrats</a>
        <a href="${pageContext.request.contextPath}/agent/sinistres">Sinistres</a>
        <a href="${pageContext.request.contextPath}/agent/garanties">Garanties</a>
        <a href="${pageContext.request.contextPath}/agent/paiements" class="active">Paiements</a>
        <a href="${pageContext.request.contextPath}/agent/statistiques">Statistiques</a>
    </nav>

    <div class="sidebar-bottom">
        <nav class="menu">
            <a href="${pageContext.request.contextPath}/logout" class="logout">Déconnexion</a>
        </nav>
    </div>
</aside>

<main class="main">

    <header class="topbar">
        <div>
            <h1>Enregistrer un paiement</h1>
            <p class="subtitle">Contrat <c:out value="${echeance.contrat.numeroContrat}"/></p>
        </div>
        <div class="profile">
            <c:out value="${sessionScope.prenom}"/> <c:out value="${sessionScope.nom}"/>
        </div>
    </header>

    <a class="back" href="${pageContext.request.contextPath}/agent/echeances?contratId=${echeance.contrat.id}">
        ← Retour à l'échéancier
    </a>

    <c:if test="${not empty erreur}">
        <div class="alerte alerte-erreur"><c:out value="${erreur}"/></div>
    </c:if>

    <section class="card">
        <h2>Échéance du ${echeance.dateEcheance}</h2>

        <div class="infos">
            <div>Client
                <strong><c:out value="${echeance.contrat.client.nom} ${echeance.contrat.client.prenom}"/></strong>
            </div>
            <div>Montant de l'échéance <strong>${echeance.montant} €</strong></div>
            <div>Reste à payer <strong>${reste} €</strong></div>
        </div>

        <form class="formulaire" action="${pageContext.request.contextPath}/agent/enregistrer-paiement" method="post">
            <input type="hidden" name="echeanceId" value="${echeance.id}">

            <label for="montant">Montant (€)</label>
            <input type="number" id="montant" name="montant" step="0.01" min="0.01" max="${reste}"
                   value="${empty param.montant ? reste : param.montant}" required>

            <label for="modePaiement">Mode de paiement</label>
            <select id="modePaiement" name="modePaiement" required>
                <c:forEach var="m" items="${modes}">
                    <option value="${m}" ${param.modePaiement == m ? 'selected' : ''}>${m}</option>
                </c:forEach>
            </select>

            <label for="reference">Référence (facultatif)</label>
            <input type="text" id="reference" name="reference" maxlength="100"
                   value="<c:out value='${param.reference}'/>"
                   placeholder="Laissez vide pour une référence automatique">

            <label>Encaissement</label>
            <div class="choix">
                <label>
                    <input type="radio" name="encaissement" value="EFFECTUE"
                           ${param.encaissement != 'EN_ATTENTE' ? 'checked' : ''}>
                    Paiement reçu
                </label>
                <label>
                    <input type="radio" name="encaissement" value="EN_ATTENTE"
                           ${param.encaissement == 'EN_ATTENTE' ? 'checked' : ''}>
                    En attente (virement, chèque à encaisser)
                </label>
            </div>

            <div class="actions">
                <button type="submit" class="btn btn-bleu">Enregistrer le paiement</button>
                <a class="btn btn-gris"
                   href="${pageContext.request.contextPath}/agent/echeances?contratId=${echeance.contrat.id}">Annuler</a>
            </div>
        </form>
    </section>

</main>

</body>
</html>
