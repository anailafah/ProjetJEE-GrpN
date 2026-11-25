<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier un type d'assurance</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f4f6fa;
            color: #263449;
        }

        .topbar {
            background: #142d4e;
            color: white;
            padding: 22px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 21px;
            font-weight: bold;
        }

        .logo span {
            color: #45c3b1;
        }

        .back-link {
            color: white;
            text-decoration: none;
            font-size: 14px;
            padding: 10px 16px;
            border: 1px solid rgba(255,255,255,0.35);
            border-radius: 7px;
            transition: 0.2s;
        }

        .back-link:hover {
            background: #24466e;
        }

        .container {
            max-width: 750px;
            margin: 45px auto;
            padding: 0 20px;
        }

        .page-heading {
            margin-bottom: 25px;
        }

        .page-heading h1 {
            font-size: 27px;
            color: #142d4e;
            margin-bottom: 9px;
        }

        .page-heading p {
            font-size: 14px;
            color: #8490a0;
            line-height: 1.5;
        }

        .form-card {
            background: white;
            padding: 35px;
            border-radius: 12px;
            box-shadow: 0 3px 15px rgba(25,45,75,0.06);
            border: 1px solid #edf0f5;
        }

        .form-group {
            margin-bottom: 23px;
        }

        label {
            display: block;
            color: #34445b;
            font-size: 14px;
            font-weight: bold;
            margin-bottom: 9px;
        }

        input,
        textarea {
            width: 100%;
            padding: 13px 14px;
            border: 1px solid #dce2eb;
            border-radius: 7px;
            font-family: Arial, sans-serif;
            font-size: 14px;
            color: #263449;
            background: white;
            outline: none;
            transition: 0.2s;
        }

        input:focus,
        textarea:focus {
            border-color: #168f91;
            box-shadow: 0 0 0 3px rgba(22,143,145,0.10);
        }

        textarea {
            resize: vertical;
            min-height: 110px;
        }

        .form-help {
            display: block;
            margin-top: 7px;
            color: #98a2b1;
            font-size: 12px;
        }

        .form-actions {
            display: flex;
            gap: 12px;
            margin-top: 30px;
        }

        .btn-save {
            flex: 1;
            border: none;
            background: #168f91;
            color: white;
            padding: 14px;
            border-radius: 8px;
            font-size: 14px;
            font-weight: bold;
            cursor: pointer;
            transition: 0.2s;
        }

        .btn-save:hover {
            background: #117779;
        }

        .btn-cancel {
            text-decoration: none;
            background: #edf1f6;
            color: #526176;
            padding: 14px 20px;
            border-radius: 8px;
            font-size: 14px;
            font-weight: bold;
            text-align: center;
            transition: 0.2s;
        }

        .btn-cancel:hover {
            background: #e0e6ee;
        }

        .footer {
            text-align: center;
            color: #9aa5b4;
            font-size: 12px;
            padding: 30px;
        }

        @media (max-width: 600px) {
            .topbar {
                padding: 20px;
                flex-direction: column;
                gap: 18px;
                align-items: flex-start;
            }

            .form-card {
                padding: 23px;
            }

            .form-actions {
                flex-direction: column;
            }
        }
    </style>
</head>

<body>

    <header class="topbar">
        <div class="logo">
            Gestion<span>Assurance</span>
        </div>

        <a class="back-link"
           href="${pageContext.request.contextPath}/admin/types-assurance">
            Retour aux types d'assurance
        </a>
    </header>

    <main class="container">

        <section class="page-heading">
            <h1>Modifier un type d'assurance</h1>
            <p>
                Modifiez les informations du produit d'assurance sélectionné.
            </p>
        </section>

        <section class="form-card">

            <form action="${pageContext.request.contextPath}/admin/modifier-type-assurance"
                  method="post">

                <input type="hidden" name="id" value="${type.id}">

                <div class="form-group">
                    <label for="nom">Nom du type d'assurance</label>
                    <input type="text"
                           id="nom"
                           name="nom"
                           value="<c:out value='${type.nom}'/>"
                           maxlength="100"
                           required>
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description"
                              name="description"
                              rows="4"><c:out value="${type.description}"/></textarea>
                    <span class="form-help">
                        Décrivez les caractéristiques de cette assurance.
                    </span>
                </div>

                <div class="form-group">
                    <label for="dureeMois">Durée en mois</label>
                    <input type="number"
                           id="dureeMois"
                           name="dureeMois"
                           value="${type.dureeMois}"
                           min="1"
                           required>
                </div>

                <div class="form-group">
                    <label for="tarifBase">Tarif de base (€)</label>
                    <input type="number"
                           id="tarifBase"
                           name="tarifBase"
                           value="${type.tarifBase}"
                           min="0.01"
                           step="0.01"
                           required>
                    <span class="form-help">
                        Indiquez le tarif de base de cette assurance en euros.
                    </span>
                </div>

                <div class="form-actions">

                    <button type="submit" class="btn-save">
                        Enregistrer les modifications
                    </button>

                    <a class="btn-cancel"
                       href="${pageContext.request.contextPath}/admin/types-assurance">
                        Annuler
                    </a>

                </div>

            </form>

        </section>

    </main>

    <footer class="footer">
        GestionAssurance — Administration
    </footer>

</body>
</html>