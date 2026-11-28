<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Détails du contrat - GestionAssurance</title>

    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f3f5f9;
            color: #263247;
        }

        header {
            background: #17243b;
            color: white;
            padding: 22px 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        header h2 {
            font-size: 22px;
        }

        header a {
            color: white;
            text-decoration: none;
            background: #34445f;
            padding: 10px 16px;
            border-radius: 7px;
        }

        main {
            width: 90%;
            max-width: 1100px;
            margin: 40px auto;
        }

        .retour {
            display: inline-block;
            margin-bottom: 25px;
            color: #2563eb;
            text-decoration: none;
            font-weight: bold;
        }

        .titre {
            margin-bottom: 25px;
        }

        .titre h1 {
            font-size: 28px;
            margin-bottom: 8px;
        }

        .titre p {
            color: #718096;
        }

        .card {
            background: white;
            padding: 28px;
            border-radius: 12px;
            margin-bottom: 25px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
        }

        .card h2 {
            color: #17243b;
            font-size: 19px;
            margin-bottom: 22px;
        }

        .infos {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 22px;
        }

        .info {
            padding: 15px;
            background: #f8fafc;
            border-radius: 8px;
        }

        .info label {
            display: block;
            color: #718096;
            font-size: 13px;
            margin-bottom: 8px;
        }

        .info strong {
            color: #17243b;
            font-size: 15px;
        }

        .badge {
            display: inline-block;
            padding: 7px 12px;
            border-radius: 20px;
            background: #e8edf5;
            color: #263247;
            font-size: 12px;
            font-weight: bold;
        }

        .badge-actif {
            background: #dcfce7;
            color: #15803d;
        }

        .badge-en_attente {
            background: #fef3c7;
            color: #b45309;
        }

        .badge-suspendu {
            background: #ffedd5;
            color: #c2410c;
        }

        .badge-expire, .badge-resilie {
            background: #fee2e2;
            color: #b91c1c;
        }

        .garanties {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
            gap: 15px;
        }

        .garantie {
            background: #f8fafc;
            border: 1px solid #e8edf5;
            border-radius: 10px;
            padding: 20px;
        }

        .garantie h3 {
            color: #2563eb;
            margin-bottom: 12px;
        }

        .garantie p {
            color: #64748b;
            font-size: 14px;
            line-height: 1.5;
            margin-bottom: 12px;
        }

        .montant {
            font-weight: bold;
            color: #17243b;
        }

        .vide {
            text-align: center;
            color: #718096;
            padding: 25px;
        }

        .erreur {
            background: #fee2e2;
            color: #b91c1c;
            padding: 15px;
            border-radius: 7px;
            margin-bottom: 20px;
        }

        @media(max-width: 650px) {
            .infos {
                grid-template-columns: 1fr;
            }

            header {
                padding: 18px 5%;
            }

            main {
                margin-top: 25px;
            }
        }
        .echeances { width: 100%; border-collapse: collapse; font-size: 14px; }
		.echeances th, .echeances td { padding: 12px; text-align: left; border-bottom: 1px solid #eef0f4; }
		.echeances th { color: #64748b; font-size: 12px; text-transform: uppercase; }
		.badge-payee { background: #dcfce7; color: #15803d; }
		.badge-en_retard { background: #fee2e2; color: #b91c1c; }
		.badge-annulee { background: #f1f5f9; color: #64748b; }
    </style>
</head>

<body>

<header>
    <h2>GestionAssurance</h2>

    <a href="${pageContext.request.contextPath}/logout">
        Déconnexion
    </a>
</header>

<main>

    <a class="retour"
       href="${pageContext.request.contextPath}/client/mes-contrats">
        ← Retour à mes contrats
    </a>

    <c:if test="${not empty erreur}">
        <div class="erreur">
            <c:out value="${erreur}"/>
        </div>
    </c:if>

    <c:if test="${not empty contrat}">

        <div class="titre">
            <h1>Détails du contrat</h1>
            <p>Retrouvez toutes les informations de votre assurance.</p>
        </div>

        <!-- INFORMATIONS GENERALES -->
        <section class="card">

            <h2>Informations générales</h2>

            <div class="infos">

                <div class="info">
                    <label>Numéro du contrat</label>
                    <strong>
                        <c:out value="${contrat.numeroContrat}"/>
                    </strong>
                </div>

                <div class="info">
                    <label>Type d'assurance</label>
                    <strong>
                        <c:out value="${contrat.typeAssurance.nom}"/>
                    </strong>
                </div>

                <div class="info">
                    <label>Date de début</label>
                    <strong>${contrat.dateDebut}</strong>
                </div>

                <div class="info">
                    <label>Date de fin</label>
                    <strong>${contrat.dateFin}</strong>
                </div>

                <div class="info">
                    <label>Montant du contrat</label>
                    <strong>${contrat.montant} €</strong>
                </div>

                <div class="info">
                    <label>Statut</label>

                    <span class="badge badge-${contrat.statut.toLowerCase()}">
                        <c:out value="${contrat.statut}"/>
                    </span>
                </div>

            </div>

        </section>

        <!-- INFORMATIONS CLIENT -->
        <section class="card">

            <h2>Informations du titulaire</h2>

            <div class="infos">

                <div class="info">
                    <label>Nom</label>
                    <strong>
                        <c:out value="${contrat.client.nom}"/>
                    </strong>
                </div>

                <div class="info">
                    <label>Prénom</label>
                    <strong>
                        <c:out value="${contrat.client.prenom}"/>
                    </strong>
                </div>

                <div class="info">
                    <label>Email</label>
                    <strong>
                        <c:out value="${contrat.client.email}"/>
                    </strong>
                </div>

                <div class="info">
                    <label>Téléphone</label>
                    <strong>
                        <c:out value="${contrat.client.telephone}"/>
                    </strong>
                </div>

            </div>

        </section>

        <!-- GARANTIES -->
        <section class="card">

            <h2>Garanties associées</h2>

            <c:choose>

                <c:when test="${not empty contrat.garanties}">

                    <div class="garanties">

                        <c:forEach var="garantie" items="${contrat.garanties}">

                            <div class="garantie">

                                <h3>
                                    <c:out value="${garantie.nom}"/>
                                </h3>

                                <p>
                                    <c:choose>
                                        <c:when test="${not empty garantie.description}">
                                            <c:out value="${garantie.description}"/>
                                        </c:when>
                                        <c:otherwise>
                                            Aucune description disponible.
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <div class="montant">
                                    Couverture maximale :
                                    <c:choose>
                                        <c:when test="${not empty garantie.montantCouverture}">
                                            ${garantie.montantCouverture} €
                                        </c:when>
                                        <c:otherwise>
                                            Non renseignée
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                            </div>

                        </c:forEach>

                    </div>

                </c:when>

                <c:otherwise>

                    <div class="vide">
                        Aucune garantie associée à ce contrat.
                    </div>

                </c:otherwise>

            </c:choose>

        </section>
        <section class="card">
    		<h2>Échéancier</h2>
    		<c:choose>
        	<c:when test="${not empty echeances}">
            <table class="echeances">
                <tr><th>Date</th><th>Montant</th><th>Reste à payer</th><th>Statut</th></tr>
                <c:forEach var="e" items="${echeances}">
                    <tr>
                        <td>${e.dateEcheance}</td>
                        <td>${e.montant} €</td>
                        <td>${restes[e.id]} €</td>
                        <td>
                            <span class="badge badge-${e.statut.toLowerCase()}">
                                <c:choose>
                                    <c:when test="${e.statut == 'PAYEE'}">Payée</c:when>
                                    <c:when test="${e.statut == 'EN_RETARD'}">En retard</c:when>
                                    <c:when test="${e.statut == 'ANNULEE'}">Annulée</c:when>
                                    <c:otherwise>À venir</c:otherwise>
                                </c:choose>
                            </span>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <p style="margin-top:15px;">
                <a href="${pageContext.request.contextPath}/client/paiements">Voir l'historique de mes paiements →</a>
            </p>
        	</c:when>
        <	c:otherwise>
            <div class="vide"><p>L'échéancier sera disponible une fois le contrat activé.</p></div>
        </c:otherwise>
    </c:choose>
</section>

    </c:if>

</main>

</body>
</html>