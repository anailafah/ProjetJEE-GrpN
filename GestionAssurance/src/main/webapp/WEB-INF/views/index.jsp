
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AssurPlus - Accueil</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/accueil.css">
</head>

<body>

    <!-- En-tête -->
    <header class="header">
        <a href="${pageContext.request.contextPath}/accueil"
           class="logo">ASSUR<span>PLUS</span></a>

        <nav class="navigation">
            <a href="${pageContext.request.contextPath}/accueil">Accueil</a>
            <a href="#services">Nos services</a>
            <a href="#apropos">À propos</a>
            <a href="#contact">Contact</a>
        </nav>

		<a href="${pageContext.request.contextPath}/login" class="btn-login">
    		Se connecter
		</a>
		<a href="${pageContext.request.contextPath}/inscription" class="btn-register">
    		Créer un compte
		</a>
    </header>

    <!-- Bannière principale -->
    <main>

        <section class="hero">
            <div class="hero-content">
                <p class="sous-titre">BIENVENUE CHEZ ASSURPLUS</p>

                <h1>Votre protection,<br>
                    <span>notre priorité.</span>
                </h1>

                <p class="hero-description">
                    Nous vous accompagnons au quotidien avec des solutions
                    d'assurance adaptées à vos besoins.
                    Protégez ce qui compte vraiment pour vous.
                </p>

                <a href="#services" class="btn-principal">
                    Découvrir nos offres
                </a>
            </div>

            <div class="hero-decoration">
                <div class="decoration-card">
                    <h2>AssurPlus</h2>
                    <p>La confiance au cœur de nos engagements.</p>
                    <div class="decoration-line"></div>
                    <strong>Votre avenir, notre engagement.</strong>
                </div>
            </div>
        </section>

        <!-- Services -->
        <section class="services" id="services">
            <div class="section-title">
                <p class="sous-titre">NOS SOLUTIONS</p>
                <h2>Nos services d'assurance</h2>
                <p>
                    Découvrez nos différentes solutions pour vous protéger
                    et protéger vos proches.
                </p>
            </div>

            <div class="services-container">

                <article class="service-card">
                    <h3>Assurance santé</h3>
                    <p>
                        Bénéficiez d'une protection adaptée à vos besoins
                        de santé et à ceux de votre famille.
                    </p>
                </article>

                <article class="service-card">
                    <h3>Assurance automobile</h3>
                    <p>
                        Protégez votre véhicule et conduisez
                        en toute sérénité.
                    </p>
                </article>

                <article class="service-card">
                    <h3>Assurance habitation</h3>
                    <p>
                        Protégez votre logement et vos biens
                        contre les imprévus.
                    </p>
                </article>

                <article class="service-card">
                    <h3>Assurance vie</h3>
                    <p>
                        Préparez votre avenir et protégez
                        financièrement vos proches.
                    </p>
                </article>

            </div>
        </section>

        <!-- À propos -->
        <section class="about" id="apropos">
            <div class="about-content">
                <p class="sous-titre">QUI SOMMES-NOUS ?</p>
                <h2>Une assurance basée sur la confiance</h2>

                <p>
                    Chez AssurPlus, nous mettons la satisfaction de nos
                    clients au centre de nos priorités.
                    Notre objectif est de proposer des solutions
                    d'assurance simples, accessibles et adaptées à chacun.
                </p>

                <p>
                    Nous vous accompagnons dans la gestion de vos contrats,
                    le suivi de vos paiements et le traitement de vos
                    déclarations de sinistres.
                </p>
            </div>
        </section>

        <!-- Contact -->
        <section class="contact" id="contact">
            <div class="section-title">
                <p class="sous-titre">NOUS CONTACTER</p>
                <h2>Nous sommes à votre écoute</h2>
                <p>
                    Une question concernant nos services ?
                    Notre équipe est là pour vous accompagner.
                </p>
            </div>

            <div class="contact-info">
                <div>
                    <h3>Email</h3>
                    <p>contact@assurplus.fr</p>
                </div>

                <div>
                    <h3>Téléphone</h3>
                    <p>01 00 00 00 00</p>
                </div>
            </div>
        </section>

    </main>

    <!-- Pied de page -->
    <footer class="footer">
        <div class="footer-content">
            <a href="${pageContext.request.contextPath}/accueil"
               class="logo footer-logo">ASSUR<span>PLUS</span></a>

            <p>
                Votre protection, notre priorité.
            </p>

            <div class="footer-links">
                <a href="#services">Nos services</a>
                <a href="#apropos">À propos</a>
                <a href="#contact">Contact</a>
            </div>
        </div>
    </footer>

</body>
</html>