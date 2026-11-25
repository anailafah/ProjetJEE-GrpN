<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Modifier une garantie</title>

    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f6f9;
        }

        .container {
            width: 600px;
            margin: 60px auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }

        h1 {
            color: #172b4d;
            margin-bottom: 25px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }

        input, textarea {
            width: 100%;
            padding: 11px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 14px;
        }

        textarea {
            height: 110px;
            resize: vertical;
        }

        .btn-modifier {
            background: #0d6efd;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: bold;
        }

        .btn-retour {
            display: inline-block;
            margin-left: 10px;
            color: #172b4d;
            text-decoration: none;
        }
    </style>
</head>

<body>

<div class="container">

    <h1>Modifier une garantie</h1>

    <form action="${pageContext.request.contextPath}/agent/modifier-garantie"
          method="post">

        <input type="hidden" name="id" value="${garantie.id}">

        <label for="nom">Nom de la garantie</label>
        <input type="text"
               id="nom"
               name="nom"
               value="${garantie.nom}"
               required>

        <label for="description">Description</label>
        <textarea id="description"
                  name="description"
                  required>${garantie.description}</textarea>

        <label for="montantCouverture">Montant maximal de couverture (€)</label>
        <input type="number"
               id="montantCouverture"
               name="montantCouverture"
               min="0.01"
               step="0.01"
               value="${garantie.montantCouverture}"
               required>

        <button type="submit" class="btn-modifier">
            Enregistrer les modifications
        </button>

        <a href="${pageContext.request.contextPath}/agent/garanties"
           class="btn-retour">
            Retour
        </a>

    </form>

</div>

</body>
</html>