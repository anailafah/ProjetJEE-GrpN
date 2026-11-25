<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GestionAssurance | Contrats</title>

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
</style>
</head>

<body>

<aside class="sidebar">
    <div class="brand">Gestion<span>Assurance</span></div>

    <div class="menu-title">MENU PRINCIPAL</div>

    <nav class="menu">
        <a href="${pageContext.request.contextPath}/agent/dashboard">Tableau de bord</a>
        <a href="${pageContext.request.contextPath}/agent/clients">Clients</a>
        <a href="${pageContext.request.contextPath}/agent/contrats" class="active">Contrats</a>
        <a href="${pageContext.request.contextPath}/agent/sinistres">Sinistres</a>
        <a href="${pageContext.request.contextPath}/agent/garanties">Garanties</a>
        <a href="${pageContext.request.contextPath}/agent/paiements">Paiements</a>
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
            <h1>Gestion des contrats</h1>
            
        	<a href="${pageContext.request.contextPath}/agent/ajouter-contrat"
   class="btn-ajouter">
    + Ajouter un contrat
</a>
        </div>

        <div class="profile">
            <c:out value="${sessionScope.prenom}"/>
            <c:out value="${sessionScope.nom}"/>
        </div>
    </header>

    <a class="back" href="${pageContext.request.contextPath}/agent/dashboard">
        ← Retour au tableau de bord
    </a>

    <section class="banner">
        <h2>Espace contrats</h2>
        <p>Retrouvez les informations et les statuts des contrats d'assurance.</p>
    </section>

    <section class="card">
        <h2>Liste des contrats</h2>
        <form action="${pageContext.request.contextPath}/agent/contrats"
      method="get"
      style="display:flex; gap:10px; margin-bottom:20px;">

    <input type="text"
           name="recherche"
           value="${recherche}"
           placeholder="Rechercher par numéro ou nom du client..."
           style="flex:1; padding:12px; border:1px solid #ddd; border-radius:7px;">

    <button type="submit"
            style="background:#1769aa; color:white; padding:12px 20px; border:none; border-radius:7px; cursor:pointer;">
        Rechercher
    </button>

    <a href="${pageContext.request.contextPath}/agent/contrats"
       style="background:#e5e7eb; color:#26344d; padding:12px 20px; border-radius:7px; text-decoration:none;">
        Réinitialiser
    </a>

</form>

        <div class="table-container">

            <c:choose>
                <c:when test="${not empty contrats}">
                    <table>
                        <thead>
                            <tr>
                                <th>Numéro</th>
                                <th>Client</th>
                                <th>Type d'assurance</th>
                                <th>Date début</th>
                                <th>Date fin</th>
                                <th>Montant</th>
                                <th>Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="contrat" items="${contrats}">
                                <tr>
                                    <td><strong><c:out value="${contrat.numeroContrat}"/></strong></td>
                                    <td>
                                        <c:out value="${contrat.client.prenom}"/>
                                        <c:out value="${contrat.client.nom}"/>
                                    </td>
                                    <td><c:out value="${contrat.typeAssurance.nom}"/></td>
                                    <td><c:out value="${contrat.dateDebut}"/></td>
                                    <td><c:out value="${contrat.dateFin}"/></td>
                                    <td><c:out value="${contrat.montant}"/> €</td>
                                    <td>
                                        <span class="badge badge-${contrat.statut.toLowerCase()}">
                                            <c:out value="${contrat.statut}"/>
                                        </span>
                                    </td>
                                    <td>
    									<a class="btn-modifier"
       										href="${pageContext.request.contextPath}/agent/modifier-contrat?id=${contrat.id}">
       										 Modifier
    									</a>

    								<c:if test="${contrat.statut == 'ACTIF' || contrat.statut == 'SUSPENDU'}">
        									<form action="${pageContext.request.contextPath}/agent/resilier-contrat"
              								method="post"
              										style="display:inline;"
              								onsubmit="return confirm('Voulez-vous vraiment résilier ce contrat ?');">

            										<input type="hidden" name="id" value="${contrat.id}">

           									 <button type="submit"
               							     style="background:#dc3545; color:white; padding:8px 13px; border:none; border-radius:6px; cursor:pointer; font-weight:bold;">
               									 Résilier
            									</button>
       											 </form>
   			 						</c:if>
   			 						<c:if test="${contrat.statut == 'ACTIF'}">
    <form action="${pageContext.request.contextPath}/agent/suspendre-contrat"
          method="post"
          style="display:inline;"
          onsubmit="return confirm('Voulez-vous vraiment suspendre ce contrat ?');">

        <input type="hidden" name="id" value="${contrat.id}">

        <button type="submit"
                style="background:#f0ad4e; color:white; padding:8px 13px; border:none; border-radius:6px; cursor:pointer; font-weight:bold;">
            Suspendre
        </button>
    </form>
</c:if>
<c:if test="${contrat.statut == 'EXPIRE'}">
    <form action="${pageContext.request.contextPath}/agent/renouveler-contrat"
          method="post"
          style="display:inline;"
          onsubmit="return confirm('Voulez-vous vraiment renouveler ce contrat pour un an ?');">

        <input type="hidden" name="id" value="${contrat.id}">

        <button type="submit"
                style="background:#198754; color:white; padding:8px 13px; border:none; border-radius:6px; cursor:pointer; font-weight:bold;">
            Renouveler
        </button>
    </form>
</c:if>
   			 						
								</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>

                <c:otherwise>
                    <div class="empty">
                        <h3>Aucun contrat disponible</h3>
                        <p>Les contrats apparaîtront ici lorsqu'ils seront chargés.</p>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </section>

</main>
</body>
</html>
