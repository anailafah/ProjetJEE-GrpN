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

<body class="espace-employe">

<%@ include file="/WEB-INF/views/sinistres/menu-employe.jspf" %>

<c:set var="client" value="${sinistre.contrat.client}"/>

<main class="main">

    <header class="topbar">
        <div>
            <h1>Sinistre <c:out value="${sinistre.numeroSinistre}"/></h1>
            <p class="sous-titre" style="margin-bottom:0;">
                Déclaré le ${fn:replace(fn:substring(sinistre.dateDeclaration, 0, 16), 'T', ' à ')}
                par <c:out value="${client.prenom}"/> <c:out value="${client.nom}"/>
            </p>
        </div>

        <div class="profile">
            <c:out value="${sessionScope.prenom}"/>
            <c:out value="${sessionScope.nom}"/>
        </div>
    </header>

    <a class="retour" href="${ctx}/${espace}/sinistres">← Retour à la liste des sinistres</a>

    <c:if test="${not empty succes}">
        <div class="succes"><c:out value="${succes}"/></div>
    </c:if>

    <c:if test="${not empty erreur}">
        <div class="erreur"><c:out value="${erreur}"/></div>
    </c:if>

    <%@ include file="/WEB-INF/views/sinistres/etapes.jspf" %>

    <div class="grille-detail">

        <%-- Colonne de gauche : déclaration et échanges --%>
        <div>
            <div class="carte">
                <h2>Déclaration</h2>

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
                        <span>Montant estimé par le client</span>
                        <p>${sinistre.montantDemande} €</p>
                    </div>
                    <div class="info">
                        <span>Montant indemnisé</span>
                        <p>
                            <c:choose>
                                <c:when test="${not empty sinistre.montantIndemnisation}">
                                    <strong>${sinistre.montantIndemnisation} €</strong>
                                    <c:if test="${not empty sinistre.garantie}">
                                        <br><span class="aide">Garantie « <c:out value="${sinistre.garantie.nom}"/> »</span>
                                    </c:if>
                                </c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </p>
                    </div>

                    <div class="description"><c:out value="${sinistre.description}"/></div>
                </div>
            </div>

            <div class="carte">
                <h2>Commentaires</h2>

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
                        <p class="aide" style="margin-bottom:15px;">Aucun commentaire pour le moment.</p>
                    </c:otherwise>
                </c:choose>

                <c:if test="${peutTraiter}">
                    <form action="${ctx}/agent/sinistre" method="post" style="margin-top:10px;">
                        <input type="hidden" name="id" value="${sinistre.id}">
                        <input type="hidden" name="action" value="commentaire">

                        <div class="champ">
                            <label for="commentaire">Ajouter un commentaire (visible par le client)</label>
                            <textarea id="commentaire" name="commentaire" maxlength="2000" required
                                      placeholder="Ex. pièce manquante, rendez-vous avec l'expert..."></textarea>
                        </div>

                        <button type="submit" class="btn" style="margin-top:12px;">Publier</button>
                    </form>
                </c:if>
            </div>
        </div>

        <%-- Colonne de droite : traitement, client, contrat, pièces --%>
        <div>
            <c:if test="${peutTraiter}">
                <div class="carte">
                    <h2>Traitement du dossier</h2>

                    <c:choose>
                        <%-- Accepté : il reste à saisir l'indemnisation --%>
                        <c:when test="${sinistre.statut == 'ACCEPTE' and empty garanties}">
                            <div class="erreur" style="margin-bottom:0;">
                                Le contrat de ce client ne comporte aucune garantie :
                                ce sinistre ne peut pas être indemnisé.
                                Ajoutez une garantie au contrat dans « Contrats ».
                            </div>
                        </c:when>

                        <c:when test="${sinistre.statut == 'ACCEPTE'}">
                            <form action="${ctx}/agent/sinistre" method="post">
                                <input type="hidden" name="id" value="${sinistre.id}">
                                <input type="hidden" name="action" value="indemniser">

                                <div class="champ">
                                    <label for="garantieId">Garantie qui couvre le sinistre</label>
                                    <select id="garantieId" name="garantieId" required>
                                        <c:forEach var="g" items="${garanties}" varStatus="etat">
                                            <option value="${g.id}"
                                                    data-plafond="${g.montantCouverture}"
                                                    data-propose="${montantsProposes[g.id]}"
                                                    ${etat.first ? 'selected' : ''}>
                                                <c:out value="${g.nom}"/> -
                                                <c:choose>
                                                    <c:when test="${not empty g.montantCouverture}">plafond ${g.montantCouverture} €</c:when>
                                                    <c:otherwise>sans plafond</c:otherwise>
                                                </c:choose>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="champ" style="margin-top:12px;">
                                    <label for="montant">Montant d'indemnisation (€)</label>
                                    <input type="number" id="montant" name="montant"
                                           min="0.01" step="0.01" required
                                           data-demande="${sinistre.montantDemande}"
                                           value="${montantsProposes[garanties[0].id]}">
                                    <span class="aide" id="aideMontant">
                                        Demandé par le client : ${sinistre.montantDemande} €.
                                        Le montant ne peut dépasser ni la demande ni le plafond de la garantie.
                                    </span>
                                </div>

                                <div class="champ" style="margin-top:12px;">
                                    <label for="motifIndemnisation">
                                        Motif <span id="motifObligatoire">(obligatoire si le montant est inférieur à la demande)</span>
                                    </label>
                                    <textarea id="motifIndemnisation" name="motif" maxlength="1500"
                                              placeholder="Ex. plafond de la garantie atteint, vétusté, rapport d'expertise..."></textarea>
                                    <span class="aide">Le motif sera visible par le client dans le suivi de son dossier.</span>
                                </div>

                                <button type="submit" class="btn btn-vert" style="margin-top:12px;">
                                    Enregistrer l'indemnisation
                                </button>
                            </form>

                            <%-- Aide à la saisie : met à jour le montant proposé et le maximum selon la garantie choisie,
                                 et rend le motif obligatoire pour un remboursement partiel.
                                 Les vraies vérifications restent faites côté serveur (SinistreService). --%>
                            <script>
                                (function () {
                                    var choix = document.getElementById('garantieId');
                                    var montant = document.getElementById('montant');
                                    var motif = document.getElementById('motifIndemnisation');
                                    var demande = parseFloat(montant.dataset.demande);

                                    function majMaximum() {
                                        var option = choix.options[choix.selectedIndex];
                                        var plafond = parseFloat(option.dataset.plafond);
                                        var max = [demande, plafond].filter(function (v) { return !isNaN(v); });
                                        if (max.length) {
                                            montant.max = Math.min.apply(null, max);
                                        } else {
                                            montant.removeAttribute('max');
                                        }
                                    }

                                    function majMotif() {
                                        var valeur = parseFloat(montant.value);
                                        motif.required = !isNaN(demande) && !isNaN(valeur) && valeur < demande;
                                    }

                                    choix.addEventListener('change', function () {
                                        var propose = choix.options[choix.selectedIndex].dataset.propose;
                                        if (propose) {
                                            montant.value = propose;
                                        }
                                        majMaximum();
                                        majMotif();
                                    });

                                    montant.addEventListener('input', majMotif);

                                    majMaximum();
                                    majMotif();
                                })();
                            </script>
                        </c:when>

                        <%-- Déclaré / En analyse : changement de statut --%>
                        <c:when test="${not empty statutsPossibles}">
                            <form action="${ctx}/agent/sinistre" method="post">
                                <input type="hidden" name="id" value="${sinistre.id}">
                                <input type="hidden" name="action" value="statut">

                                <div class="champ">
                                    <label for="statut">Nouveau statut</label>
                                    <select id="statut" name="statut" required>
                                        <c:forEach var="s" items="${statutsPossibles}">
                                            <c:if test="${s != 'INDEMNISE'}">
                                                <option value="${s}"><c:out value="${s.libelle}"/></option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="champ" style="margin-top:12px;">
                                    <label for="motif">Commentaire (facultatif)</label>
                                    <textarea id="motif" name="commentaire" maxlength="2000"
                                              placeholder="Ex. motif du refus"></textarea>
                                </div>

                                <button type="submit" class="btn" style="margin-top:12px;">Mettre à jour</button>
                            </form>
                        </c:when>

                        <%-- Refusé / Indemnisé : dossier clos --%>
                        <c:otherwise>
                            <p class="aide">Ce dossier est clos : son statut ne peut plus changer.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <div class="carte">
                <h2>Client</h2>
                <div class="infos" style="grid-template-columns:1fr;">
                    <div class="info">
                        <span>Nom</span>
                        <p><c:out value="${client.prenom}"/> <c:out value="${client.nom}"/></p>
                    </div>
                    <div class="info">
                        <span>Email</span>
                        <p><c:out value="${client.email}"/></p>
                    </div>
                    <div class="info">
                        <span>Téléphone</span>
                        <p><c:out value="${client.telephone}" default="-"/></p>
                    </div>
                </div>
            </div>

            <div class="carte">
                <h2>Contrat</h2>
                <div class="infos" style="grid-template-columns:1fr;">
                    <div class="info">
                        <span>Numéro</span>
                        <p><c:out value="${sinistre.contrat.numeroContrat}"/></p>
                    </div>
                    <div class="info">
                        <span>Type d'assurance</span>
                        <p><c:out value="${sinistre.contrat.typeAssurance.nom}"/></p>
                    </div>
                    <div class="info">
                        <span>Période</span>
                        <p>du ${sinistre.contrat.dateDebut} au ${sinistre.contrat.dateFin}</p>
                    </div>
                    <div class="info">
                        <span>Statut du contrat</span>
                        <p><c:out value="${sinistre.contrat.statut}"/></p>
                    </div>
                </div>
            </div>

            <div class="carte">
                <h2>Pièces justificatives</h2>

                <c:choose>
                    <c:when test="${not empty documents}">
                        <ul class="liste-documents">
                            <c:forEach var="document" items="${documents}">
                                <li>
                                    <span><c:out value="${document.nomFichier}"/></span>
                                    <a href="${ctx}/${espace}/document-sinistre?id=${document.id}"
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
