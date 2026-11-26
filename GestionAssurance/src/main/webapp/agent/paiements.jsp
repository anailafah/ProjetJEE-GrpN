<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GestionAssurance | Paiements</title>

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
            <h1>Paiements</h1>
            <p class="subtitle">Historique des paiements et suivi des échéances</p>
        </div>
        <div class="profile">
            <c:out value="${sessionScope.prenom}"/> <c:out value="${sessionScope.nom}"/>
        </div>
    </header>

    <a class="back" href="${pageContext.request.contextPath}/agent/dashboard">← Retour au tableau de bord</a>

    <nav class="onglets">
        <a href="${pageContext.request.contextPath}/agent/paiements" class="actif">Historique des paiements</a>
        <a href="${pageContext.request.contextPath}/agent/echeances">Échéances</a>
    </nav>

    <c:if test="${not empty succes}">
        <div class="alerte alerte-succes"><c:out value="${succes}"/></div>
    </c:if>
    <c:if test="${not empty erreur}">
        <div class="alerte alerte-erreur"><c:out value="${erreur}"/></div>
    </c:if>

    <section class="card">
        <h2>Historique des paiements</h2>

        <form class="filtres" action="${pageContext.request.contextPath}/agent/paiements" method="get">
            <select name="statut">
                <option value="">Tous les statuts</option>
                <option value="EFFECTUE"   ${statutChoisi == 'EFFECTUE'   ? 'selected' : ''}>Effectués</option>
                <option value="EN_ATTENTE" ${statutChoisi == 'EN_ATTENTE' ? 'selected' : ''}>En attente</option>
            </select>
            <input type="number" name="contratId" min="1" value="${contratIdChoisi}" placeholder="ID du contrat">
            <button type="submit" class="btn btn-bleu">Filtrer</button>
            <a href="${pageContext.request.contextPath}/agent/paiements" class="btn btn-gris">Réinitialiser</a>
        </form>

        <p class="resume">
            ${paiements.size()} paiement(s) — total encaissé : <strong>${totalEncaisse} €</strong>
        </p>

        <div class="table-container">
            <c:choose>
                <c:when test="${empty paiements}">
                    <div class="empty">
                        <h3>Aucun paiement</h3>
                        <p>Aucun paiement ne correspond à ces critères.</p>
                    </div>
                </c:when>

                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Contrat</th>
                                <th>Client</th>
                                <th>Échéance du</th>
                                <th>Montant</th>
                                <th>Mode</th>
                                <th>Référence</th>
                                <th>Statut</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${paiements}">
                                <tr>
                                    <td>${p.datePaiement.format(formatDate)}</td>
                                    <td><strong><c:out value="${p.echeance.contrat.numeroContrat}"/></strong></td>
                                    <td><c:out value="${p.echeance.contrat.client.nom} ${p.echeance.contrat.client.prenom}"/></td>
                                    <td>${p.echeance.dateEcheance}</td>
                                    <td>${p.montant} €</td>
                                    <td><c:out value="${p.modePaiement}"/></td>
                                    <td><c:out value="${p.referencePaiement}"/></td>
                                    <td>
                                        <span class="badge badge-${p.statut.toLowerCase()}">
                                            <c:choose>
                                                <c:when test="${p.statut == 'EFFECTUE'}">Effectué</c:when>
                                                <c:when test="${p.statut == 'EN_ATTENTE'}">En attente</c:when>
                                                <c:otherwise><c:out value="${p.statut}"/></c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <td>
                                        <c:if test="${p.statut == 'EN_ATTENTE'}">
                                            <form action="${pageContext.request.contextPath}/agent/valider-paiement" method="post"
                                                  onsubmit="return confirm('Confirmer l\'encaissement de ce paiement ?');">
                                                <input type="hidden" name="id" value="${p.id}">
                                                <button type="submit" class="btn btn-vert btn-petit">Valider</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

</main>

</body>
</html>
