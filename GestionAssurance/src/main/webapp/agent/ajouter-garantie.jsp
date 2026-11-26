<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Ajouter une garantie</title>

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

        .btn-ajouter {
            background: #198754;
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

        .btn-ajouter:hover {
            background: #146c43;
        }
    </style>
</head>

<body>

<div class="container">

    <h1>Ajouter une garantie</h1>

    <form action="${pageContext.request.contextPath}/agent/ajouter-garantie"
          method="post">

        <label for="nom">Nom de la garantie</label>
        <input type="text"
               id="nom"
               name="nom"
               placeholder="Exemple : Responsabilité civile"
               required>

        <label for="description">Description</label>
        <textarea id="description"
                  name="description"
                  placeholder="Décrivez la couverture proposée..."
                  required></textarea>

        <label for="montantCouverture">Montant maximal de couverture (€)</label>
        <input type="number"
               id="montantCouverture"
               name="montantCouverture"
               min="0.01"
               step="0.01"
               placeholder="Exemple : 15000"
               required>

        <button type="submit" class="btn-ajouter">
            Ajouter la garantie
        </button>

        <a href="${pageContext.request.contextPath}/agent/garanties"
           class="btn-retour">
            Retour
        </a>

    </form>

</div>

</body>
</html>