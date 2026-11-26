<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Espace client - GestionAssurance</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Arial, sans-serif;
        }

        body {
            background: #f3f6fb;
            color: #26344a;
        }

        /* HEADER */
        .header {
            background: #142b4a;
            color: white;
            padding: 20px 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 15px rgba(0,0,0,0.12);
        }

        .logo {
            font-size: 23px;
            font-weight: 800;
            letter-spacing: 0.5px;
        }

        .logo span {
            color: #35d0c2;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .user-name {
            font-size: 14px;
        }

        .logout {
            text-decoration: none;
            color: white;
            background: #e45b63;
            padding: 10px 17px;
            border-radius: 8px;
            font-size: 13px;
            font-weight: 600;
            transition: 0.3s;
        }

        .logout:hover {
            background: #c83e48;
        }

        /* CONTENU */
        .container {
            width: 90%;
            max-width: 1250px;
            margin: 40px auto;
        }

        .welcome {
            background: linear-gradient(120deg, #142b4a, #1d5275);
            padding: 38px;
            border-radius: 18px;
            color: white;
            margin-bottom: 35px;
            position: relative;
            overflow: hidden;
        }

        .welcome::after {
            content: "";
            position: absolute;
            width: 220px;
            height: 220px;
            background: rgba(53,208,194,0.12);
            border-radius: 50%;
            right: -50px;
            top: -80px;
        }

        .welcome h1 {
            font-size: 29px;
            margin-bottom: 12px;
            position: relative;
            z-index: 1;
        }

        .welcome p {
            color: #d4e5f2;
            font-size: 15px;
            position: relative;
            z-index: 1;
        }

        .section-title {
            margin-bottom: 20px;
        }

        .section-title h2 {
            font-size: 22px;
            color: #142b4a;
            margin-bottom: 7px;
        }

        .section-title p {
            color: #8290a3;
            font-size: 14px;
        }

        /* CARTES */
        .cards {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 22px;
        }

        .card {
            background: white;
            padding: 25px;
            border-radius: 15px;
            border: 1px solid #e9edf4;
            box-shadow: 0 5px 18px rgba(25,45,75,0.04);
            transition: 0.3s;
            min-height: 220px;
            display: flex;
            flex-direction: column;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 25px rgba(25,45,75,0.10);
        }

        .icon {
            width: 50px;
            height: 50px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 25px;
            margin-bottom: 18px;
        }

        .blue { background: #e7efff; }
        .green { background: #e1f8f2; }
        .orange { background: #fff0df; }
        .purple { background: #f0eaff; }
        .red { background: #ffe9eb; }
        .cyan { background: #e1f7fa; }

        .card h3 {
            font-size: 18px;
            color: #142b4a;
            margin-bottom: 10px;
        }

        .card p {
            color: #8290a3;
            font-size: 13px;
            line-height: 1.6;
            margin-bottom: 18px;
            flex-grow: 1;
        }

        .card-link {
            display: inline-block;
            text-decoration: none;
            color: #159f9a;
            font-weight: 700;
            font-size: 13px;
            margin-top: auto;
        }

        .card-link:hover {
            color: #117c78;
        }

        .status {
            display: inline-block;
            background: #f0f2f5;
            color: #8792a2;
            padding: 7px 11px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
            align-self: flex-start;
            margin-top: auto;
        }

        /* FOOTER */
        .footer {
            text-align: center;
            color: #98a3b2;
            font-size: 12px;
            padding: 30px 10px;
            margin-top: 35px;
        }

        /* RESPONSIVE */
        @media (max-width: 900px) {
            .cards {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        @media (max-width: 600px) {
            .header {
                padding: 18px 5%;
                flex-direction: column;
                gap: 15px;
            }

            .user-info {
                flex-direction: column;
                gap: 10px;
            }

            .container {
                width: 92%;
                margin-top: 25px;
            }

            .welcome {
                padding: 27px 22px;
            }

            .welcome h1 {
                font-size: 23px;
            }

            .cards {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>

<body>

<header class="header">

    <div class="logo">
        Gestion<span>Assurance</span>
    </div>

    <div class="user-info">
        <span class="user-name">
            Bonjour, ${sessionScope.prenom} ${sessionScope.nom}
        </span>

        <a class="logout"
           href="${pageContext.request.contextPath}/logout">
            Déconnexion
        </a>
    </div>

</header>

<main class="container">

    <section class="welcome">
        <h1>Bienvenue dans votre espace client</h1>
        <p>
            Gérez facilement vos assurances, consultez vos contrats
            et suivez vos demandes depuis votre espace personnel.
        </p>
    </section>

    <div class="section-title">
        <h2>Mon espace personnel</h2>
        <p>Retrouvez tous vos services en un seul endroit.</p>
    </div>

    <section class="cards">

        <!-- CONTRATS -->
    <div class="card">
    	<h3>Mes contrats</h3>
    	<p>Consultez vos contrats d'assurance et leurs informations.</p>
    	<a href="${pageContext.request.contextPath}/client/mes-contrats"
   style="
       display: inline-block;
       margin-top: 15px;
       padding: 11px 20px;
       background: linear-gradient(120deg, #142b4a, #1d5275);
       color: white;
       text-decoration: none;
       border-radius: 8px;
       font-size: 14px;
       font-weight: 600;
       transition: 0.3s;
   ">
    Consulter mes contrats →
</a>
	</div>

        <!-- GARANTIES -->
        <div class="card">
            <h3>Mes garanties</h3>
            <p>
                Découvrez les garanties et les couvertures
                associées à vos contrats.
            </p>
<a href="${pageContext.request.contextPath}/client/garanties"
   style="
   		display:inline-block;
  		margin-top:15px;
   		padding:10px 18px;
   		background:linear-gradient(120deg, #142b4a, #1d5275);
   		color:white;
   		text-decoration:none;
   		border-radius:8px;
   		font-weight:600;">
    	Consulter mes garanties
		</a>        </div>

        <!-- PAIEMENTS -->
        <div class="card">
            <h3>Mes paiements</h3>
            <p>
                Consultez vos paiements, vos échéances
                et votre historique de règlement.
            </p>
<a href="${pageContext.request.contextPath}/client/paiements"
   style="
   		display:inline-block;
  		margin-top:15px;
   		padding:10px 18px;
   		background:linear-gradient(120deg, #142b4a, #1d5275);
   		color:white;
   		text-decoration:none;
   		border-radius:8px;
   		font-weight:600;">
    	Consulter mes paiements
		</a>       
		 </div>
           

        <!-- SINISTRES -->
        <div class="card">
            <h3>Mes sinistres</h3>
            <p>
                Déclarez un sinistre et suivez son traitement
                ainsi que son évolution.
            </p>
            <span class="status">À venir</span>
        </div>

        <!-- HISTORIQUE -->
        <div class="card">
            <h3>Historique de mes demandes</h3>
            <p>
                Retrouvez l'historique de vos déclarations
                et de vos demandes.
            </p>
            <span class="status">À venir</span>
        </div>

        <!-- ASSISTANT IA -->
        <div class="card">
            <h3>Assistant IA</h3>
            <p>
                Posez vos questions et obtenez de l'aide
                concernant vos assurances.
            </p>

            <a class="card-link"
               href="${pageContext.request.contextPath}/assistant-ia">
                Discuter avec l'assistant →
            </a>
        </div>

        <!-- PROFIL -->
        <div class="card">
            <h3>Mon profil</h3>
            <p>
                Consultez et modifiez vos informations
                personnelles.
            </p>
            <span class="status">À venir</span>
        </div>

    </section>

</main>

<footer class="footer">
    © 2026 GestionAssurance - Tous droits réservés.
</footer>

</body>
</html>