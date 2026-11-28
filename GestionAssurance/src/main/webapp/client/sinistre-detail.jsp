<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sinistre <c:out value="${sinistre.numeroSinistre}"/> - GestionAssurance</title>
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

    <h1>Sinistre <c:out value="${sinistre.numeroSinistre}"/></h1>
    <p class="sous-titre">
        Déclaré le ${fn:replace(fn:substring(sinistre.dateDeclaration, 0, 16), 'T', ' à ')}
        sur le contrat <c:out value="${sinistre.contrat.numeroContrat}"/>.
    </p>

    <c:if test="${not empty succes}">
        <div class="succes"><c:out value="${succes}"/></div>
    </c:if>

    <%@ include file="/WEB-INF/views/sinistres/etapes.jspf" %>

    <div class="grille-detail">

        <div>
            <div class="carte">
                <h2>Ma déclaration</h2>

                <div class="infos">
                    <div class="info">
                        <span>Statut</span>
                        <p>
                            <span class="badge statut-${sinistre.statut}">
                                <c:out value="${sinistre.statut.libelle}"/>
                            </span>
                        </p>
                    </div>
                    <div class="info">
                        <span>Type</span>
                        <p><c:out value="${sinistre.typeSinistre.libelle}"/></p>
                    </div>
                    <div class="info">
                        <span>Date du sinistre</span>
                        <p>${sinistre.dateSinistre}</p>
                    </div>
                    <div class="info">
                        <span>Montant estimé</span>
                        <p>${sinistre.montantDemande} €</p>
                    </div>
                    <div class="info">
                        <span>Contrat</span>
                        <p>
                            <a href="${pageContext.request.contextPath}/client/details-contrat?id=${sinistre.contrat.id}">
                                <c:out value="${sinistre.contrat.numeroContrat}"/>
                            </a>
                            - <c:out value="${sinistre.contrat.typeAssurance.nom}"/>
                        </p>
                    </div>

                    <div class="description"><c:out value="${sinistre.description}"/></div>
                </div>
            </div>

            <div class="carte">
                <h2>Suivi de mon dossier</h2>

                <c:choose>
                    <c:when test="${not empty commentaires}">
                        <c:forEach var="commentaire" items="${commentaires}">
                            <div class="commentaire">
                                <div class="auteur">
                                    <c:out value="${commentaire.auteur.prenom}"/>
                                    <c:out value="${commentaire.auteur.nom}"/>
                                    - ${fn:replace(fn:substring(commentaire.dateCommentaire, 0, 16), 'T', ' à ')}
                                </div>
                                <p><c:out value="${commentaire.commentaire}"/></p>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="aide">Aucun message de votre agent pour le moment.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div>
            <c:if test="${sinistre.statut == 'INDEMNISE'}">
                <div class="carte">
                    <h2>Indemnisation</h2>
                    <p class="montant-indemnise">${sinistre.montantIndemnisation} €</p>
                    <c:if test="${not empty sinistre.garantie}">
                        <p class="aide" style="margin-top:6px;">
                            Au titre de la garantie « <c:out value="${sinistre.garantie.nom}"/> ».
                        </p>
                    </c:if>
                    <c:if test="${not empty sinistre.montantDemande and sinistre.montantIndemnisation lt sinistre.montantDemande}">
                        <p class="aide" style="margin-top:6px;">
                            Montant demandé : ${sinistre.montantDemande} €.
                            Le motif est indiqué dans le suivi de votre dossier.
                        </p>
                    </c:if>
                </div>
            </c:if>

            <div class="carte">
                <h2>Pièces justificatives</h2>

                <c:choose>
                    <c:when test="${not empty documents}">
                        <ul class="liste-documents">
                            <c:forEach var="document" items="${documents}">
                                <li>
                                    <span><c:out value="${document.nomFichier}"/></span>
                                    <a href="${pageContext.request.contextPath}/client/document-sinistre?id=${document.id}"
                                       target="_blank">Ouvrir</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <p class="aide">Aucune pièce jointe.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>

</main>

</body>
</html>
