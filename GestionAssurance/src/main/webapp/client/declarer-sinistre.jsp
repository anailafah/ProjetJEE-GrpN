<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Déclarer un sinistre - GestionAssurance</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sinistres.css">
</head>

<body class="espace-client">

<header>
    <h2>GestionAssurance</h2>
    <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
</header>

<main>

    <a class="retour" href="${pageContext.request.contextPath}/client/sinistres">
        ← Retour à mes sinistres
    </a>

    <h1>Déclarer un sinistre</h1>
    <p class="sous-titre">
        Décrivez l'événement et joignez vos justificatifs. Un agent analysera votre dossier.
    </p>

    <c:if test="${not empty erreur}">
        <div class="erreur"><c:out value="${erreur}"/></div>
    </c:if>

    <div class="carte">

        <c:choose>
            <c:when test="${empty contrats}">
                <div class="vide">
                    <h3>Aucun contrat actif</h3>
                    <p>Vous devez avoir un contrat actif pour déclarer un sinistre.</p>
                    <p style="margin-top:15px;">
                        <a class="btn" href="${pageContext.request.contextPath}/client/mes-contrats">
                            Voir mes contrats
                        </a>
                    </p>
                </div>
            </c:when>

            <c:otherwise>
                <form class="formulaire"
                      action="${pageContext.request.contextPath}/client/declarer-sinistre"
                      method="post"
                      enctype="multipart/form-data">

                    <div class="champ">
                        <label for="contratId">Contrat concerné *</label>
                        <select id="contratId" name="contratId" required>
                            <option value="">-- Choisir un contrat --</option>
                            <c:forEach var="contrat" items="${contrats}">
                                <option value="${contrat.id}"
                                        ${param.contratId == contrat.id ? 'selected' : ''}>
                                    <c:out value="${contrat.numeroContrat}"/>
                                    - <c:out value="${contrat.typeAssurance.nom}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="champ">
                        <label for="typeSinistre">Type de sinistre *</label>
                        <select id="typeSinistre" name="typeSinistre" required>
                            <option value="">-- Choisir un type --</option>
                            <c:forEach var="type" items="${types}">
                                <option value="${type}"
                                        ${param.typeSinistre == type.name() ? 'selected' : ''}>
                                    <c:out value="${type.libelle}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="champ">
                        <label for="dateSinistre">Date du sinistre *</label>
                        <input type="date" id="dateSinistre" name="dateSinistre"
                               max="${aujourdhui}"
                               value="<c:out value='${param.dateSinistre}'/>" required>
                    </div>

                    <div class="champ">
                        <label for="montantDemande">Montant estimé (€) *</label>
                        <input type="number" id="montantDemande" name="montantDemande"
                               min="0.01" step="0.01" placeholder="Ex. 1500"
                               value="<c:out value='${param.montantDemande}'/>" required>
                    </div>

                    <div class="champ champ-large">
                        <label for="description">Description *</label>
                        <textarea id="description" name="description" minlength="10" maxlength="5000"
                                  placeholder="Que s'est-il passé ? Où, comment, quels dégâts ?"
                                  required><c:out value="${param.description}"/></textarea>
                    </div>

                    <div class="champ champ-large">
                        <label for="pieces">Pièces justificatives</label>
                        <input type="file" id="pieces" name="pieces" multiple
                               accept=".pdf,.jpg,.jpeg,.png">
                        <span class="aide">
                            Photos, factures, constat, dépôt de plainte... PDF, JPG ou PNG,
                            5 fichiers maximum, 5 Mo par fichier.
                        </span>
                    </div>

                    <div class="actions-formulaire">
                        <button type="submit" class="btn">Déclarer le sinistre</button>
                        <a class="btn btn-secondaire"
                           href="${pageContext.request.contextPath}/client/sinistres">Annuler</a>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>

    </div>

</main>

</body>
</html>
