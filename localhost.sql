-- MySQL dump 10.13  Distrib 5.5.22, for Win64 (x86)
--
-- Host: localhost    Database: leimz
-- ------------------------------------------------------
-- Server version	6.0.3-alpha-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `mot_de_passe` text NOT NULL,
  `currjoueur` varchar(25) DEFAULT NULL,
  `nom_de_compte` varchar(20) NOT NULL,
  `connected` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`nom_de_compte`),
  UNIQUE KEY `currjoueur` (`currjoueur`),
  CONSTRAINT `Account_ibfk_1` FOREIGN KEY (`currjoueur`) REFERENCES `personnage` (`name`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES ('chelendil','Choco','chelendil',0),('greglebg','FaZeGa','fazega',0);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caracteristiques`
--

DROP TABLE IF EXISTS `caracteristiques`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caracteristiques` (
  `name` varchar(70) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caracteristiques`
--

LOCK TABLES `caracteristiques` WRITE;
/*!40000 ALTER TABLE `caracteristiques` DISABLE KEYS */;
INSERT INTO `caracteristiques` VALUES ('deplacement'),('dommages_cac'),('dommages_magie'),('endurance'),('energie'),('precision'),('vie');
/*!40000 ALTER TABLE `caracteristiques` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caracteristiques_classe`
--

DROP TABLE IF EXISTS `caracteristiques_classe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caracteristiques_classe` (
  `classe` varchar(20) NOT NULL,
  `caracteristique` varchar(20) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`classe`,`caracteristique`),
  KEY `id_caracteristique` (`caracteristique`),
  CONSTRAINT `caracteristiques_classe_ibfk_1` FOREIGN KEY (`caracteristique`) REFERENCES `caracteristiques` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `caracteristiques_classe_ibfk_2` FOREIGN KEY (`classe`) REFERENCES `classe` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caracteristiques_classe`
--

LOCK TABLES `caracteristiques_classe` WRITE;
/*!40000 ALTER TABLE `caracteristiques_classe` DISABLE KEYS */;
INSERT INTO `caracteristiques_classe` VALUES ('barbare','deplacement',1000),('barbare','dommages_cac',2),('barbare','dommages_magie',2),('barbare','endurance',10),('barbare','energie',20),('barbare','precision',-20),('barbare','vie',15);
/*!40000 ALTER TABLE `caracteristiques_classe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caracteristiques_joueur`
--

DROP TABLE IF EXISTS `caracteristiques_joueur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caracteristiques_joueur` (
  `nom_joueur` varchar(30) NOT NULL,
  `caracteristique` varchar(20) NOT NULL,
  `value` int(11) NOT NULL,
  `current_value` int(11) DEFAULT NULL,
  PRIMARY KEY (`nom_joueur`,`caracteristique`),
  KEY `caracteristique` (`caracteristique`),
  CONSTRAINT `caracteristiques_joueur_ibfk_1` FOREIGN KEY (`caracteristique`) REFERENCES `caracteristiques` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `caracteristiques_joueur_ibfk_2` FOREIGN KEY (`nom_joueur`) REFERENCES `personnage` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caracteristiques_joueur`
--

LOCK TABLES `caracteristiques_joueur` WRITE;
/*!40000 ALTER TABLE `caracteristiques_joueur` DISABLE KEYS */;
INSERT INTO `caracteristiques_joueur` VALUES ('Choco','deplacement',10,10),('Choco','dommages_cac',10,10),('Choco','dommages_magie',10,10),('Choco','endurance',2500,2500),('Choco','energie',8000,8000),('Choco','precision',60,0),('Choco','vie',150,150),('FaZeGa','deplacement',10,10),('FaZeGa','dommages_cac',10,10),('FaZeGa','dommages_magie',10,10),('FaZeGa','endurance',2500,2500),('FaZeGa','energie',8000,8000),('FaZeGa','precision',60,0),('FaZeGa','vie',150,150);
/*!40000 ALTER TABLE `caracteristiques_joueur` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caracteristiques_objet`
--

DROP TABLE IF EXISTS `caracteristiques_objet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caracteristiques_objet` (
  `objet` varchar(25) NOT NULL,
  `caracteristique` varchar(20) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`objet`,`caracteristique`),
  KEY `id_caracteristique` (`caracteristique`),
  CONSTRAINT `caracteristiques_objet_ibfk_1` FOREIGN KEY (`caracteristique`) REFERENCES `caracteristiques` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `caracteristiques_objet_ibfk_2` FOREIGN KEY (`objet`) REFERENCES `item` (`nom`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caracteristiques_objet`
--

LOCK TABLES `caracteristiques_objet` WRITE;
/*!40000 ALTER TABLE `caracteristiques_objet` DISABLE KEYS */;
INSERT INTO `caracteristiques_objet` VALUES ('Anneau Dfer','precision',2),('Anneau Maly','endurance',50),('Cape Ripu','energie',1);
/*!40000 ALTER TABLE `caracteristiques_objet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caracteristiques_race`
--

DROP TABLE IF EXISTS `caracteristiques_race`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caracteristiques_race` (
  `race` varchar(25) NOT NULL,
  `caracteristique` varchar(20) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`race`,`caracteristique`),
  KEY `id_caracteristique` (`caracteristique`),
  CONSTRAINT `caracteristiques_race_ibfk_1` FOREIGN KEY (`caracteristique`) REFERENCES `caracteristiques` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `caracteristiques_race_ibfk_2` FOREIGN KEY (`race`) REFERENCES `race` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caracteristiques_race`
--

LOCK TABLES `caracteristiques_race` WRITE;
/*!40000 ALTER TABLE `caracteristiques_race` DISABLE KEYS */;
INSERT INTO `caracteristiques_race` VALUES ('Groz','deplacement',8000),('Groz','dommages_cac',10),('Groz','dommages_magie',6),('Groz','endurance',20),('Groz','energie',150),('Groz','precision',60),('Groz','vie',10);
/*!40000 ALTER TABLE `caracteristiques_race` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classe`
--

DROP TABLE IF EXISTS `classe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classe` (
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classe`
--

LOCK TABLES `classe` WRITE;
/*!40000 ALTER TABLE `classe` DISABLE KEYS */;
INSERT INTO `classe` VALUES ('barbare');
/*!40000 ALTER TABLE `classe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classe_sort`
--

DROP TABLE IF EXISTS `classe_sort`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classe_sort` (
  `classe` varchar(25) NOT NULL,
  `sort` varchar(50) NOT NULL,
  PRIMARY KEY (`classe`,`sort`),
  KEY `sort` (`sort`),
  CONSTRAINT `classe_sort_ibfk_1` FOREIGN KEY (`classe`) REFERENCES `classe` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `classe_sort_ibfk_2` FOREIGN KEY (`sort`) REFERENCES `sorts_classe` (`nom`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classe_sort`
--

LOCK TABLES `classe_sort` WRITE;
/*!40000 ALTER TABLE `classe_sort` DISABLE KEYS */;
INSERT INTO `classe_sort` VALUES ('barbare','Coup de Poing');
/*!40000 ALTER TABLE `classe_sort` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventaire`
--

DROP TABLE IF EXISTS `inventaire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inventaire` (
  `joueur` varchar(30) NOT NULL DEFAULT '',
  `objet` varchar(25) NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`joueur`,`objet`),
  KEY `objet` (`objet`),
  CONSTRAINT `inventaire_ibfk_1` FOREIGN KEY (`joueur`) REFERENCES `personnage` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `inventaire_ibfk_2` FOREIGN KEY (`objet`) REFERENCES `item` (`nom`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventaire`
--

LOCK TABLES `inventaire` WRITE;
/*!40000 ALTER TABLE `inventaire` DISABLE KEYS */;
INSERT INTO `inventaire` VALUES ('Choco','Anneau Maly',1),('FaZeGa','Anneau Dfer',1);
/*!40000 ALTER TABLE `inventaire` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `nom` varchar(200) NOT NULL DEFAULT '',
  `description` text,
  `type` varchar(20) NOT NULL,
  `icone` varchar(100) DEFAULT NULL,
  `apercu` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`nom`),
  KEY `type` (`type`),
  CONSTRAINT `item_ibfk_1` FOREIGN KEY (`type`) REFERENCES `item_type` (`type`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES ('Anneau Dfer','Un anneau simple mais elegant, qui vous permettra de porter des coups sanglants !','anneau','data/Images/Objets/anneau_dfer.png','data/Images/Objets/anneau_dfer.png'),('Anneau Maly','Cet anneau fut forgé il y a très longtemps dans les montagnes cléodors. On remarque une fissure sur son côté, encore mystérieuse .....','anneau','data/Images/Objets/anneau_maly.png','data/Images/Objets/anneau_maly.png'),('Cape Ripu','Cette cape est pourrie, ne donne (presque) rien. Elle sert juste à faire joli :p','cape','data/GUI/Images/ailles.png','data/GUI/Images/ailles.png'),('Chapeau D\'Vache','Ce chapeau, d\'une grande valeur, n\'est pas très utile. Il sert surtout à se rendre plus beau.','chapeau','data/GUI/Images/ailles.png','data/GUI/Images/ailles.png');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_type`
--

DROP TABLE IF EXISTS `item_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_type` (
  `type` varchar(30) NOT NULL,
  PRIMARY KEY (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_type`
--

LOCK TABLES `item_type` WRITE;
/*!40000 ALTER TABLE `item_type` DISABLE KEYS */;
INSERT INTO `item_type` VALUES ('anneau'),('cape'),('chapeau');
/*!40000 ALTER TABLE `item_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `map`
--

DROP TABLE IF EXISTS `map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `map` (
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `type` varchar(30) NOT NULL,
  `monsterHolder` tinyint(1) NOT NULL,
  PRIMARY KEY (`x`,`y`),
  KEY `type` (`type`),
  CONSTRAINT `map_ibfk_1` FOREIGN KEY (`type`) REFERENCES `tiles_map` (`nom`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `map`
--

LOCK TABLES `map` WRITE;
/*!40000 ALTER TABLE `map` DISABLE KEYS */;
INSERT INTO `map` VALUES (0,0,'herbe',0),(0,1,'herbe',0),(0,2,'herbe',0),(0,3,'herbe',0),(0,4,'herbe',0),(0,5,'herbe',0),(0,6,'herbe',0),(0,7,'herbe',0),(0,8,'herbe',0),(0,9,'herbe',0),(0,10,'herbe',0),(0,11,'herbe',0),(0,12,'herbe',0),(0,13,'herbe',0),(0,14,'herbe',0),(0,15,'herbe',0),(0,16,'herbe',0),(0,17,'herbe',0),(0,18,'herbe',0),(0,19,'herbe',0),(0,20,'herbe',0),(0,21,'herbe',0),(0,22,'herbe',0),(0,23,'herbe',0),(0,24,'herbe',0),(0,25,'herbe',0),(0,26,'herbe',0),(0,27,'herbe',0),(0,28,'herbe',0),(0,29,'herbe',0),(0,30,'herbe',0),(0,31,'herbe',0),(0,32,'herbe',0),(0,33,'herbe',0),(0,34,'herbe',0),(0,35,'herbe',0),(0,36,'herbe',0),(0,37,'herbe',0),(0,38,'herbe',0),(0,39,'herbe',0),(0,40,'herbe',0),(0,41,'herbe',0),(0,42,'herbe',0),(0,43,'herbe',0),(0,44,'herbe',0),(0,45,'herbe',0),(0,46,'herbe',0),(0,47,'herbe',0),(0,48,'herbe',0),(0,49,'herbe',0),(1,0,'herbe',0),(1,1,'herbe',0),(1,2,'herbe',0),(1,3,'herbe',0),(1,4,'herbe',0),(1,5,'herbe',0),(1,6,'herbe',0),(1,7,'herbe',0),(1,8,'herbe',0),(1,9,'herbe',0),(1,10,'herbe',0),(1,11,'herbe',0),(1,12,'herbe',0),(1,13,'herbe',0),(1,14,'herbe',0),(1,15,'herbe',0),(1,16,'herbe',0),(1,17,'herbe',0),(1,18,'herbe',0),(1,19,'herbe',0),(1,20,'herbe',0),(1,21,'herbe',0),(1,22,'herbe',0),(1,23,'herbe',0),(1,24,'herbe',0),(1,25,'herbe',0),(1,26,'herbe',0),(1,27,'herbe',0),(1,28,'herbe',0),(1,29,'herbe',0),(1,30,'herbe',0),(1,31,'herbe',0),(1,32,'herbe',0),(1,33,'herbe',0),(1,34,'herbe',0),(1,35,'herbe',0),(1,36,'herbe',0),(1,37,'herbe',0),(1,38,'herbe',0),(1,39,'herbe',0),(1,40,'herbe',0),(1,41,'herbe',0),(1,42,'herbe',0),(1,43,'herbe',0),(1,44,'herbe',0),(1,45,'herbe',0),(1,46,'herbe',0),(1,47,'herbe',0),(1,48,'herbe',0),(1,49,'herbe',0),(2,0,'herbe',0),(2,1,'herbe',0),(2,2,'herbe',0),(2,3,'herbe',0),(2,4,'herbe',0),(2,5,'herbe',0),(2,6,'herbe',0),(2,7,'herbe',0),(2,8,'herbe',0),(2,9,'herbe',0),(2,10,'herbe',0),(2,11,'herbe',0),(2,12,'herbe',0),(2,13,'herbe',0),(2,14,'herbe',0),(2,15,'herbe',0),(2,16,'herbe',0),(2,17,'herbe',0),(2,18,'herbe',0),(2,19,'herbe',0),(2,20,'herbe',0),(2,21,'herbe',0),(2,22,'herbe',0),(2,23,'herbe',0),(2,24,'herbe',0),(2,25,'herbe',0),(2,26,'herbe',0),(2,27,'herbe',0),(2,28,'herbe',0),(2,29,'herbe',0),(2,30,'herbe',0),(2,31,'herbe',0),(2,32,'herbe',0),(2,33,'herbe',0),(2,34,'herbe',0),(2,35,'herbe',0),(2,36,'herbe',0),(2,37,'herbe',0),(2,38,'herbe',0),(2,39,'herbe',0),(2,40,'herbe',0),(2,41,'herbe',0),(2,42,'herbe',0),(2,43,'herbe',0),(2,44,'herbe',0),(2,45,'herbe',0),(2,46,'herbe',0),(2,47,'herbe',0),(2,48,'herbe',0),(2,49,'herbe',0),(3,0,'herbe',0),(3,1,'herbe',0),(3,2,'herbe',0),(3,3,'herbe',0),(3,4,'herbe',0),(3,5,'herbe',0),(3,6,'herbe',0),(3,7,'herbe',0),(3,8,'herbe',0),(3,9,'herbe',0),(3,10,'herbe',0),(3,11,'herbe',0),(3,12,'herbe',0),(3,13,'herbe',0),(3,14,'herbe',0),(3,15,'herbe',0),(3,16,'herbe',0),(3,17,'herbe',0),(3,18,'herbe',0),(3,19,'herbe',0),(3,20,'herbe',0),(3,21,'herbe',0),(3,22,'herbe',0),(3,23,'herbe',0),(3,24,'herbe',0),(3,25,'herbe',0),(3,26,'herbe',0),(3,27,'herbe',0),(3,28,'herbe',0),(3,29,'herbe',0),(3,30,'herbe',0),(3,31,'herbe',0),(3,32,'herbe',0),(3,33,'herbe',0),(3,34,'herbe',0),(3,35,'herbe',0),(3,36,'herbe',0),(3,37,'herbe',0),(3,38,'herbe',0),(3,39,'herbe',0),(3,40,'herbe',0),(3,41,'herbe',0),(3,42,'herbe',0),(3,43,'herbe',0),(3,44,'herbe',0),(3,45,'herbe',0),(3,46,'herbe',0),(3,47,'herbe',0),(3,48,'herbe',0),(3,49,'herbe',0),(4,0,'herbe',0),(4,1,'herbe',0),(4,2,'herbe',0),(4,3,'herbe',1),(4,4,'herbe',0),(4,5,'herbe',0),(4,6,'herbe',0),(4,7,'herbe',0),(4,8,'herbe',0),(4,9,'herbe',0),(4,10,'herbe',0),(4,11,'herbe',0),(4,12,'herbe',0),(4,13,'herbe',0),(4,14,'herbe',0),(4,15,'herbe',0),(4,16,'herbe',0),(4,17,'herbe',0),(4,18,'herbe',0),(4,19,'herbe',0),(4,20,'herbe',0),(4,21,'herbe',0),(4,22,'herbe',0),(4,23,'herbe',0),(4,24,'herbe',0),(4,25,'herbe',0),(4,26,'herbe',0),(4,27,'herbe',0),(4,28,'herbe',0),(4,29,'herbe',0),(4,30,'herbe',0),(4,31,'herbe',0),(4,32,'herbe',0),(4,33,'herbe',0),(4,34,'herbe',0),(4,35,'herbe',0),(4,36,'herbe',0),(4,37,'herbe',0),(4,38,'herbe',0),(4,39,'herbe',0),(4,40,'herbe',0),(4,41,'herbe',0),(4,42,'herbe',0),(4,43,'herbe',0),(4,44,'herbe',0),(4,45,'herbe',0),(4,46,'herbe',0),(4,47,'herbe',0),(4,48,'herbe',0),(4,49,'herbe',0),(5,0,'herbe',0),(5,1,'herbe',0),(5,2,'herbe',0),(5,3,'herbe',0),(5,4,'herbe',0),(5,5,'herbe',0),(5,6,'herbe',0),(5,7,'herbe',0),(5,8,'herbe',0),(5,9,'herbe',0),(5,10,'herbe',0),(5,11,'herbe',0),(5,12,'herbe',0),(5,13,'herbe',0),(5,14,'herbe',0),(5,15,'herbe',0),(5,16,'herbe',0),(5,17,'herbe',0),(5,18,'herbe',0),(5,19,'herbe',0),(5,20,'herbe',0),(5,21,'herbe',0),(5,22,'herbe',0),(5,23,'herbe',0),(5,24,'herbe',0),(5,25,'herbe',0),(5,26,'herbe',0),(5,27,'herbe',0),(5,28,'herbe',0),(5,29,'herbe',0),(5,30,'herbe',0),(5,31,'herbe',0),(5,32,'herbe',0),(5,33,'herbe',0),(5,34,'herbe',0),(5,35,'herbe',0),(5,36,'herbe',0),(5,37,'herbe',0),(5,38,'herbe',0),(5,39,'herbe',0),(5,40,'herbe',0),(5,41,'herbe',0),(5,42,'herbe',0),(5,43,'herbe',0),(5,44,'herbe',0),(5,45,'herbe',0),(5,46,'herbe',0),(5,47,'herbe',0),(5,48,'herbe',0),(5,49,'herbe',0),(6,0,'herbe',0),(6,1,'herbe',0),(6,2,'herbe',0),(6,3,'herbe',0),(6,4,'herbe',0),(6,5,'herbe',0),(6,6,'herbe',0),(6,7,'herbe',0),(6,8,'herbe',0),(6,9,'herbe',0),(6,10,'herbe',0),(6,11,'herbe',0),(6,12,'herbe',0),(6,13,'herbe',0),(6,14,'herbe',0),(6,15,'herbe',0),(6,16,'herbe',0),(6,17,'herbe',0),(6,18,'herbe',0),(6,19,'herbe',0),(6,20,'herbe',0),(6,21,'herbe',0),(6,22,'herbe',0),(6,23,'herbe',0),(6,24,'herbe',0),(6,25,'herbe',0),(6,26,'herbe',0),(6,27,'herbe',0),(6,28,'herbe',0),(6,29,'herbe',0),(6,30,'herbe',0),(6,31,'herbe',0),(6,32,'herbe',0),(6,33,'herbe',0),(6,34,'herbe',0),(6,35,'herbe',0),(6,36,'herbe',0),(6,37,'herbe',0),(6,38,'herbe',0),(6,39,'herbe',0),(6,40,'herbe',0),(6,41,'herbe',0),(6,42,'herbe',0),(6,43,'herbe',0),(6,44,'herbe',0),(6,45,'herbe',0),(6,46,'herbe',0),(6,47,'herbe',0),(6,48,'herbe',0),(6,49,'herbe',0),(7,0,'herbe',0),(7,1,'herbe',0),(7,2,'herbe',0),(7,3,'herbe',0),(7,4,'herbe',0),(7,5,'herbe',0),(7,6,'herbe',0),(7,7,'herbe',0),(7,8,'herbe',0),(7,9,'herbe',0),(7,10,'herbe',0),(7,11,'herbe',0),(7,12,'herbe',0),(7,13,'herbe',0),(7,14,'herbe',0),(7,15,'herbe',0),(7,16,'herbe',0),(7,17,'herbe',0),(7,18,'herbe',0),(7,19,'herbe',0),(7,20,'herbe',0),(7,21,'herbe',0),(7,22,'herbe',0),(7,23,'herbe',0),(7,24,'herbe',0),(7,25,'herbe',0),(7,26,'herbe',0),(7,27,'herbe',0),(7,28,'herbe',0),(7,29,'herbe',0),(7,30,'herbe',0),(7,31,'herbe',0),(7,32,'herbe',0),(7,33,'herbe',0),(7,34,'herbe',0),(7,35,'herbe',0),(7,36,'herbe',0),(7,37,'herbe',0),(7,38,'herbe',0),(7,39,'herbe',0),(7,40,'herbe',0),(7,41,'herbe',0),(7,42,'herbe',0),(7,43,'herbe',0),(7,44,'herbe',0),(7,45,'herbe',0),(7,46,'herbe',0),(7,47,'herbe',0),(7,48,'herbe',0),(7,49,'herbe',0),(8,0,'herbe',0),(8,1,'herbe',0),(8,2,'herbe',0),(8,3,'herbe',0),(8,4,'herbe',0),(8,5,'herbe',0),(8,6,'herbe',0),(8,7,'paves',0),(8,8,'paves',0),(8,9,'herbe',0),(8,10,'herbe',0),(8,11,'herbe',0),(8,12,'herbe',0),(8,13,'herbe',0),(8,14,'herbe',0),(8,15,'herbe',0),(8,16,'herbe',0),(8,17,'herbe',0),(8,18,'herbe',0),(8,19,'herbe',0),(8,20,'herbe',0),(8,21,'herbe',0),(8,22,'herbe',0),(8,23,'herbe',0),(8,24,'herbe',0),(8,25,'herbe',0),(8,26,'herbe',0),(8,27,'herbe',0),(8,28,'herbe',0),(8,29,'herbe',0),(8,30,'herbe',0),(8,31,'herbe',0),(8,32,'herbe',0),(8,33,'herbe',0),(8,34,'herbe',0),(8,35,'herbe',0),(8,36,'herbe',0),(8,37,'herbe',0),(8,38,'herbe',0),(8,39,'herbe',0),(8,40,'herbe',0),(8,41,'herbe',0),(8,42,'herbe',0),(8,43,'herbe',0),(8,44,'herbe',0),(8,45,'herbe',0),(8,46,'herbe',0),(8,47,'herbe',0),(8,48,'herbe',0),(8,49,'herbe',0),(9,0,'herbe',0),(9,1,'herbe',0),(9,2,'herbe',0),(9,3,'herbe',0),(9,4,'herbe',0),(9,5,'herbe',0),(9,6,'paves',0),(9,7,'herbe',0),(9,8,'paves',0),(9,9,'herbe',1),(9,10,'herbe',0),(9,11,'herbe',0),(9,12,'herbe',0),(9,13,'herbe',0),(9,14,'herbe',0),(9,15,'herbe',1),(9,16,'herbe',0),(9,17,'herbe',0),(9,18,'herbe',0),(9,19,'herbe',0),(9,20,'herbe',0),(9,21,'herbe',0),(9,22,'herbe',0),(9,23,'herbe',0),(9,24,'herbe',0),(9,25,'herbe',0),(9,26,'herbe',0),(9,27,'herbe',0),(9,28,'herbe',0),(9,29,'herbe',0),(9,30,'herbe',0),(9,31,'herbe',0),(9,32,'herbe',0),(9,33,'herbe',0),(9,34,'herbe',0),(9,35,'herbe',0),(9,36,'herbe',0),(9,37,'herbe',0),(9,38,'herbe',0),(9,39,'herbe',0),(9,40,'herbe',0),(9,41,'herbe',0),(9,42,'herbe',0),(9,43,'herbe',0),(9,44,'herbe',0),(9,45,'herbe',0),(9,46,'herbe',0),(9,47,'herbe',0),(9,48,'herbe',0),(9,49,'herbe',0),(10,0,'herbe',0),(10,1,'herbe',0),(10,2,'herbe',0),(10,3,'herbe',0),(10,4,'herbe',0),(10,5,'herbe',0),(10,6,'herbe',0),(10,7,'paves',0),(10,8,'paves',0),(10,9,'herbe',0),(10,10,'herbe',0),(10,11,'herbe',0),(10,12,'herbe',0),(10,13,'herbe',0),(10,14,'herbe',0),(10,15,'herbe',0),(10,16,'herbe',0),(10,17,'herbe',0),(10,18,'herbe',0),(10,19,'herbe',0),(10,20,'herbe',0),(10,21,'herbe',0),(10,22,'herbe',0),(10,23,'herbe',0),(10,24,'herbe',0),(10,25,'herbe',0),(10,26,'herbe',0),(10,27,'herbe',0),(10,28,'herbe',0),(10,29,'herbe',0),(10,30,'herbe',0),(10,31,'herbe',0),(10,32,'herbe',0),(10,33,'herbe',0),(10,34,'herbe',0),(10,35,'herbe',0),(10,36,'herbe',0),(10,37,'herbe',0),(10,38,'herbe',0),(10,39,'herbe',0),(10,40,'herbe',0),(10,41,'herbe',0),(10,42,'herbe',0),(10,43,'herbe',0),(10,44,'herbe',0),(10,45,'herbe',0),(10,46,'herbe',0),(10,47,'herbe',0),(10,48,'herbe',0),(10,49,'herbe',0),(11,0,'herbe',0),(11,1,'herbe',0),(11,2,'herbe',0),(11,3,'herbe',0),(11,4,'herbe',0),(11,5,'herbe',0),(11,6,'paves',0),(11,7,'paves',0),(11,8,'paves',0),(11,9,'herbe',0),(11,10,'herbe',0),(11,11,'herbe',0),(11,12,'herbe',0),(11,13,'herbe',0),(11,14,'herbe',0),(11,15,'herbe',0),(11,16,'herbe',0),(11,17,'herbe',0),(11,18,'herbe',0),(11,19,'herbe',0),(11,20,'herbe',0),(11,21,'herbe',0),(11,22,'herbe',0),(11,23,'herbe',0),(11,24,'herbe',0),(11,25,'herbe',0),(11,26,'herbe',0),(11,27,'herbe',0),(11,28,'herbe',0),(11,29,'herbe',0),(11,30,'herbe',0),(11,31,'herbe',0),(11,32,'herbe',0),(11,33,'herbe',0),(11,34,'herbe',0),(11,35,'herbe',0),(11,36,'herbe',0),(11,37,'herbe',0),(11,38,'herbe',0),(11,39,'herbe',0),(11,40,'herbe',0),(11,41,'herbe',0),(11,42,'herbe',0),(11,43,'herbe',0),(11,44,'herbe',0),(11,45,'herbe',0),(11,46,'herbe',0),(11,47,'herbe',0),(11,48,'herbe',0),(11,49,'herbe',0),(12,0,'herbe',0),(12,1,'herbe',0),(12,2,'herbe',0),(12,3,'herbe',0),(12,4,'herbe',0),(12,5,'herbe',0),(12,6,'herbe',0),(12,7,'herbe',0),(12,8,'herbe',0),(12,9,'herbe',0),(12,10,'herbe',0),(12,11,'herbe',0),(12,12,'herbe',0),(12,13,'herbe',0),(12,14,'herbe',0),(12,15,'herbe',0),(12,16,'herbe',0),(12,17,'herbe',0),(12,18,'herbe',0),(12,19,'herbe',0),(12,20,'herbe',0),(12,21,'herbe',0),(12,22,'herbe',0),(12,23,'herbe',0),(12,24,'herbe',0),(12,25,'herbe',0),(12,26,'herbe',0),(12,27,'herbe',0),(12,28,'herbe',0),(12,29,'herbe',0),(12,30,'herbe',0),(12,31,'herbe',0),(12,32,'herbe',0),(12,33,'herbe',0),(12,34,'herbe',0),(12,35,'herbe',0),(12,36,'herbe',0),(12,37,'herbe',0),(12,38,'herbe',0),(12,39,'herbe',0),(12,40,'herbe',0),(12,41,'herbe',0),(12,42,'herbe',0),(12,43,'herbe',0),(12,44,'herbe',0),(12,45,'herbe',0),(12,46,'herbe',0),(12,47,'herbe',0),(12,48,'herbe',0),(12,49,'herbe',0),(13,0,'herbe',0),(13,1,'herbe',0),(13,2,'herbe',0),(13,3,'herbe',0),(13,4,'herbe',0),(13,5,'herbe',0),(13,6,'herbe',0),(13,7,'herbe',0),(13,8,'herbe',0),(13,9,'herbe',0),(13,10,'herbe',0),(13,11,'herbe',0),(13,12,'herbe',0),(13,13,'herbe',0),(13,14,'herbe',0),(13,15,'herbe',0),(13,16,'herbe',0),(13,17,'herbe',0),(13,18,'herbe',0),(13,19,'herbe',0),(13,20,'herbe',0),(13,21,'herbe',0),(13,22,'herbe',0),(13,23,'herbe',0),(13,24,'herbe',0),(13,25,'herbe',0),(13,26,'herbe',0),(13,27,'herbe',0),(13,28,'herbe',0),(13,29,'herbe',0),(13,30,'herbe',0),(13,31,'herbe',0),(13,32,'herbe',0),(13,33,'herbe',0),(13,34,'herbe',0),(13,35,'herbe',0),(13,36,'herbe',0),(13,37,'herbe',0),(13,38,'herbe',0),(13,39,'herbe',0),(13,40,'herbe',0),(13,41,'herbe',0),(13,42,'herbe',0),(13,43,'herbe',0),(13,44,'herbe',0),(13,45,'herbe',0),(13,46,'herbe',0),(13,47,'herbe',0),(13,48,'herbe',0),(13,49,'herbe',0),(14,0,'herbe',0),(14,1,'herbe',0),(14,2,'herbe',0),(14,3,'herbe',0),(14,4,'herbe',0),(14,5,'herbe',0),(14,6,'herbe',0),(14,7,'herbe',0),(14,8,'herbe',0),(14,9,'herbe',0),(14,10,'herbe',0),(14,11,'herbe',0),(14,12,'herbe',0),(14,13,'herbe',0),(14,14,'herbe',0),(14,15,'herbe',0),(14,16,'herbe',0),(14,17,'herbe',0),(14,18,'herbe',0),(14,19,'herbe',0),(14,20,'herbe',0),(14,21,'herbe',0),(14,22,'herbe',0),(14,23,'herbe',0),(14,24,'herbe',0),(14,25,'herbe',0),(14,26,'herbe',0),(14,27,'herbe',0),(14,28,'herbe',0),(14,29,'herbe',0),(14,30,'herbe',0),(14,31,'herbe',0),(14,32,'herbe',0),(14,33,'herbe',0),(14,34,'herbe',0),(14,35,'herbe',0),(14,36,'herbe',0),(14,37,'herbe',0),(14,38,'herbe',0),(14,39,'herbe',0),(14,40,'herbe',0),(14,41,'herbe',0),(14,42,'herbe',0),(14,43,'herbe',0),(14,44,'herbe',0),(14,45,'herbe',0),(14,46,'herbe',0),(14,47,'herbe',0),(14,48,'herbe',0),(14,49,'herbe',0),(15,0,'herbe',0),(15,1,'herbe',0),(15,2,'herbe',0),(15,3,'herbe',0),(15,4,'herbe',0),(15,5,'herbe',0),(15,6,'herbe',0),(15,7,'herbe',0),(15,8,'herbe',0),(15,9,'herbe',0),(15,10,'herbe',0),(15,11,'herbe',0),(15,12,'herbe',0),(15,13,'herbe',0),(15,14,'herbe',0),(15,15,'herbe',0),(15,16,'herbe',0),(15,17,'herbe',0),(15,18,'herbe',0),(15,19,'herbe',0),(15,20,'herbe',0),(15,21,'herbe',0),(15,22,'herbe',0),(15,23,'herbe',0),(15,24,'herbe',0),(15,25,'herbe',0),(15,26,'herbe',0),(15,27,'herbe',0),(15,28,'herbe',0),(15,29,'herbe',0),(15,30,'herbe',0),(15,31,'herbe',0),(15,32,'herbe',0),(15,33,'herbe',0),(15,34,'herbe',0),(15,35,'herbe',0),(15,36,'herbe',0),(15,37,'herbe',0),(15,38,'herbe',0),(15,39,'herbe',0),(15,40,'herbe',0),(15,41,'herbe',0),(15,42,'herbe',0),(15,43,'herbe',0),(15,44,'herbe',0),(15,45,'herbe',0),(15,46,'herbe',0),(15,47,'herbe',0),(15,48,'herbe',0),(15,49,'herbe',0),(16,0,'herbe',0),(16,1,'herbe',0),(16,2,'herbe',0),(16,3,'herbe',0),(16,4,'herbe',0),(16,5,'herbe',0),(16,6,'herbe',0),(16,7,'herbe',0),(16,8,'herbe',0),(16,9,'herbe',0),(16,10,'herbe',0),(16,11,'herbe',0),(16,12,'herbe',0),(16,13,'herbe',0),(16,14,'herbe',0),(16,15,'herbe',0),(16,16,'herbe',0),(16,17,'herbe',0),(16,18,'herbe',0),(16,19,'herbe',0),(16,20,'herbe',0),(16,21,'herbe',0),(16,22,'herbe',0),(16,23,'herbe',0),(16,24,'herbe',0),(16,25,'herbe',0),(16,26,'herbe',0),(16,27,'herbe',0),(16,28,'herbe',0),(16,29,'herbe',0),(16,30,'herbe',0),(16,31,'herbe',0),(16,32,'herbe',0),(16,33,'herbe',0),(16,34,'herbe',0),(16,35,'herbe',0),(16,36,'herbe',0),(16,37,'herbe',0),(16,38,'herbe',0),(16,39,'herbe',0),(16,40,'herbe',0),(16,41,'herbe',0),(16,42,'herbe',0),(16,43,'herbe',0),(16,44,'herbe',0),(16,45,'herbe',0),(16,46,'herbe',0),(16,47,'herbe',0),(16,48,'herbe',0),(16,49,'herbe',0),(17,0,'herbe',0),(17,1,'herbe',0),(17,2,'herbe',0),(17,3,'herbe',0),(17,4,'herbe',0),(17,5,'herbe',0),(17,6,'herbe',0),(17,7,'herbe',0),(17,8,'herbe',0),(17,9,'herbe',0),(17,10,'herbe',0),(17,11,'herbe',0),(17,12,'herbe',0),(17,13,'herbe',0),(17,14,'herbe',0),(17,15,'herbe',0),(17,16,'herbe',0),(17,17,'herbe',0),(17,18,'herbe',0),(17,19,'herbe',0),(17,20,'herbe',0),(17,21,'herbe',0),(17,22,'herbe',0),(17,23,'herbe',0),(17,24,'herbe',0),(17,25,'herbe',0),(17,26,'herbe',0),(17,27,'herbe',0),(17,28,'herbe',0),(17,29,'herbe',0),(17,30,'herbe',0),(17,31,'herbe',0),(17,32,'herbe',0),(17,33,'herbe',0),(17,34,'herbe',0),(17,35,'herbe',0),(17,36,'herbe',0),(17,37,'herbe',0),(17,38,'herbe',0),(17,39,'herbe',0),(17,40,'herbe',0),(17,41,'herbe',0),(17,42,'herbe',0),(17,43,'herbe',0),(17,44,'herbe',0),(17,45,'herbe',0),(17,46,'herbe',0),(17,47,'herbe',0),(17,48,'herbe',0),(17,49,'herbe',0),(18,0,'herbe',0),(18,1,'herbe',0),(18,2,'herbe',0),(18,3,'herbe',0),(18,4,'herbe',0),(18,5,'herbe',0),(18,6,'herbe',0),(18,7,'herbe',0),(18,8,'herbe',0),(18,9,'herbe',0),(18,10,'herbe',0),(18,11,'herbe',0),(18,12,'herbe',0),(18,13,'herbe',0),(18,14,'herbe',0),(18,15,'herbe',0),(18,16,'herbe',0),(18,17,'herbe',0),(18,18,'herbe',0),(18,19,'herbe',0),(18,20,'herbe',0),(18,21,'herbe',0),(18,22,'herbe',0),(18,23,'herbe',0),(18,24,'herbe',0),(18,25,'herbe',0),(18,26,'herbe',0),(18,27,'herbe',0),(18,28,'herbe',0),(18,29,'herbe',0),(18,30,'herbe',0),(18,31,'herbe',0),(18,32,'herbe',0),(18,33,'herbe',0),(18,34,'herbe',0),(18,35,'herbe',0),(18,36,'herbe',0),(18,37,'herbe',0),(18,38,'herbe',0),(18,39,'herbe',0),(18,40,'herbe',0),(18,41,'herbe',0),(18,42,'herbe',0),(18,43,'herbe',0),(18,44,'herbe',0),(18,45,'herbe',0),(18,46,'herbe',0),(18,47,'herbe',0),(18,48,'herbe',0),(18,49,'herbe',0),(19,0,'herbe',0),(19,1,'herbe',0),(19,2,'herbe',0),(19,3,'herbe',0),(19,4,'herbe',0),(19,5,'herbe',0),(19,6,'herbe',0),(19,7,'herbe',0),(19,8,'herbe',0),(19,9,'herbe',0),(19,10,'herbe',0),(19,11,'herbe',0),(19,12,'herbe',0),(19,13,'herbe',0),(19,14,'herbe',0),(19,15,'herbe',0),(19,16,'herbe',0),(19,17,'herbe',0),(19,18,'herbe',0),(19,19,'herbe',0),(19,20,'herbe',0),(19,21,'herbe',0),(19,22,'herbe',0),(19,23,'herbe',0),(19,24,'herbe',0),(19,25,'herbe',0),(19,26,'herbe',0),(19,27,'herbe',0),(19,28,'herbe',0),(19,29,'herbe',0),(19,30,'herbe',0),(19,31,'herbe',0),(19,32,'herbe',0),(19,33,'herbe',0),(19,34,'herbe',0),(19,35,'herbe',0),(19,36,'herbe',0),(19,37,'herbe',0),(19,38,'herbe',0),(19,39,'herbe',0),(19,40,'herbe',0),(19,41,'herbe',0),(19,42,'herbe',0),(19,43,'herbe',0),(19,44,'herbe',0),(19,45,'herbe',0),(19,46,'herbe',0),(19,47,'herbe',0),(19,48,'herbe',0),(19,49,'herbe',0),(20,0,'herbe',0),(20,1,'herbe',0),(20,2,'herbe',0),(20,3,'herbe',0),(20,4,'herbe',0),(20,5,'herbe',0),(20,6,'herbe',0),(20,7,'herbe',0),(20,8,'herbe',0),(20,9,'herbe',0),(20,10,'herbe',0),(20,11,'herbe',0),(20,12,'herbe',0),(20,13,'herbe',0),(20,14,'herbe',0),(20,15,'herbe',0),(20,16,'herbe',0),(20,17,'herbe',0),(20,18,'herbe',0),(20,19,'herbe',0),(20,20,'herbe',0),(20,21,'herbe',0),(20,22,'herbe',0),(20,23,'herbe',0),(20,24,'herbe',0),(20,25,'herbe',0),(20,26,'herbe',0),(20,27,'herbe',0),(20,28,'herbe',0),(20,29,'herbe',0),(20,30,'herbe',0),(20,31,'herbe',0),(20,32,'herbe',0),(20,33,'herbe',0),(20,34,'herbe',0),(20,35,'herbe',0),(20,36,'herbe',0),(20,37,'herbe',0),(20,38,'herbe',0),(20,39,'herbe',0),(20,40,'herbe',0),(20,41,'herbe',0),(20,42,'herbe',0),(20,43,'herbe',0),(20,44,'herbe',0),(20,45,'herbe',0),(20,46,'herbe',0),(20,47,'herbe',0),(20,48,'herbe',0),(20,49,'herbe',0),(21,0,'herbe',0),(21,1,'herbe',0),(21,2,'herbe',0),(21,3,'herbe',0),(21,4,'herbe',0),(21,5,'herbe',0),(21,6,'herbe',0),(21,7,'herbe',0),(21,8,'herbe',0),(21,9,'herbe',0),(21,10,'herbe',0),(21,11,'herbe',0),(21,12,'herbe',0),(21,13,'herbe',0),(21,14,'herbe',0),(21,15,'herbe',0),(21,16,'herbe',0),(21,17,'herbe',0),(21,18,'herbe',0),(21,19,'herbe',0),(21,20,'herbe',0),(21,21,'herbe',0),(21,22,'herbe',0),(21,23,'herbe',0),(21,24,'herbe',0),(21,25,'herbe',0),(21,26,'herbe',0),(21,27,'herbe',0),(21,28,'herbe',0),(21,29,'herbe',0),(21,30,'herbe',0),(21,31,'herbe',0),(21,32,'herbe',0),(21,33,'herbe',0),(21,34,'herbe',0),(21,35,'herbe',0),(21,36,'herbe',0),(21,37,'herbe',0),(21,38,'herbe',0),(21,39,'herbe',0),(21,40,'herbe',0),(21,41,'herbe',0),(21,42,'herbe',0),(21,43,'herbe',0),(21,44,'herbe',0),(21,45,'herbe',0),(21,46,'herbe',0),(21,47,'herbe',0),(21,48,'herbe',0),(21,49,'herbe',0),(22,0,'herbe',0),(22,1,'herbe',0),(22,2,'herbe',0),(22,3,'herbe',0),(22,4,'herbe',0),(22,5,'herbe',0),(22,6,'herbe',0),(22,7,'herbe',0),(22,8,'herbe',0),(22,9,'herbe',0),(22,10,'herbe',0),(22,11,'herbe',0),(22,12,'herbe',0),(22,13,'herbe',0),(22,14,'herbe',0),(22,15,'herbe',0),(22,16,'herbe',0),(22,17,'herbe',0),(22,18,'herbe',0),(22,19,'herbe',0),(22,20,'herbe',0),(22,21,'herbe',0),(22,22,'herbe',0),(22,23,'herbe',0),(22,24,'herbe',0),(22,25,'herbe',0),(22,26,'herbe',0),(22,27,'herbe',0),(22,28,'herbe',0),(22,29,'herbe',0),(22,30,'herbe',0),(22,31,'herbe',0),(22,32,'herbe',0),(22,33,'herbe',0),(22,34,'herbe',0),(22,35,'herbe',0),(22,36,'herbe',0),(22,37,'herbe',0),(22,38,'herbe',0),(22,39,'herbe',0),(22,40,'herbe',0),(22,41,'herbe',0),(22,42,'herbe',0),(22,43,'herbe',0),(22,44,'herbe',0),(22,45,'herbe',0),(22,46,'herbe',0),(22,47,'herbe',0),(22,48,'herbe',0),(22,49,'herbe',0),(23,0,'herbe',0),(23,1,'herbe',0),(23,2,'herbe',0),(23,3,'herbe',0),(23,4,'herbe',0),(23,5,'herbe',0),(23,6,'herbe',0),(23,7,'herbe',0),(23,8,'herbe',0),(23,9,'herbe',0),(23,10,'herbe',0),(23,11,'herbe',0),(23,12,'herbe',0),(23,13,'herbe',0),(23,14,'herbe',0),(23,15,'herbe',0),(23,16,'herbe',0),(23,17,'herbe',0),(23,18,'herbe',0),(23,19,'herbe',0),(23,20,'herbe',0),(23,21,'herbe',0),(23,22,'herbe',0),(23,23,'herbe',0),(23,24,'herbe',0),(23,25,'herbe',0),(23,26,'herbe',0),(23,27,'herbe',0),(23,28,'herbe',0),(23,29,'herbe',0),(23,30,'herbe',1),(23,31,'herbe',0),(23,32,'herbe',0),(23,33,'herbe',0),(23,34,'herbe',0),(23,35,'herbe',0),(23,36,'herbe',0),(23,37,'herbe',0),(23,38,'herbe',0),(23,39,'herbe',0),(23,40,'herbe',0),(23,41,'herbe',0),(23,42,'herbe',0),(23,43,'herbe',0),(23,44,'herbe',0),(23,45,'herbe',0),(23,46,'herbe',0),(23,47,'herbe',0),(23,48,'herbe',0),(23,49,'herbe',0),(24,0,'herbe',0),(24,1,'herbe',0),(24,2,'herbe',0),(24,3,'herbe',0),(24,4,'herbe',0),(24,5,'herbe',0),(24,6,'herbe',0),(24,7,'herbe',0),(24,8,'herbe',0),(24,9,'herbe',0),(24,10,'herbe',0),(24,11,'herbe',0),(24,12,'herbe',0),(24,13,'herbe',0),(24,14,'herbe',0),(24,15,'herbe',0),(24,16,'herbe',0),(24,17,'herbe',0),(24,18,'herbe',0),(24,19,'herbe',0),(24,20,'herbe',0),(24,21,'herbe',0),(24,22,'herbe',0),(24,23,'herbe',0),(24,24,'herbe',0),(24,25,'herbe',0),(24,26,'herbe',0),(24,27,'herbe',0),(24,28,'herbe',0),(24,29,'herbe',0),(24,30,'herbe',0),(24,31,'herbe',0),(24,32,'herbe',0),(24,33,'herbe',0),(24,34,'herbe',0),(24,35,'herbe',0),(24,36,'herbe',0),(24,37,'herbe',0),(24,38,'herbe',0),(24,39,'herbe',0),(24,40,'herbe',0),(24,41,'herbe',0),(24,42,'herbe',0),(24,43,'herbe',0),(24,44,'herbe',0),(24,45,'herbe',0),(24,46,'herbe',0),(24,47,'herbe',0),(24,48,'herbe',0),(24,49,'herbe',0),(25,0,'herbe',0),(25,1,'herbe',0),(25,2,'herbe',0),(25,3,'herbe',0),(25,4,'herbe',0),(25,5,'herbe',0),(25,6,'herbe',0),(25,7,'herbe',0),(25,8,'herbe',0),(25,9,'herbe',0),(25,10,'herbe',0),(25,11,'herbe',0),(25,12,'herbe',0),(25,13,'herbe',0),(25,14,'herbe',0),(25,15,'herbe',0),(25,16,'herbe',0),(25,17,'herbe',0),(25,18,'herbe',0),(25,19,'herbe',0),(25,20,'herbe',0),(25,21,'herbe',0),(25,22,'herbe',0),(25,23,'herbe',0),(25,24,'herbe',0),(25,25,'herbe',0),(25,26,'herbe',0),(25,27,'herbe',0),(25,28,'herbe',0),(25,29,'herbe',0),(25,30,'herbe',0),(25,31,'herbe',0),(25,32,'herbe',0),(25,33,'herbe',0),(25,34,'herbe',0),(25,35,'herbe',0),(25,36,'herbe',0),(25,37,'herbe',0),(25,38,'herbe',0),(25,39,'herbe',0),(25,40,'herbe',0),(25,41,'herbe',0),(25,42,'herbe',0),(25,43,'herbe',0),(25,44,'herbe',0),(25,45,'herbe',0),(25,46,'herbe',0),(25,47,'herbe',0),(25,48,'herbe',0),(25,49,'herbe',0),(26,0,'herbe',0),(26,1,'herbe',0),(26,2,'herbe',0),(26,3,'herbe',0),(26,4,'herbe',0),(26,5,'herbe',0),(26,6,'herbe',0),(26,7,'herbe',0),(26,8,'herbe',0),(26,9,'herbe',0),(26,10,'herbe',0),(26,11,'herbe',0),(26,12,'herbe',0),(26,13,'herbe',0),(26,14,'herbe',0),(26,15,'herbe',0),(26,16,'herbe',0),(26,17,'herbe',0),(26,18,'herbe',0),(26,19,'herbe',0),(26,20,'herbe',0),(26,21,'herbe',0),(26,22,'herbe',0),(26,23,'herbe',0),(26,24,'herbe',0),(26,25,'herbe',0),(26,26,'herbe',0),(26,27,'herbe',0),(26,28,'herbe',0),(26,29,'herbe',0),(26,30,'herbe',0),(26,31,'herbe',0),(26,32,'herbe',0),(26,33,'herbe',0),(26,34,'herbe',0),(26,35,'herbe',0),(26,36,'herbe',0),(26,37,'herbe',0),(26,38,'herbe',0),(26,39,'herbe',0),(26,40,'herbe',0),(26,41,'herbe',0),(26,42,'herbe',0),(26,43,'herbe',0),(26,44,'herbe',0),(26,45,'herbe',0),(26,46,'herbe',0),(26,47,'herbe',0),(26,48,'herbe',0),(26,49,'herbe',0),(27,0,'herbe',0),(27,1,'herbe',0),(27,2,'herbe',0),(27,3,'herbe',0),(27,4,'herbe',0),(27,5,'herbe',0),(27,6,'herbe',0),(27,7,'herbe',0),(27,8,'herbe',0),(27,9,'herbe',0),(27,10,'herbe',0),(27,11,'herbe',0),(27,12,'herbe',0),(27,13,'herbe',0),(27,14,'herbe',0),(27,15,'herbe',0),(27,16,'herbe',0),(27,17,'herbe',0),(27,18,'herbe',0),(27,19,'herbe',0),(27,20,'herbe',0),(27,21,'herbe',0),(27,22,'herbe',0),(27,23,'herbe',0),(27,24,'herbe',0),(27,25,'herbe',0),(27,26,'herbe',0),(27,27,'herbe',0),(27,28,'herbe',0),(27,29,'herbe',0),(27,30,'herbe',0),(27,31,'herbe',0),(27,32,'herbe',0),(27,33,'herbe',0),(27,34,'herbe',0),(27,35,'herbe',0),(27,36,'herbe',0),(27,37,'herbe',0),(27,38,'herbe',0),(27,39,'herbe',0),(27,40,'herbe',0),(27,41,'herbe',0),(27,42,'herbe',0),(27,43,'herbe',0),(27,44,'herbe',0),(27,45,'herbe',0),(27,46,'herbe',0),(27,47,'herbe',0),(27,48,'herbe',0),(27,49,'herbe',0),(28,0,'herbe',0),(28,1,'herbe',0),(28,2,'herbe',0),(28,3,'herbe',0),(28,4,'herbe',0),(28,5,'herbe',0),(28,6,'herbe',0),(28,7,'herbe',0),(28,8,'herbe',0),(28,9,'herbe',0),(28,10,'herbe',0),(28,11,'herbe',0),(28,12,'herbe',0),(28,13,'herbe',0),(28,14,'herbe',0),(28,15,'herbe',0),(28,16,'herbe',0),(28,17,'herbe',0),(28,18,'herbe',0),(28,19,'herbe',0),(28,20,'herbe',0),(28,21,'herbe',0),(28,22,'herbe',0),(28,23,'herbe',0),(28,24,'herbe',0),(28,25,'herbe',0),(28,26,'herbe',0),(28,27,'herbe',0),(28,28,'herbe',0),(28,29,'herbe',0),(28,30,'herbe',0),(28,31,'herbe',0),(28,32,'herbe',0),(28,33,'herbe',0),(28,34,'herbe',0),(28,35,'herbe',0),(28,36,'herbe',0),(28,37,'herbe',0),(28,38,'herbe',0),(28,39,'herbe',0),(28,40,'herbe',0),(28,41,'herbe',0),(28,42,'herbe',0),(28,43,'herbe',0),(28,44,'herbe',0),(28,45,'herbe',0),(28,46,'herbe',0),(28,47,'herbe',0),(28,48,'herbe',0),(28,49,'herbe',0),(29,0,'herbe',0),(29,1,'herbe',0),(29,2,'herbe',0),(29,3,'herbe',0),(29,4,'herbe',0),(29,5,'herbe',0),(29,6,'herbe',0),(29,7,'herbe',0),(29,8,'herbe',0),(29,9,'herbe',0),(29,10,'herbe',0),(29,11,'herbe',0),(29,12,'herbe',0),(29,13,'herbe',0),(29,14,'herbe',0),(29,15,'herbe',0),(29,16,'herbe',0),(29,17,'herbe',0),(29,18,'herbe',0),(29,19,'herbe',0),(29,20,'herbe',0),(29,21,'herbe',0),(29,22,'herbe',0),(29,23,'herbe',0),(29,24,'herbe',0),(29,25,'herbe',0),(29,26,'herbe',0),(29,27,'herbe',0),(29,28,'herbe',0),(29,29,'herbe',0),(29,30,'herbe',0),(29,31,'herbe',0),(29,32,'herbe',0),(29,33,'herbe',0),(29,34,'herbe',0),(29,35,'herbe',0),(29,36,'herbe',0),(29,37,'herbe',0),(29,38,'herbe',0),(29,39,'herbe',0),(29,40,'herbe',0),(29,41,'herbe',0),(29,42,'herbe',0),(29,43,'herbe',0),(29,44,'herbe',0),(29,45,'herbe',0),(29,46,'herbe',0),(29,47,'herbe',0),(29,48,'herbe',0),(29,49,'herbe',0),(30,0,'herbe',0),(30,1,'herbe',0),(30,2,'herbe',0),(30,3,'herbe',0),(30,4,'herbe',0),(30,5,'herbe',0),(30,6,'herbe',0),(30,7,'herbe',0),(30,8,'herbe',0),(30,9,'herbe',0),(30,10,'herbe',0),(30,11,'herbe',0),(30,12,'herbe',0),(30,13,'herbe',0),(30,14,'herbe',0),(30,15,'herbe',0),(30,16,'herbe',0),(30,17,'herbe',0),(30,18,'herbe',0),(30,19,'herbe',0),(30,20,'herbe',0),(30,21,'herbe',0),(30,22,'herbe',0),(30,23,'herbe',0),(30,24,'herbe',0),(30,25,'herbe',0),(30,26,'herbe',0),(30,27,'herbe',0),(30,28,'herbe',0),(30,29,'herbe',0),(30,30,'herbe',0),(30,31,'herbe',0),(30,32,'herbe',0),(30,33,'herbe',0),(30,34,'herbe',0),(30,35,'herbe',0),(30,36,'herbe',0),(30,37,'herbe',0),(30,38,'herbe',0),(30,39,'herbe',0),(30,40,'herbe',0),(30,41,'herbe',0),(30,42,'herbe',0),(30,43,'herbe',0),(30,44,'herbe',0),(30,45,'herbe',0),(30,46,'herbe',0),(30,47,'herbe',0),(30,48,'herbe',0),(30,49,'herbe',0),(31,0,'herbe',0),(31,1,'herbe',0),(31,2,'herbe',0),(31,3,'herbe',0),(31,4,'herbe',0),(31,5,'herbe',0),(31,6,'herbe',0),(31,7,'herbe',0),(31,8,'herbe',0),(31,9,'herbe',0),(31,10,'herbe',0),(31,11,'herbe',0),(31,12,'herbe',0),(31,13,'herbe',0),(31,14,'herbe',0),(31,15,'herbe',0),(31,16,'herbe',0),(31,17,'herbe',0),(31,18,'herbe',0),(31,19,'herbe',0),(31,20,'herbe',0),(31,21,'herbe',0),(31,22,'herbe',0),(31,23,'herbe',0),(31,24,'herbe',0),(31,25,'herbe',0),(31,26,'herbe',0),(31,27,'herbe',0),(31,28,'herbe',0),(31,29,'herbe',0),(31,30,'herbe',0),(31,31,'herbe',0),(31,32,'herbe',0),(31,33,'herbe',0),(31,34,'herbe',0),(31,35,'herbe',0),(31,36,'herbe',0),(31,37,'herbe',0),(31,38,'herbe',0),(31,39,'herbe',0),(31,40,'herbe',0),(31,41,'herbe',0),(31,42,'herbe',0),(31,43,'herbe',0),(31,44,'herbe',0),(31,45,'herbe',0),(31,46,'herbe',0),(31,47,'herbe',0),(31,48,'herbe',0),(31,49,'herbe',0),(32,0,'herbe',0),(32,1,'herbe',0),(32,2,'herbe',0),(32,3,'herbe',0),(32,4,'herbe',0),(32,5,'herbe',0),(32,6,'herbe',0),(32,7,'herbe',0),(32,8,'herbe',0),(32,9,'herbe',0),(32,10,'herbe',0),(32,11,'herbe',0),(32,12,'herbe',0),(32,13,'herbe',0),(32,14,'herbe',0),(32,15,'herbe',0),(32,16,'herbe',0),(32,17,'herbe',0),(32,18,'herbe',0),(32,19,'herbe',0),(32,20,'herbe',0),(32,21,'herbe',0),(32,22,'herbe',0),(32,23,'herbe',0),(32,24,'herbe',0),(32,25,'herbe',0),(32,26,'herbe',0),(32,27,'herbe',0),(32,28,'herbe',0),(32,29,'herbe',0),(32,30,'herbe',0),(32,31,'herbe',0),(32,32,'herbe',0),(32,33,'herbe',0),(32,34,'herbe',0),(32,35,'herbe',0),(32,36,'herbe',0),(32,37,'herbe',0),(32,38,'herbe',0),(32,39,'herbe',0),(32,40,'herbe',0),(32,41,'herbe',0),(32,42,'herbe',0),(32,43,'herbe',0),(32,44,'herbe',0),(32,45,'herbe',0),(32,46,'herbe',0),(32,47,'herbe',0),(32,48,'herbe',0),(32,49,'herbe',0),(33,0,'herbe',0),(33,1,'herbe',0),(33,2,'herbe',0),(33,3,'herbe',0),(33,4,'herbe',0),(33,5,'herbe',0),(33,6,'herbe',0),(33,7,'herbe',0),(33,8,'herbe',0),(33,9,'herbe',0),(33,10,'herbe',0),(33,11,'herbe',0),(33,12,'herbe',0),(33,13,'herbe',0),(33,14,'herbe',0),(33,15,'herbe',0),(33,16,'herbe',0),(33,17,'herbe',0),(33,18,'herbe',0),(33,19,'herbe',0),(33,20,'herbe',0),(33,21,'herbe',0),(33,22,'herbe',0),(33,23,'herbe',0),(33,24,'herbe',0),(33,25,'herbe',0),(33,26,'herbe',0),(33,27,'herbe',0),(33,28,'herbe',0),(33,29,'herbe',0),(33,30,'herbe',0),(33,31,'herbe',0),(33,32,'herbe',0),(33,33,'herbe',0),(33,34,'herbe',0),(33,35,'herbe',0),(33,36,'herbe',0),(33,37,'herbe',0),(33,38,'herbe',0),(33,39,'herbe',0),(33,40,'herbe',0),(33,41,'herbe',0),(33,42,'herbe',0),(33,43,'herbe',0),(33,44,'herbe',0),(33,45,'herbe',0),(33,46,'herbe',0),(33,47,'herbe',0),(33,48,'herbe',0),(33,49,'herbe',0),(34,0,'herbe',0),(34,1,'herbe',0),(34,2,'herbe',0),(34,3,'herbe',0),(34,4,'herbe',0),(34,5,'herbe',0),(34,6,'herbe',0),(34,7,'herbe',0),(34,8,'herbe',0),(34,9,'herbe',0),(34,10,'herbe',0),(34,11,'herbe',0),(34,12,'herbe',0),(34,13,'herbe',0),(34,14,'herbe',0),(34,15,'herbe',0),(34,16,'herbe',0),(34,17,'herbe',0),(34,18,'herbe',0),(34,19,'herbe',0),(34,20,'herbe',0),(34,21,'herbe',0),(34,22,'herbe',0),(34,23,'herbe',0),(34,24,'herbe',0),(34,25,'herbe',0),(34,26,'herbe',0),(34,27,'herbe',0),(34,28,'herbe',0),(34,29,'herbe',0),(34,30,'herbe',0),(34,31,'herbe',0),(34,32,'herbe',0),(34,33,'herbe',0),(34,34,'herbe',0),(34,35,'herbe',0),(34,36,'herbe',0),(34,37,'herbe',0),(34,38,'herbe',0),(34,39,'herbe',0),(34,40,'herbe',0),(34,41,'herbe',0),(34,42,'herbe',0),(34,43,'herbe',0),(34,44,'herbe',0),(34,45,'herbe',0),(34,46,'herbe',0),(34,47,'herbe',0),(34,48,'herbe',0),(34,49,'herbe',0),(35,0,'herbe',0),(35,1,'herbe',0),(35,2,'herbe',0),(35,3,'herbe',0),(35,4,'herbe',0),(35,5,'herbe',0),(35,6,'herbe',0),(35,7,'herbe',0),(35,8,'herbe',0),(35,9,'herbe',0),(35,10,'herbe',0),(35,11,'herbe',0),(35,12,'herbe',0),(35,13,'herbe',0),(35,14,'herbe',0),(35,15,'herbe',0),(35,16,'herbe',0),(35,17,'herbe',0),(35,18,'herbe',0),(35,19,'herbe',0),(35,20,'herbe',0),(35,21,'herbe',0),(35,22,'herbe',0),(35,23,'herbe',0),(35,24,'herbe',0),(35,25,'herbe',0),(35,26,'herbe',0),(35,27,'herbe',0),(35,28,'herbe',0),(35,29,'herbe',1),(35,30,'herbe',0),(35,31,'herbe',0),(35,32,'herbe',0),(35,33,'herbe',0),(35,34,'herbe',0),(35,35,'herbe',0),(35,36,'herbe',0),(35,37,'herbe',0),(35,38,'herbe',0),(35,39,'herbe',0),(35,40,'herbe',0),(35,41,'herbe',0),(35,42,'herbe',0),(35,43,'herbe',0),(35,44,'herbe',0),(35,45,'herbe',0),(35,46,'herbe',0),(35,47,'herbe',0),(35,48,'herbe',0),(35,49,'herbe',0),(36,0,'herbe',0),(36,1,'herbe',0),(36,2,'herbe',0),(36,3,'herbe',0),(36,4,'herbe',0),(36,5,'herbe',0),(36,6,'herbe',0),(36,7,'herbe',0),(36,8,'herbe',0),(36,9,'herbe',0),(36,10,'herbe',0),(36,11,'herbe',0),(36,12,'herbe',0),(36,13,'herbe',0),(36,14,'herbe',0),(36,15,'herbe',0),(36,16,'herbe',0),(36,17,'herbe',0),(36,18,'herbe',0),(36,19,'herbe',0),(36,20,'herbe',0),(36,21,'herbe',0),(36,22,'herbe',0),(36,23,'herbe',0),(36,24,'herbe',0),(36,25,'herbe',0),(36,26,'herbe',0),(36,27,'herbe',0),(36,28,'herbe',0),(36,29,'herbe',0),(36,30,'herbe',0),(36,31,'herbe',0),(36,32,'herbe',0),(36,33,'herbe',0),(36,34,'herbe',0),(36,35,'herbe',0),(36,36,'herbe',0),(36,37,'herbe',0),(36,38,'herbe',0),(36,39,'herbe',0),(36,40,'herbe',0),(36,41,'herbe',0),(36,42,'herbe',0),(36,43,'herbe',0),(36,44,'herbe',0),(36,45,'herbe',0),(36,46,'herbe',0),(36,47,'herbe',0),(36,48,'herbe',0),(36,49,'herbe',0),(37,0,'herbe',0),(37,1,'herbe',0),(37,2,'herbe',0),(37,3,'herbe',0),(37,4,'herbe',0),(37,5,'herbe',0),(37,6,'herbe',0),(37,7,'herbe',0),(37,8,'herbe',0),(37,9,'herbe',0),(37,10,'herbe',0),(37,11,'herbe',0),(37,12,'herbe',0),(37,13,'herbe',0),(37,14,'herbe',0),(37,15,'herbe',0),(37,16,'herbe',0),(37,17,'herbe',0),(37,18,'herbe',0),(37,19,'herbe',0),(37,20,'herbe',0),(37,21,'herbe',0),(37,22,'herbe',0),(37,23,'herbe',0),(37,24,'herbe',0),(37,25,'herbe',0),(37,26,'herbe',0),(37,27,'herbe',0),(37,28,'herbe',0),(37,29,'herbe',0),(37,30,'herbe',0),(37,31,'herbe',0),(37,32,'herbe',0),(37,33,'herbe',0),(37,34,'herbe',0),(37,35,'herbe',0),(37,36,'herbe',0),(37,37,'herbe',0),(37,38,'herbe',0),(37,39,'herbe',0),(37,40,'herbe',0),(37,41,'herbe',0),(37,42,'herbe',0),(37,43,'herbe',0),(37,44,'herbe',0),(37,45,'herbe',0),(37,46,'herbe',0),(37,47,'herbe',0),(37,48,'herbe',0),(37,49,'herbe',0),(38,0,'herbe',0),(38,1,'herbe',0),(38,2,'herbe',0),(38,3,'herbe',0),(38,4,'herbe',0),(38,5,'herbe',0),(38,6,'herbe',0),(38,7,'herbe',0),(38,8,'herbe',0),(38,9,'herbe',0),(38,10,'herbe',0),(38,11,'herbe',0),(38,12,'herbe',0),(38,13,'herbe',0),(38,14,'herbe',0),(38,15,'herbe',0),(38,16,'herbe',0),(38,17,'herbe',0),(38,18,'herbe',0),(38,19,'herbe',0),(38,20,'herbe',0),(38,21,'herbe',0),(38,22,'herbe',0),(38,23,'herbe',0),(38,24,'herbe',0),(38,25,'herbe',0),(38,26,'herbe',0),(38,27,'herbe',0),(38,28,'herbe',0),(38,29,'herbe',0),(38,30,'herbe',0),(38,31,'herbe',0),(38,32,'herbe',0),(38,33,'herbe',0),(38,34,'herbe',0),(38,35,'herbe',0),(38,36,'herbe',0),(38,37,'herbe',0),(38,38,'herbe',0),(38,39,'herbe',0),(38,40,'herbe',0),(38,41,'herbe',0),(38,42,'herbe',0),(38,43,'herbe',0),(38,44,'herbe',0),(38,45,'herbe',0),(38,46,'herbe',0),(38,47,'herbe',0),(38,48,'herbe',0),(38,49,'herbe',0),(39,0,'herbe',0),(39,1,'herbe',0),(39,2,'herbe',0),(39,3,'herbe',0),(39,4,'herbe',0),(39,5,'herbe',0),(39,6,'herbe',0),(39,7,'herbe',0),(39,8,'herbe',0),(39,9,'herbe',0),(39,10,'herbe',0),(39,11,'herbe',0),(39,12,'herbe',0),(39,13,'herbe',0),(39,14,'herbe',0),(39,15,'herbe',0),(39,16,'herbe',0),(39,17,'herbe',0),(39,18,'herbe',0),(39,19,'herbe',0),(39,20,'herbe',0),(39,21,'herbe',0),(39,22,'herbe',0),(39,23,'herbe',0),(39,24,'herbe',0),(39,25,'herbe',0),(39,26,'herbe',0),(39,27,'herbe',0),(39,28,'herbe',0),(39,29,'herbe',0),(39,30,'herbe',0),(39,31,'herbe',0),(39,32,'herbe',0),(39,33,'herbe',0),(39,34,'herbe',0),(39,35,'herbe',0),(39,36,'herbe',0),(39,37,'herbe',0),(39,38,'herbe',0),(39,39,'herbe',0),(39,40,'herbe',0),(39,41,'herbe',0),(39,42,'herbe',0),(39,43,'herbe',0),(39,44,'herbe',0),(39,45,'herbe',0),(39,46,'herbe',0),(39,47,'herbe',0),(39,48,'herbe',0),(39,49,'herbe',0),(40,0,'herbe',0),(40,1,'herbe',0),(40,2,'herbe',0),(40,3,'herbe',0),(40,4,'herbe',0),(40,5,'herbe',0),(40,6,'herbe',0),(40,7,'herbe',0),(40,8,'herbe',0),(40,9,'herbe',0),(40,10,'herbe',0),(40,11,'herbe',0),(40,12,'herbe',0),(40,13,'herbe',0),(40,14,'herbe',0),(40,15,'herbe',0),(40,16,'herbe',0),(40,17,'herbe',0),(40,18,'herbe',0),(40,19,'herbe',0),(40,20,'herbe',0),(40,21,'herbe',0),(40,22,'herbe',0),(40,23,'herbe',0),(40,24,'herbe',0),(40,25,'herbe',0),(40,26,'herbe',0),(40,27,'herbe',0),(40,28,'herbe',0),(40,29,'herbe',0),(40,30,'herbe',0),(40,31,'herbe',0),(40,32,'herbe',0),(40,33,'herbe',0),(40,34,'herbe',0),(40,35,'herbe',0),(40,36,'herbe',0),(40,37,'herbe',0),(40,38,'herbe',0),(40,39,'herbe',0),(40,40,'herbe',0),(40,41,'herbe',0),(40,42,'herbe',0),(40,43,'herbe',0),(40,44,'herbe',0),(40,45,'herbe',0),(40,46,'herbe',0),(40,47,'herbe',0),(40,48,'herbe',0),(40,49,'herbe',0),(41,0,'herbe',0),(41,1,'herbe',0),(41,2,'herbe',0),(41,3,'herbe',0),(41,4,'herbe',0),(41,5,'herbe',0),(41,6,'herbe',0),(41,7,'herbe',0),(41,8,'herbe',0),(41,9,'herbe',0),(41,10,'herbe',0),(41,11,'herbe',0),(41,12,'herbe',0),(41,13,'herbe',0),(41,14,'herbe',0),(41,15,'herbe',0),(41,16,'herbe',0),(41,17,'herbe',0),(41,18,'herbe',0),(41,19,'herbe',0),(41,20,'herbe',0),(41,21,'herbe',0),(41,22,'herbe',0),(41,23,'herbe',0),(41,24,'herbe',0),(41,25,'herbe',0),(41,26,'herbe',0),(41,27,'herbe',0),(41,28,'herbe',0),(41,29,'herbe',0),(41,30,'herbe',0),(41,31,'herbe',0),(41,32,'herbe',0),(41,33,'herbe',0),(41,34,'herbe',0),(41,35,'herbe',0),(41,36,'herbe',0),(41,37,'herbe',0),(41,38,'herbe',0),(41,39,'herbe',0),(41,40,'herbe',0),(41,41,'herbe',0),(41,42,'herbe',0),(41,43,'herbe',0),(41,44,'herbe',0),(41,45,'herbe',0),(41,46,'herbe',0),(41,47,'herbe',0),(41,48,'herbe',0),(41,49,'herbe',0),(42,0,'herbe',0),(42,1,'herbe',0),(42,2,'herbe',0),(42,3,'herbe',0),(42,4,'herbe',0),(42,5,'herbe',0),(42,6,'herbe',0),(42,7,'herbe',0),(42,8,'herbe',0),(42,9,'herbe',0),(42,10,'herbe',0),(42,11,'herbe',0),(42,12,'herbe',0),(42,13,'herbe',0),(42,14,'herbe',0),(42,15,'herbe',0),(42,16,'herbe',0),(42,17,'herbe',0),(42,18,'herbe',0),(42,19,'herbe',0),(42,20,'herbe',0),(42,21,'herbe',0),(42,22,'herbe',0),(42,23,'herbe',0),(42,24,'herbe',0),(42,25,'herbe',0),(42,26,'herbe',0),(42,27,'herbe',0),(42,28,'herbe',0),(42,29,'herbe',0),(42,30,'herbe',0),(42,31,'herbe',0),(42,32,'herbe',0),(42,33,'herbe',0),(42,34,'herbe',0),(42,35,'herbe',0),(42,36,'herbe',0),(42,37,'herbe',0),(42,38,'herbe',0),(42,39,'herbe',0),(42,40,'herbe',0),(42,41,'herbe',0),(42,42,'herbe',0),(42,43,'herbe',0),(42,44,'herbe',0),(42,45,'herbe',0),(42,46,'herbe',0),(42,47,'herbe',0),(42,48,'herbe',0),(42,49,'herbe',0),(43,0,'herbe',0),(43,1,'herbe',0),(43,2,'herbe',0),(43,3,'herbe',0),(43,4,'herbe',0),(43,5,'herbe',0),(43,6,'herbe',0),(43,7,'herbe',0),(43,8,'herbe',0),(43,9,'herbe',0),(43,10,'herbe',0),(43,11,'herbe',0),(43,12,'herbe',0),(43,13,'herbe',0),(43,14,'herbe',0),(43,15,'herbe',0),(43,16,'herbe',0),(43,17,'herbe',0),(43,18,'herbe',0),(43,19,'herbe',0),(43,20,'herbe',0),(43,21,'herbe',0),(43,22,'herbe',0),(43,23,'herbe',0),(43,24,'herbe',0),(43,25,'herbe',0),(43,26,'herbe',0),(43,27,'herbe',0),(43,28,'herbe',0),(43,29,'herbe',0),(43,30,'herbe',0),(43,31,'herbe',0),(43,32,'herbe',0),(43,33,'herbe',0),(43,34,'herbe',0),(43,35,'herbe',0),(43,36,'herbe',0),(43,37,'herbe',0),(43,38,'herbe',0),(43,39,'herbe',0),(43,40,'herbe',0),(43,41,'herbe',0),(43,42,'herbe',0),(43,43,'herbe',0),(43,44,'herbe',0),(43,45,'herbe',0),(43,46,'herbe',0),(43,47,'herbe',0),(43,48,'herbe',0),(43,49,'herbe',0),(44,0,'herbe',0),(44,1,'herbe',0),(44,2,'herbe',0),(44,3,'herbe',0),(44,4,'herbe',0),(44,5,'herbe',0),(44,6,'herbe',0),(44,7,'herbe',0),(44,8,'herbe',0),(44,9,'herbe',0),(44,10,'herbe',0),(44,11,'herbe',0),(44,12,'herbe',0),(44,13,'herbe',0),(44,14,'herbe',0),(44,15,'herbe',0),(44,16,'herbe',0),(44,17,'herbe',0),(44,18,'herbe',0),(44,19,'herbe',0),(44,20,'herbe',0),(44,21,'herbe',0),(44,22,'herbe',0),(44,23,'herbe',0),(44,24,'herbe',0),(44,25,'herbe',0),(44,26,'herbe',0),(44,27,'herbe',0),(44,28,'herbe',0),(44,29,'herbe',0),(44,30,'herbe',0),(44,31,'herbe',0),(44,32,'herbe',0),(44,33,'herbe',0),(44,34,'herbe',0),(44,35,'herbe',0),(44,36,'herbe',0),(44,37,'herbe',0),(44,38,'herbe',0),(44,39,'herbe',0),(44,40,'herbe',0),(44,41,'herbe',0),(44,42,'herbe',0),(44,43,'herbe',0),(44,44,'herbe',0),(44,45,'herbe',0),(44,46,'herbe',0),(44,47,'herbe',0),(44,48,'herbe',0),(44,49,'herbe',0),(45,0,'herbe',0),(45,1,'herbe',0),(45,2,'herbe',0),(45,3,'herbe',0),(45,4,'herbe',0),(45,5,'herbe',0),(45,6,'herbe',0),(45,7,'herbe',0),(45,8,'herbe',0),(45,9,'herbe',0),(45,10,'herbe',0),(45,11,'herbe',0),(45,12,'herbe',0),(45,13,'herbe',0),(45,14,'herbe',0),(45,15,'herbe',0),(45,16,'herbe',0),(45,17,'herbe',0),(45,18,'herbe',0),(45,19,'herbe',0),(45,20,'herbe',0),(45,21,'herbe',0),(45,22,'herbe',0),(45,23,'herbe',0),(45,24,'herbe',0),(45,25,'herbe',0),(45,26,'herbe',0),(45,27,'herbe',0),(45,28,'herbe',0),(45,29,'herbe',0),(45,30,'herbe',0),(45,31,'herbe',0),(45,32,'herbe',0),(45,33,'herbe',0),(45,34,'herbe',0),(45,35,'herbe',0),(45,36,'herbe',0),(45,37,'herbe',0),(45,38,'herbe',0),(45,39,'herbe',0),(45,40,'herbe',0),(45,41,'herbe',0),(45,42,'herbe',0),(45,43,'herbe',0),(45,44,'herbe',0),(45,45,'herbe',0),(45,46,'herbe',0),(45,47,'herbe',0),(45,48,'herbe',0),(45,49,'herbe',0),(46,0,'herbe',0),(46,1,'herbe',0),(46,2,'herbe',0),(46,3,'herbe',0),(46,4,'herbe',0),(46,5,'herbe',0),(46,6,'herbe',0),(46,7,'herbe',0),(46,8,'herbe',0),(46,9,'herbe',0),(46,10,'herbe',0),(46,11,'herbe',0),(46,12,'herbe',0),(46,13,'herbe',0),(46,14,'herbe',0),(46,15,'herbe',0),(46,16,'herbe',0),(46,17,'herbe',0),(46,18,'herbe',0),(46,19,'herbe',0),(46,20,'herbe',0),(46,21,'herbe',0),(46,22,'herbe',0),(46,23,'herbe',0),(46,24,'herbe',0),(46,25,'herbe',0),(46,26,'herbe',0),(46,27,'herbe',0),(46,28,'herbe',0),(46,29,'herbe',0),(46,30,'herbe',0),(46,31,'herbe',0),(46,32,'herbe',0),(46,33,'herbe',0),(46,34,'herbe',0),(46,35,'herbe',0),(46,36,'herbe',0),(46,37,'herbe',0),(46,38,'herbe',0),(46,39,'herbe',0),(46,40,'herbe',0),(46,41,'herbe',0),(46,42,'herbe',0),(46,43,'herbe',0),(46,44,'herbe',0),(46,45,'herbe',0),(46,46,'herbe',0),(46,47,'herbe',0),(46,48,'herbe',0),(46,49,'herbe',0),(47,0,'herbe',0),(47,1,'herbe',0),(47,2,'herbe',0),(47,3,'herbe',0),(47,4,'herbe',0),(47,5,'herbe',0),(47,6,'herbe',0),(47,7,'herbe',0),(47,8,'herbe',0),(47,9,'herbe',0),(47,10,'herbe',0),(47,11,'herbe',0),(47,12,'herbe',0),(47,13,'herbe',0),(47,14,'herbe',0),(47,15,'herbe',0),(47,16,'herbe',0),(47,17,'herbe',0),(47,18,'herbe',0),(47,19,'herbe',0),(47,20,'herbe',0),(47,21,'herbe',0),(47,22,'herbe',0),(47,23,'herbe',0),(47,24,'herbe',0),(47,25,'herbe',0),(47,26,'herbe',0),(47,27,'herbe',0),(47,28,'herbe',0),(47,29,'herbe',0),(47,30,'herbe',0),(47,31,'herbe',0),(47,32,'herbe',0),(47,33,'herbe',0),(47,34,'herbe',0),(47,35,'herbe',0),(47,36,'herbe',0),(47,37,'herbe',0),(47,38,'herbe',0),(47,39,'herbe',0),(47,40,'herbe',0),(47,41,'herbe',0),(47,42,'herbe',0),(47,43,'herbe',0),(47,44,'herbe',0),(47,45,'herbe',0),(47,46,'herbe',0),(47,47,'herbe',0),(47,48,'herbe',0),(47,49,'herbe',0),(48,0,'herbe',0),(48,1,'herbe',0),(48,2,'herbe',0),(48,3,'herbe',0),(48,4,'herbe',0),(48,5,'herbe',0),(48,6,'herbe',0),(48,7,'herbe',0),(48,8,'herbe',0),(48,9,'herbe',0),(48,10,'herbe',0),(48,11,'herbe',0),(48,12,'herbe',0),(48,13,'herbe',0),(48,14,'herbe',0),(48,15,'herbe',0),(48,16,'herbe',0),(48,17,'herbe',0),(48,18,'herbe',0),(48,19,'herbe',0),(48,20,'herbe',0),(48,21,'herbe',0),(48,22,'herbe',0),(48,23,'herbe',0),(48,24,'herbe',0),(48,25,'herbe',0),(48,26,'herbe',0),(48,27,'herbe',0),(48,28,'herbe',0),(48,29,'herbe',0),(48,30,'herbe',0),(48,31,'herbe',0),(48,32,'herbe',0),(48,33,'herbe',0),(48,34,'herbe',0),(48,35,'herbe',0),(48,36,'herbe',0),(48,37,'herbe',0),(48,38,'herbe',0),(48,39,'herbe',0),(48,40,'herbe',0),(48,41,'herbe',0),(48,42,'herbe',0),(48,43,'herbe',0),(48,44,'herbe',0),(48,45,'herbe',0),(48,46,'herbe',0),(48,47,'herbe',0),(48,48,'herbe',0),(48,49,'herbe',0),(49,0,'herbe',0),(49,1,'herbe',0),(49,2,'herbe',0),(49,3,'herbe',0),(49,4,'herbe',0),(49,5,'herbe',0),(49,6,'herbe',0),(49,7,'herbe',0),(49,8,'herbe',0),(49,9,'herbe',0),(49,10,'herbe',0),(49,11,'herbe',0),(49,12,'herbe',0),(49,13,'herbe',0),(49,14,'herbe',0),(49,15,'herbe',0),(49,16,'herbe',0),(49,17,'herbe',0),(49,18,'herbe',0),(49,19,'herbe',0),(49,20,'herbe',0),(49,21,'herbe',0),(49,22,'herbe',0),(49,23,'herbe',0),(49,24,'herbe',0),(49,25,'herbe',0),(49,26,'herbe',0),(49,27,'herbe',0),(49,28,'herbe',0),(49,29,'herbe',0),(49,30,'herbe',0),(49,31,'herbe',0),(49,32,'herbe',0),(49,33,'herbe',0),(49,34,'herbe',0),(49,35,'herbe',0),(49,36,'herbe',0),(49,37,'herbe',0),(49,38,'herbe',0),(49,39,'herbe',0),(49,40,'herbe',0),(49,41,'herbe',0),(49,42,'herbe',0),(49,43,'herbe',0),(49,44,'herbe',0),(49,45,'herbe',0),(49,46,'herbe',0),(49,47,'herbe',0),(49,48,'herbe',0),(49,49,'herbe',0);
/*!40000 ALTER TABLE `map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `map_content`
--

DROP TABLE IF EXISTS `map_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `map_content` (
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `type` varchar(30) NOT NULL,
  PRIMARY KEY (`x`,`y`),
  KEY `type` (`type`),
  CONSTRAINT `map_content_ibfk_1` FOREIGN KEY (`type`) REFERENCES `tiles_map_content` (`nom`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `map_content`
--

LOCK TABLES `map_content` WRITE;
/*!40000 ALTER TABLE `map_content` DISABLE KEYS */;
INSERT INTO `map_content` VALUES (9,16,'arbre'),(15,19,'arbre'),(36,12,'arbre'),(40,29,'arbre'),(42,13,'arbre');
/*!40000 ALTER TABLE `map_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `monster`
--

DROP TABLE IF EXISTS `monster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `monster` (
  `name` varchar(150) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monster`
--

LOCK TABLES `monster` WRITE;
/*!40000 ALTER TABLE `monster` DISABLE KEYS */;
INSERT INTO `monster` VALUES ('bouftou'),('tofu');
/*!40000 ALTER TABLE `monster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personnage`
--

DROP TABLE IF EXISTS `personnage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personnage` (
  `compte` varchar(25) NOT NULL,
  `race` varchar(20) NOT NULL,
  `classe` varchar(20) NOT NULL,
  `name` varchar(30) NOT NULL,
  `posx` double DEFAULT NULL,
  `orientation` varchar(50) DEFAULT NULL,
  `posy` double NOT NULL,
  PRIMARY KEY (`name`),
  KEY `compte` (`compte`),
  KEY `classe` (`classe`),
  KEY `race` (`race`),
  CONSTRAINT `personnage_ibfk_1` FOREIGN KEY (`compte`) REFERENCES `account` (`nom_de_compte`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `personnage_ibfk_2` FOREIGN KEY (`race`) REFERENCES `race` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `personnage_ibfk_3` FOREIGN KEY (`classe`) REFERENCES `classe` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personnage`
--

LOCK TABLES `personnage` WRITE;
/*!40000 ALTER TABLE `personnage` DISABLE KEYS */;
INSERT INTO `personnage` VALUES ('chelendil','Groz','barbare','Choco',37,'b',34),('fazega','Groz','barbare','FaZeGa',33,'h',32),('fazega','Groz','barbare','Memor',20,'b',20);
/*!40000 ALTER TABLE `personnage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pnj`
--

DROP TABLE IF EXISTS `pnj`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pnj` (
  `nom` varchar(100) NOT NULL DEFAULT '',
  `pos_x` int(11) DEFAULT NULL,
  `pos_y` int(11) DEFAULT NULL,
  PRIMARY KEY (`nom`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pnj`
--

LOCK TABLES `pnj` WRITE;
/*!40000 ALTER TABLE `pnj` DISABLE KEYS */;
INSERT INTO `pnj` VALUES ('Well Caume',5,5);
/*!40000 ALTER TABLE `pnj` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pnj_discours`
--

DROP TABLE IF EXISTS `pnj_discours`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pnj_discours` (
  `nom_pnj` varchar(30) DEFAULT NULL,
  `discours` text,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `after_answer` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `after_answer` (`after_answer`),
  KEY `nom_pnj` (`nom_pnj`),
  CONSTRAINT `pnj_discours_ibfk_2` FOREIGN KEY (`after_answer`) REFERENCES `pnj_discours` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pnj_discours_ibfk_3` FOREIGN KEY (`nom_pnj`) REFERENCES `pnj` (`nom`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pnj_discours`
--

LOCK TABLES `pnj_discours` WRITE;
/*!40000 ALTER TABLE `pnj_discours` DISABLE KEYS */;
INSERT INTO `pnj_discours` VALUES ('Well Caume','Bienvenue jeune leimzien !#n#nTu veux partir a l\'aventure, risquer ta vie pour sauver notre bon roi Domo ? Alors c\'est parti, lance toi !',1,NULL),('Well Caume',' Oui, je comprends ton inquietude. Mais trahiras-tu la confiance que t\'as donne ton roi ? J\'imagine que non.#nPars a l\'aventure, tu ne le regretteras pas.#n#nBonne chance !',2,7),('Well Caume','Certes, certes. Mais tente de les m‚langer … de la ciboulette, le gout en est meilleur.#n#nSur ce, bonne chance dans ton aventure !',3,8),('Well Caume','Oui, je suis pret !',6,1),('Well Caume','Euh ...',7,1),('Well Caume','Je n\'aime pas les champignons.',8,1),('Well Caume','Merci, bonne journée !',9,2),('Well Caume','Merci, bonne journée !',10,3);
/*!40000 ALTER TABLE `pnj_discours` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `race`
--

DROP TABLE IF EXISTS `race`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `race` (
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `race`
--

LOCK TABLES `race` WRITE;
/*!40000 ALTER TABLE `race` DISABLE KEYS */;
INSERT INTO `race` VALUES ('Groz');
/*!40000 ALTER TABLE `race` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `race_sort`
--

DROP TABLE IF EXISTS `race_sort`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `race_sort` (
  `race` varchar(20) NOT NULL,
  `sort` varchar(50) NOT NULL,
  PRIMARY KEY (`race`,`sort`),
  KEY `sort` (`sort`),
  CONSTRAINT `race_sort_ibfk_1` FOREIGN KEY (`race`) REFERENCES `race` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `race_sort_ibfk_2` FOREIGN KEY (`sort`) REFERENCES `sorts_race` (`nom`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `race_sort`
--

LOCK TABLES `race_sort` WRITE;
/*!40000 ALTER TABLE `race_sort` DISABLE KEYS */;
INSERT INTO `race_sort` VALUES ('Groz','Bouclier d\'énergie');
/*!40000 ALTER TABLE `race_sort` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sorts_classe`
--

DROP TABLE IF EXISTS `sorts_classe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sorts_classe` (
  `nom` varchar(50) NOT NULL,
  `value_min` int(11) DEFAULT NULL,
  `description` varchar(250) NOT NULL,
  `value_max` int(11) DEFAULT NULL,
  PRIMARY KEY (`nom`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sorts_classe`
--

LOCK TABLES `sorts_classe` WRITE;
/*!40000 ALTER TABLE `sorts_classe` DISABLE KEYS */;
INSERT INTO `sorts_classe` VALUES ('Coup de Poing',50,'Donne un coup de poing puissant à l\'adversaire, qui peut aller jusqu\'à l\'assomer.',52);
/*!40000 ALTER TABLE `sorts_classe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sorts_race`
--

DROP TABLE IF EXISTS `sorts_race`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sorts_race` (
  `nom` varchar(50) NOT NULL,
  `value_min` int(11) DEFAULT NULL,
  `description` varchar(250) NOT NULL,
  `value_max` int(11) DEFAULT NULL,
  PRIMARY KEY (`nom`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sorts_race`
--

LOCK TABLES `sorts_race` WRITE;
/*!40000 ALTER TABLE `sorts_race` DISABLE KEYS */;
INSERT INTO `sorts_race` VALUES ('Bouclier d\'énergie',NULL,'Fais apparaître un bouclier magique autour du lanceur, qui le protège de toute attaque',NULL);
/*!40000 ALTER TABLE `sorts_race` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tiles_map`
--

DROP TABLE IF EXISTS `tiles_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tiles_map` (
  `nom` varchar(30) NOT NULL,
  `image` varchar(250) NOT NULL,
  `collidable` tinyint(1) NOT NULL,
  `base_x` int(11) NOT NULL,
  `base_y` int(11) NOT NULL,
  PRIMARY KEY (`nom`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tiles_map`
--

LOCK TABLES `tiles_map` WRITE;
/*!40000 ALTER TABLE `tiles_map` DISABLE KEYS */;
INSERT INTO `tiles_map` VALUES ('grosses pierres','data/Images/tiles/pierre/grosses pierres.png',0,0,0),('grosses pierres desafectees','data/Images/tiles/pierre/grosses pierres desafectees.png',0,0,0),('herbe','data/Images/tiles/herbe/herbe simple.png',0,2,4),('none','data/Images/tiles/croix.png',0,0,0),('paves','data/Images/tiles/pierre/paves.png',0,0,0);
/*!40000 ALTER TABLE `tiles_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tiles_map_content`
--

DROP TABLE IF EXISTS `tiles_map_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tiles_map_content` (
  `nom` varchar(30) NOT NULL,
  `image` varchar(250) NOT NULL,
  `collidable` tinyint(1) NOT NULL,
  `base_x` int(11) NOT NULL,
  `base_y` int(11) NOT NULL,
  PRIMARY KEY (`nom`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tiles_map_content`
--

LOCK TABLES `tiles_map_content` WRITE;
/*!40000 ALTER TABLE `tiles_map_content` DISABLE KEYS */;
INSERT INTO `tiles_map_content` VALUES ('arbre','data/Images/tiles/arbre.png',0,67,256);
/*!40000 ALTER TABLE `tiles_map_content` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-12-30 14:41:42
