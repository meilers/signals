-- phpMyAdmin SQL Dump
-- version 3.5.0-beta1
-- http://www.phpmyadmin.net
--
-- Host: mysql-shared-02.phpfog.com
-- Generation Time: Aug 06, 2012 at 06:21 AM
-- Server version: 5.5.12-log
-- PHP Version: 5.3.2-1ubuntu4.15

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `signals_phpfogapp_com`
--

-- --------------------------------------------------------

--
-- Table structure for table `places`
--

CREATE TABLE IF NOT EXISTS `places` (
  `placeId` int(11) NOT NULL AUTO_INCREMENT,
  `cityId` int(11) NOT NULL,
  `genre` tinyint(4) NOT NULL,
  `name` varchar(30) NOT NULL,
  `address` varchar(255) NOT NULL,
  `latitude` decimal(10,6) NOT NULL,
  `longitude` decimal(10,6) NOT NULL,
  `ageGroup` tinyint(4) NOT NULL,
  `orientationGroup` tinyint(4) NOT NULL,
  `rating` decimal(10,2) DEFAULT '-1.00',
  
  `superuser` TINYINT DEFAULT 0,,
  `message` varchar(140),
  `website` text,
  
  PRIMARY KEY (`placeId`),
  KEY `fk_place_city` (`cityId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=53 ;

--
-- Dumping data for table `places`
--

INSERT INTO `places` (`placeId`, `cityId`, `genre`, `ageGroup`, `orientationGroup`, `name`, `address`, `latitude`, `longitude`, `rating`) VALUES
(1, 1, 2, 2, 0, 'Plan B', '327 Mont-Royal E', '45.520000', '-73.580000', '-1.00'),
(2, 1, 2, 2, 0, 'Edgar Hypertaverne', '1562 Mont-Royal E', '45.530000', '-73.580000', '-1.00'),
(3, 1, 2, 2, 0, 'Chez Roger Boudoir', '2300 Beaubien E', '45.550000', '-73.590000', '-1.00'),
(4, 1, 2, 2, 0, 'Barraca, Rhumerie & Tapas', '1134 Mont-Royal', '45.530000', '-73.580000', '-1.00'),
(5, 1, 2, 2, 0, 'La Grange', '120 McGill, Vieux-Montréal', '45.500000', '-73.560000', '-1.00'),
(6, 1, 2, 2, 0, 'Le Réservoir', '9 Duluth E', '45.520000', '-73.580000', '-1.00'),
(7, 1, 2, 2, 0, 'Waverly', '5550 boulevard Saint-Laurent', '45.510000', '-73.690000', '-1.00'),
(8, 1, 2, 2, 0, 'Le Confessionnal', '1134 Mont-Royal E', '45.500000', '-73.560000', '-1.00'),
(9, 1, 2, 2, 0, 'La Buvette chez Simone', '4869 av. Du Parc, Vieux-Montréal', '45.520000', '-73.590000', '-1.00'),
(10, 1, 2, 2, 0, 'Philémon', '111 St-Paul O', '45.500000', '-73.550000', '-1.00'),
(11, 1, 2, 2, 0, 'Barmacie Baldwin', '115 Laurier O.', '45.520000', '-73.590000', '-1.00'),
(12, 1, 2, 2, 0, 'Phillips Lounge', '1184, place Phillips', '45.500000', '-73.570000', '-1.00'),
(13, 1, 2, 2, 0, 'Vauvert', '355 McGill', '46.070000', '-73.140000', '-1.00'),
(14, 1, 2, 2, 0, 'Pullman', '3424 av. du Parc', '45.510000', '-73.570000', '-1.00'),
(15, 1, 2, 2, 0, 'Jello Bar', '151 Ontario E', '45.510000', '-73.570000', '-1.00'),
(16, 1, 2, 2, 0, 'Koko', '8 Sherbrooke O', '45.510000', '-73.570000', '-1.00'),
(17, 1, 2, 2, 0, 'L''Assommoir', '211 Notre-Dame O', '45.500000', '-73.560000', '-1.00'),
(18, 1, 1, 2, 0, 'Electric Avenue', '1469 Crescent', '45.500000', '-73.580000', '-1.00'),
(19, 1, 1, 2, 0, 'Cactus', '4461 St-Denis', '45.780000', '-73.160000', '-1.00'),
(20, 1, 1, 2, 0, 'GOGO Lounge', '3682 St Laurent)', '45.500000', '-73.700000', '-1.00'),
(21, 1, 1, 2, 0, 'Stéréo', '858 Ste-Catherine E', '45.520000', '-73.560000', '-1.00'),
(22, 1, 1, 2, 0, 'Salon Daomé', '141 Mont-Royal E', '45.520000', '-73.590000', '-1.00'),
(23, 1, 1, 2, 0, 'Velvet', '426 Saint-Gabriel', '45.510000', '-73.550000', '-1.00'),
(24, 1, 1, 2, 0, 'Club U.N', '390 Notre Dame O.', '45.500000', '-73.560000', '-1.00'),
(25, 1, 1, 2, 0, 'Thursday’s', '1449 Crescent', '45.500000', '-73.580000', '-1.00'),
(26, 1, 1, 2, 0, 'La Tulipe', '4530 Papineau', '45.530000', '-73.580000', '-1.00'),
(27, 1, 1, 2, 0, 'La Porte Rouge', '1834 Mont-Royal E', '45.530000', '-73.570000', '-1.00'),
(28, 1, 3, 2, 0, 'Chez Serge', '5301 St-Laurent', '45.520000', '-73.600000', '-1.00'),
(29, 1, 3, 2, 0, 'Champs', '3956 Saint-Laurent', '45.520000', '-73.580000', '-1.00'),
(30, 1, 3, 2, 0, 'Taverne Normand', '1550 Mont-Royal E', '45.530000', '-73.580000', '-1.00'),
(31, 1, 3, 2, 0, 'Brasserie Rachel-Rachel', '500 Rachel E', '45.520000', '-73.580000', '-1.00'),
(32, 1, 3, 2, 0, '2 Pierrots', '104 St-Paul E', '45.510000', '-73.550000', '-1.00'),
(33, 1, 3, 2, 0, 'Bily Kun', '354 rue Mt-Royal E', '45.520000', '-73.580000', '-1.00'),
(34, 1, 3, 2, 0, 'Laïka', '4040 Boul. St Laurent', '45.520000', '-73.580000', '-1.00'),
(35, 1, 1, 1, 0, 'Club 1234', '1234 de la Montagne', '45.500000', '-73.570000', '-1.00'),
(36, 1, 1, 1, 0, 'Au Diable Vert', '4457 rue Saint-Denis', '45.520000', '-73.580000', '-1.00'),
(37, 1, 1, 1, 0, 'Café Campus', '57 Prince Arthur E', '45.510000', '-73.570000', '-1.00'),
(38, 1, 1, 1, 0, 'Light Ultra Club', '2020 Crescent', '45.500000', '-73.580000', '-1.00'),
(39, 1, 1, 1, 0, 'Tokyo Bar', '3709 Saint-Laurent', '45.510000', '-73.570000', '-1.00'),
(40, 1, 1, 1, 0, 'Muzique', '3781 Saint-Laurent', '45.520000', '-73.580000', '-1.00'),
(41, 1, 1, 1, 0, 'Les Foufounes électriques', '87 St. Catherine E', '45.510000', '-73.560000', '-1.00'),
(42, 1, 1, 1, 0, 'Balroom Bar', '3643 Saint-Laurent', '45.510000', '-73.690000', '-1.00'),
(43, 1, 1, 1, 0, 'Club La Boom', '1254 Stanley', '45.500000', '-73.570000', '-1.00'),
(44, 1, 1, 1, 0, 'Le Belmont', '4483 St-Laurent', '45.520000', '-73.590000', '-1.00'),
(45, 1, 3, 0, 0, 'Dieu Du Ciel', '29 Laurier O', '45.520000', '-73.590000', '-1.00'),
(46, 1, 3, 0, 0, 'Le Sainte Elisabeth', '1412 Sainte-Elisabeth', '45.510000', '-73.560000', '-1.00'),
(47, 1, 3, 0, 0, 'Salon Officiel', '351 Roy E', '45.520000', '-73.570000', '-1.00'),
(48, 1, 3, 0, 0, 'Le Saint-Sulpice', '1680 Saint-Denis', '45.550000', '-73.540000', '-1.00'),
(49, 1, 1, 0, 0, 'Radio Lounge', '3553 Saint-Laurent', '45.510000', '-73.570000', '-1.00'),
(50, 1, 1, 0, 0, 'Radio Lounge', '3553 Saint-Laurent', '37.780000', '-122.400000', '-1.00'),
(51, 1, 2, 0, 0, 'Über', '1011, Fleury Est', '45.557803', '-73.659327', '-1.00');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `places`
--
ALTER TABLE `places`
  ADD CONSTRAINT `fk_place_city` FOREIGN KEY (`cityId`) REFERENCES `cities` (`cityId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
