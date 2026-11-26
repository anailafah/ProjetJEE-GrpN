-- MySQL dump 10.13  Distrib 8.0.46, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: gestion_assurance
-- ------------------------------------------------------
-- Server version	8.0.46-0ubuntu0.24.04.4

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `commentaires_sinistre`
--

DROP TABLE IF EXISTS `commentaires_sinistre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `commentaires_sinistre` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sinistre_id` bigint NOT NULL,
  `utilisateur_id` bigint NOT NULL,
  `commentaire` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_commentaire` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `sinistre_id` (`sinistre_id`),
  KEY `utilisateur_id` (`utilisateur_id`),
  CONSTRAINT `commentaires_sinistre_ibfk_1` FOREIGN KEY (`sinistre_id`) REFERENCES `sinistres` (`id`),
  CONSTRAINT `commentaires_sinistre_ibfk_2` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateurs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contrat_garanties`
--

DROP TABLE IF EXISTS `contrat_garanties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contrat_garanties` (
  `contrat_id` bigint NOT NULL,
  `garantie_id` bigint NOT NULL,
  PRIMARY KEY (`contrat_id`,`garantie_id`),
  KEY `garantie_id` (`garantie_id`),
  CONSTRAINT `contrat_garanties_ibfk_1` FOREIGN KEY (`contrat_id`) REFERENCES `contrats` (`id`),
  CONSTRAINT `contrat_garanties_ibfk_2` FOREIGN KEY (`garantie_id`) REFERENCES `garanties` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contrats`
--

DROP TABLE IF EXISTS `contrats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contrats` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `numero_contrat` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `client_id` bigint NOT NULL,
  `type_assurance_id` bigint NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date DEFAULT NULL,
  `montant` decimal(12,2) NOT NULL,
  `statut` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT 'EN_ATTENTE',
  `date_creation` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `numero_contrat` (`numero_contrat`),
  KEY `client_id` (`client_id`),
  KEY `type_assurance_id` (`type_assurance_id`),
  CONSTRAINT `contrats_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `utilisateurs` (`id`),
  CONSTRAINT `contrats_ibfk_2` FOREIGN KEY (`type_assurance_id`) REFERENCES `types_assurance` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documents_sinistre`
--

DROP TABLE IF EXISTS `documents_sinistre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documents_sinistre` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sinistre_id` bigint NOT NULL,
  `nom_fichier` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `chemin_fichier` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_ajout` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `sinistre_id` (`sinistre_id`),
  CONSTRAINT `documents_sinistre_ibfk_1` FOREIGN KEY (`sinistre_id`) REFERENCES `sinistres` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `echeances`
--

DROP TABLE IF EXISTS `echeances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `echeances` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contrat_id` bigint NOT NULL,
  `date_echeance` date NOT NULL,
  `montant` decimal(12,2) NOT NULL,
  `statut` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT 'EN_ATTENTE',
  PRIMARY KEY (`id`),
  KEY `contrat_id` (`contrat_id`),
  CONSTRAINT `echeances_ibfk_1` FOREIGN KEY (`contrat_id`) REFERENCES `contrats` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `garanties`
--

DROP TABLE IF EXISTS `garanties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garanties` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `montant_maximal` decimal(12,2) DEFAULT NULL,
  `actif` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paiements`
--

DROP TABLE IF EXISTS `paiements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paiements` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `echeance_id` bigint NOT NULL,
  `montant` decimal(12,2) NOT NULL,
  `date_paiement` datetime DEFAULT CURRENT_TIMESTAMP,
  `mode_paiement` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reference_paiement` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `statut` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT 'EN_ATTENTE',
  PRIMARY KEY (`id`),
  KEY `echeance_id` (`echeance_id`),
  CONSTRAINT `paiements_ibfk_1` FOREIGN KEY (`echeance_id`) REFERENCES `echeances` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sinistres`
--

DROP TABLE IF EXISTS `sinistres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sinistres` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `numero_sinistre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contrat_id` bigint NOT NULL,
  `date_sinistre` date NOT NULL,
  `date_declaration` datetime DEFAULT CURRENT_TIMESTAMP,
  `description` text COLLATE utf8mb4_unicode_ci,
  `type_sinistre` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'AUTRE',
  `statut` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DECLARE',
  `montant_demande` decimal(12,2) DEFAULT NULL,
  `montant_indemnisation` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `numero_sinistre` (`numero_sinistre`),
  KEY `contrat_id` (`contrat_id`),
  CONSTRAINT `sinistres_ibfk_1` FOREIGN KEY (`contrat_id`) REFERENCES `contrats` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `type_assurance_garanties`
--

DROP TABLE IF EXISTS `type_assurance_garanties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_assurance_garanties` (
  `type_assurance_id` bigint NOT NULL,
  `garantie_id` bigint NOT NULL,
  PRIMARY KEY (`type_assurance_id`,`garantie_id`),
  KEY `garantie_id` (`garantie_id`),
  CONSTRAINT `type_assurance_garanties_ibfk_1` FOREIGN KEY (`type_assurance_id`) REFERENCES `types_assurance` (`id`),
  CONSTRAINT `type_assurance_garanties_ibfk_2` FOREIGN KEY (`garantie_id`) REFERENCES `garanties` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `types_assurance`
--

DROP TABLE IF EXISTS `types_assurance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `types_assurance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `duree_mois` int DEFAULT NULL,
  `prix_base` decimal(12,2) NOT NULL DEFAULT '0.00',
  `actif` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nom` (`nom`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `utilisateurs`
--

DROP TABLE IF EXISTS `utilisateurs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilisateurs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type_utilisateur` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nom` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `prenom` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mot_de_passe` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telephone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_creation` datetime DEFAULT CURRENT_TIMESTAMP,
  `actif` tinyint(1) DEFAULT '1',
  `adresse` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `matricule` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `service` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `niveau_acces` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `matricule` (`matricule`),
  CONSTRAINT `chk_type_utilisateur` CHECK ((`type_utilisateur` in (_utf8mb4'CLIENT',_utf8mb4'AGENT',_utf8mb4'ADMIN')))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-14 14:07:20
