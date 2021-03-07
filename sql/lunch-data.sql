/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

DROP DATABASE IF EXISTS `lunch`;
CREATE DATABASE IF NOT EXISTS `lunch` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `lunch`;


DROP TABLE IF EXISTS `ingredient`;
CREATE TABLE IF NOT EXISTS `ingredient` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(256) NOT NULL,
  `BEST_BEFORE` date NOT NULL,
  `USE_BY` date NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `TITLE_BEST_BEFORE` (`TITLE`,`BEST_BEFORE`),
  KEY `TITLE_USE_BY` (`TITLE`,`USE_BY`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;


/*!40000 ALTER TABLE `ingredient` DISABLE KEYS */;
REPLACE INTO `ingredient` (`ID`, `TITLE`, `BEST_BEFORE`, `USE_BY`) VALUES
	(1, 'Bacon', '2030-12-31', '2031-01-01'),
	(2, 'Baked Beans', '2030-12-31', '2031-01-01'),
	(3, 'Beetroot', '2030-12-31', '2031-01-01'),
	(4, 'Bread', '2030-12-31', '2031-01-01'),
	(5, 'Butter', '2030-12-31', '2031-01-01'),
	(6, 'Cheese', '2028-01-01', '2029-01-01'),
	(7, 'Cucumber', '2028-12-31', '2029-01-01'),
	(8, 'Eggs', '2028-12-31', '2029-01-01'),
	(9, 'Ham', '2028-12-31', '2029-01-01'),
	(10, 'Hotdog Bun', '2025-12-31', '2026-01-01'),
	(11, 'Ketchup', '2025-12-31', '2026-01-01'),
	(12, 'Lettuce', '2025-12-31', '2026-01-01'),
	(13, 'Milk', '2015-12-31', '2016-01-01'),
	(14, 'Mushrooms', '2015-12-31', '2016-01-01'),
	(15, 'Mustard', '2014-12-31', '2023-01-01'),
	(16, 'Salad Dressing', '2014-12-31', '2023-01-01'),
	(17, 'Sausage', '2014-12-31', '2023-01-01'),
	(18, 'Spinach', '2014-12-31', '2023-01-01'),
	(19, 'Tomato', '2014-12-31', '2023-01-01');
/*!40000 ALTER TABLE `ingredient` ENABLE KEYS */;


DROP TABLE IF EXISTS `recipe`;
CREATE TABLE IF NOT EXISTS `recipe` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(256) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `TITLE` (`TITLE`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*!40000 ALTER TABLE `recipe` DISABLE KEYS */;
REPLACE INTO `recipe` (`ID`, `TITLE`) VALUES
	(1, 'Fry-up'),
	(2, 'Ham and Cheese Toastie'),
	(3, 'Hotdog'),
	(4, 'Omelette'),
	(5, 'Salad');
/*!40000 ALTER TABLE `recipe` ENABLE KEYS */;


DROP TABLE IF EXISTS `recipe_ingredient`;
CREATE TABLE IF NOT EXISTS `recipe_ingredient` (
  `recipe_id` bigint(20) NOT NULL,
  `ingredient_id` bigint(20) NOT NULL,
  UNIQUE KEY `recipe_id_ingredient_id` (`recipe_id`,`ingredient_id`),
  KEY `FK_ingredient_id` (`ingredient_id`),
  CONSTRAINT `FK_ingredient_id` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredient` (`ID`),
  CONSTRAINT `FK_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipe` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40000 ALTER TABLE `recipe_ingredient` DISABLE KEYS */;
REPLACE INTO `recipe_ingredient` (`recipe_id`, `ingredient_id`) VALUES
	(1, 1),
	(1, 2),
	(5, 3),
	(1, 4),
	(2, 4),
	(2, 5),
	(2, 6),
	(5, 7),
	(1, 8),
	(4, 8),
	(2, 9),
	(3, 10),
	(3, 11),
	(5, 12),
	(4, 13),
	(1, 14),
	(4, 14),
	(3, 15),
	(5, 16),
	(1, 17),
	(3, 17),
	(4, 18),
	(5, 19);
/*!40000 ALTER TABLE `recipe_ingredient` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
