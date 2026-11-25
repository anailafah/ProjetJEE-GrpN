<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Gestion des rôles - GestionAssurance</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f3f5f9;
            color: #263247;
        }
        .sidebar {
            position: fixed;
            top: 0; left: 0; bottom: 0;
            width: 240px;
            padding: 28px 18px;
            background: #17243b;
            color: white;
        }
        .brand { font-size: 21px; font-weight: bold; margin-bottom: 38px; }
        .sidebar a {
            display: block;
            padding: 13px 14px;
            margin: 6px 0;
            color: #dce5f3;
            text-decoration: none;
            border-radius: 7px;
        }
        .sidebar a:hover, .sidebar a.active { background: #293b59; color: white; }
        .main { margin-left: 240px; padding: 35px; }
        h1 { margin-top: 0; font-size: 27px; }
        .subtitle { color: #78849a; margin-bottom: 25px; }
        .card {
            background: white;
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 3px 15px rgba(20,35,60,.06);
        }
        .message {
            padding: 12px 15px;
            margin-bottom: 18px;
            border-radius: 7px;
            background: #e2f5e9;
            color: #18723b;
        }
        .error {
            background: #fde8e8;
            color: #a52626;
        }
        table { width: 100%; border-collapse: collapse; }
        th, td {
            padding: 15px 12px;
            text-align: left;
            border-bottom: 1px solid #edf0f5;
        }
        th { color: #68758b; font-size: 13px; }
        select {
            padding: 9px;
            border: 1px solid #d8deea;
            border-radius: 6px;
            background: white;
        }
        button {
            padding: 9px 15px;
            border: none;
            border-radius: 6px;
            background: #315caa;
            color: white;
            cursor: pointer;
        }
        button:hover { background: #244987; }
        .empty { text-align: center; color: #8490a3; padding: 25px; }
        @media(max-width: 800px) {
            .sidebar { position: static; width: 100%; }
            .main { margin-left: 0; padding: 20px; }
            .card { overflow-x: auto; }
        }
    </style>
</head>
<body>
<aside class="sidebar">
    <div class="brand">GestionAssurance</div>
    <a href="${pageContext.request.contextPath}/admin/dashboard">Tableau de bord</a>
    <a href="${pageContext.request.contextPath}/admin/utilisateurs">Utilisateurs</a>
    <a class="active" href="${pageContext.request.contextPath}/admin/roles">Gestion des rôles</a>
    <a href="${pageContext.request.contextPath}/admin/types-assurance">Types d'assurance</a>
    <a href="${pageContext.request.contextPath}/admin/contrats">Contrats</a>
    <a href="${pageContext.request.contextPath}/admin/sinistres">Sinistres</a>
    <a href="${pageContext.request.contextPath}/statistiques">Statistiques</a>
    <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
</aside>

<main class="main">
    <h1>Gestion des rôles</h1>
    <p class="subtitle">Consultez les utilisateurs et attribuez-leur un rôle.</p>

    <c:if test="${not empty sessionScope.messageSucces}">
        <div class="message">${sessionScope.messageSucces}</div>
        <c:remove var="messageSucces" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.messageErreur}">
    	<div class="message error">${sessionScope.messageErreur}</div>
    	<c:remove var="messageErreur" scope="session"/>
	</c:if>

    <section class="card">
        <table>
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Email</th>
                    <th>Rôle actuel</th>
                    <th>Nouveau rôle</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${utilisateurs}">
                    <tr>
                        <td><c:out value="${u.nom}"/></td>
                        <td><c:out value="${u.prenom}"/></td>
                        <td><c:out value="${u.email}"/></td>
                        <td><c:out value="${u.role}"/></td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/admin/modifier-role">
                                <input type="hidden" name="id" value="${u.id}">
                                <select name="role" required>
                                    <option value="CLIENT" ${u.role == 'CLIENT' ? 'selected' : ''}>Client</option>
                                    <option value="AGENT" ${u.role == 'AGENT' ? 'selected' : ''}>Agent</option>
                                    <option value="ADMIN" ${u.role == 'ADMIN' ? 'selected' : ''}>Administrateur</option>
                                </select>
                                <button type="submit">Enregistrer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty utilisateurs}">
                    <tr><td colspan="5" class="empty">Aucun utilisateur à afficher.</td></tr>
                </c:if>
            </tbody>
        </table>
    </section>
</main>
</body>
</html>