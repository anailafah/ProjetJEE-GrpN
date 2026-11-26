<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes garanties - GestionAssurance</title>

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
            margin-bottom: 20px;
            color: #2563eb;
            text-decoration: none;
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

        .contrat {
            background: white;
            padding: 24px;
            border-radius: 12px;
            margin-bottom: 25px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
        }

        .contrat h2 {
            font-size: 19px;
            margin-bottom: 8px;
            color: #17243b;
        }

        .numero {
            color: #718096;
            font-size: 14px;
            margin-bottom: 20px;
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
            padding: 18px;
        }

        .garantie h3 {
            color: #2563eb;
            font-size: 16px;
            margin-bottom: 10px;
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
            font-size: 14px;
        }

        .vide {
            background: white;
            padding: 40px;
            text-align: center;
            border-radius: 12px;
            color: #718096;
        }

        .erreur {
            background: #fee2e2;
            color: #b91c1c;
            padding: 12px;
            border-radius: 7px;
            margin-bottom: 20px;
        }

        @media (max-width: 600px) {
            header {
                padding: 18px 5%;
            }

            header h2 {
                font-size: 18px;
            }

            main {
                margin-top: 25px;
            }
        }
    </style>
</head>

<body>

<header>
    <h2>GestionAssurance</h2>
    <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
</header>

<main>

    <a class="retour"
       href="${pageContext.request.contextPath}/client/dashboard">
        ← Retour au tableau de bord
    </a>

    <div class="titre">
        <h1>Mes garanties</h1>
        <p>Retrouvez les garanties associées à chacun de vos contrats.</p>
    </div>

    <c:if test="${not empty erreur}">
        <div class="erreur">${erreur}</div>
    </c:if>

    <c:choose>
        <c:when test="${not empty contrats}">

            <c:forEach var="contrat" items="${contrats}">

                <section class="contrat">

                    <h2>${contrat.typeAssurance.nom}</h2>

                    <p class="numero">
                        Contrat n° ${contrat.numeroContrat}
                    </p>

                    <c:choose>
                        <c:when test="${not empty contrat.garanties}">

                            <div class="garanties">

                                <c:forEach var="garantie"
                                           items="${contrat.garanties}">

                                    <div class="garantie">

                                        <h3>${garantie.nom}</h3>

                                        <p>
                                            <c:choose>
                                                <c:when test="${not empty garantie.description}">
                                                    ${garantie.description}
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
                            <p>Aucune garantie associée à ce contrat.</p>
                        </c:otherwise>
                    </c:choose>

                </section>

            </c:forEach>

        </c:when>

        <c:otherwise>
            <div class="vide">
                <h3>Aucun contrat disponible</h3>
                <p>Vos garanties apparaîtront ici lorsqu'un contrat vous sera attribué.</p>
            </div>
        </c:otherwise>
    </c:choose>

</main>

</body>
</html>