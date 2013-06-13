-- phpMyAdmin SQL Dump
-- version 3.5.0-beta1
-- http://www.phpmyadmin.net
--
-- Host: mysql-shared-02.phpfog.com
-- Generation Time: Nov 14, 2012 at 03:01 AM
-- Server version: 5.5.27-log
-- PHP Version: 5.3.2-1ubuntu4.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;



-- --------------------------------------------------------

--
-- Table structure for table `cities`
--

CREATE TABLE IF NOT EXISTS `cities` (
  `cityId` int(11) NOT NULL AUTO_INCREMENT,
  `city` varchar(40) NOT NULL,
  `state` varchar(40) NOT NULL,
  `country` varchar(40) NOT NULL,
  `hasPlaces` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cityId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=17 ;

--
-- Dumping data for table `cities`
--

INSERT INTO `cities` (`cityId`, `city`, `state`, `country`, `hasPlaces`) VALUES
(1, 'Montreal', 'Quebec', 'Canada', 1),
(3, 'Laval', 'Quebec', 'Canada', 0),
(5, 'Quebec', 'Quebec', 'Canada', 0),
(6, 'Gatineau', 'Quebec', 'Canada', 0),
(7, 'Longueuil', 'Quebec', 'Canada', 0),
(8, 'Sherbrooke', 'Quebec', 'Canada', 0),
(9, 'Sao Paulo', 'SP', 'Brazil', 0),
(10, 'St-Marcel', 'QC', 'Canada', 0),
(11, 'St-Marcelin', 'QC', 'Canada', 0),
(12, 'St-Cliclin', 'QC', 'Canada', 0),
(13, 'Jaboticabal', 'Sao Paulo', 'Brasil', 0),
(16, 'Abitibi', 'Quebec', 'Canada', 0);

-- --------------------------------------------------------

--
-- Table structure for table `places`
--

CREATE TABLE IF NOT EXISTS `places` (
  `placeId` int(11) NOT NULL AUTO_INCREMENT,
  `cityId` int(11) NOT NULL,
  `genre` tinyint(4) NOT NULL,
  `ageGroup` tinyint(4) NOT NULL,
  `orientationGroup` tinyint(4) NOT NULL,
  `name` varchar(30) NOT NULL,
  `address` varchar(255) NOT NULL,
  `latitude` decimal(10,6) NOT NULL,
  `longitude` decimal(10,6) NOT NULL,
  `rating` decimal(10,2) DEFAULT '-1.00',
  `superuser` tinyint(4) NOT NULL DEFAULT '0',
  `message` varchar(140) NOT NULL,
  `website` text NOT NULL,
  PRIMARY KEY (`placeId`),
  KEY `fk_place_city` (`cityId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=52 ;

--
-- Dumping data for table `places`
--

INSERT INTO `places` (`placeId`, `cityId`, `genre`, `ageGroup`, `orientationGroup`, `name`, `address`, `latitude`, `longitude`, `rating`, `superuser`, `message`, `website`) VALUES
(1, 1, 2, 2, 0, 'Plan B', '327 Mont-Royal E', '45.520000', '-73.580000', '-1.00', 0, '', ''),
(2, 1, 2, 2, 0, 'Edgar Hypertaverne', '1562 Mont-Royal E', '45.530000', '-73.580000', '-1.00', 0, '', ''),
(3, 1, 2, 2, 0, 'Chez Roger Boudoir', '2300 Beaubien E', '45.550000', '-73.590000', '-1.00', 0, '', ''),
(4, 1, 2, 2, 0, 'Barraca, Rhumerie & Tapas', '1134 Mont-Royal', '45.530000', '-73.580000', '-1.00', 0, '', ''),
(5, 1, 2, 2, 0, 'La Grange', '120 McGill, Vieux-Montréal', '45.500000', '-73.560000', '-1.00', 0, '', ''),
(6, 1, 2, 2, 0, 'Le Réservoir', '9 Duluth E', '45.520000', '-73.580000', '-1.00', 0, '', ''),
(7, 1, 2, 2, 0, 'Waverly', '5550 boulevard Saint-Laurent', '45.510000', '-73.690000', '-1.00', 0, '', ''),
(8, 1, 2, 2, 0, 'Le Confessionnal', '1134 Mont-Royal E', '45.500000', '-73.560000', '-1.00', 0, '', ''),
(9, 1, 2, 2, 0, 'La Buvette chez Simone', '4869 av. Du Parc, Vieux-Montréal', '45.520000', '-73.590000', '-1.00', 0, '', ''),
(10, 1, 2, 2, 0, 'Philémon', '111 St-Paul O', '45.500000', '-73.550000', '-1.00', 0, '', ''),
(11, 1, 2, 2, 0, 'Barmacie Baldwin', '115 Laurier O.', '45.520000', '-73.590000', '-1.00', 0, '', ''),
(12, 1, 2, 2, 0, 'Phillips Lounge', '1184, place Phillips', '45.500000', '-73.570000', '-1.00', 0, '', ''),
(13, 1, 2, 2, 0, 'Vauvert', '355 McGill', '46.070000', '-73.140000', '-1.00', 0, '', ''),
(14, 1, 2, 2, 0, 'Pullman', '3424 av. du Parc', '45.510000', '-73.570000', '-1.00', 0, '', ''),
(15, 1, 2, 2, 0, 'Jello Bar', '151 Ontario E', '45.510000', '-73.570000', '-1.00', 0, '', ''),
(16, 1, 2, 2, 0, 'Koko', '8 Sherbrooke O', '45.510000', '-73.570000', '-1.00', 0, '', ''),
(17, 1, 2, 2, 0, 'L''Assommoir', '211 Notre-Dame O', '45.500000', '-73.560000', '-1.00', 0, '', ''),
(18, 1, 1, 2, 0, 'Electric Avenue', '1469 Crescent', '45.500000', '-73.580000', '-1.00', 0, '', ''),
(19, 1, 1, 2, 0, 'Cactus', '4461 St-Denis', '45.780000', '-73.160000', '-1.00', 0, '', ''),
(20, 1, 1, 2, 0, 'GOGO Lounge', '3682 St Laurent)', '45.500000', '-73.700000', '-1.00', 0, '', ''),
(21, 1, 1, 2, 0, 'Stéréo', '858 Ste-Catherine E', '45.520000', '-73.560000', '-1.00', 0, '', ''),
(22, 1, 1, 2, 0, 'Salon Daomé', '141 Mont-Royal E', '45.520000', '-73.590000', '-1.00', 0, '', ''),
(23, 1, 1, 2, 0, 'Velvet', '426 Saint-Gabriel', '45.510000', '-73.550000', '-1.00', 0, '', ''),
(24, 1, 1, 2, 0, 'Club U.N', '390 Notre Dame O.', '45.500000', '-73.560000', '-1.00', 0, '', ''),
(25, 1, 1, 2, 0, 'Thursday’s', '1449 Crescent', '45.500000', '-73.580000', '-1.00', 0, '', ''),
(26, 1, 1, 2, 0, 'La Tulipe', '4530 Papineau', '45.530000', '-73.580000', '-1.00', 0, '', ''),
(27, 1, 1, 2, 0, 'La Porte Rouge', '1834 Mont-Royal E', '45.530000', '-73.570000', '-1.00', 0, '', ''),
(28, 1, 3, 2, 0, 'Chez Serge', '5301 St-Laurent', '45.520000', '-73.600000', '-1.00', 0, '', ''),
(29, 1, 3, 2, 0, 'Champs', '3956 Saint-Laurent', '45.520000', '-73.580000', '-1.00', 0, '', ''),
(30, 1, 3, 2, 0, 'Taverne Normand', '1550 Mont-Royal E', '45.530000', '-73.580000', '-1.00', 0, '', ''),
(31, 1, 3, 2, 0, 'Brasserie Rachel-Rachel', '500 Rachel E', '45.520000', '-73.580000', '-1.00', 0, '', ''),
(32, 1, 3, 2, 0, '2 Pierrots', '104 St-Paul E', '45.510000', '-73.550000', '-1.00', 0, '', ''),
(33, 1, 3, 2, 0, 'Bily Kun', '354 rue Mt-Royal E', '45.520000', '-73.580000', '-1.00', 0, '', ''),
(34, 1, 3, 2, 0, 'Laïka', '4040 Boul. St Laurent', '45.520000', '-73.580000', '-1.00', 0, '', ''),
(35, 1, 1, 1, 0, 'Club 1234', '1234 de la Montagne', '45.500000', '-73.570000', '-1.00', 0, '', ''),
(36, 1, 1, 1, 0, 'Au Diable Vert', '4457 rue Saint-Denis', '45.520000', '-73.580000', '-1.00', 0, '', ''),
(37, 1, 1, 1, 0, 'Café Campus', '57 Prince Arthur E', '45.510000', '-73.570000', '-1.00', 0, '', ''),
(38, 1, 1, 1, 0, 'Light Ultra Club', '2020 Crescent', '45.500000', '-73.580000', '-1.00', 0, '', ''),
(39, 1, 1, 1, 0, 'Tokyo Bar', '3709 Saint-Laurent', '45.510000', '-73.570000', '-1.00', 0, '', ''),
(40, 1, 1, 1, 0, 'Muzique', '3781 Saint-Laurent', '45.520000', '-73.580000', '-1.00', 0, '', ''),
(41, 1, 1, 1, 0, 'Les Foufounes électriques', '87 St. Catherine E', '45.510000', '-73.560000', '-1.00', 0, '', ''),
(42, 1, 1, 1, 0, 'Balroom Bar', '3643 Saint-Laurent', '45.510000', '-73.690000', '-1.00', 0, '', ''),
(43, 1, 1, 1, 0, 'Club La Boom', '1254 Stanley', '45.500000', '-73.570000', '-1.00', 0, '', ''),
(44, 1, 1, 1, 0, 'Le Belmont', '4483 St-Laurent', '45.520000', '-73.590000', '-1.00', 0, '', ''),
(45, 1, 3, 0, 0, 'Dieu Du Ciel', '29 Laurier O', '45.520000', '-73.590000', '-1.00', 0, '', ''),
(46, 1, 3, 0, 0, 'Le Sainte Elisabeth', '1412 Sainte-Elisabeth', '45.510000', '-73.560000', '-1.00', 0, '', ''),
(47, 1, 3, 0, 0, 'Salon Officiel', '351 Roy E', '45.520000', '-73.570000', '-1.00', 0, '', ''),
(48, 1, 3, 0, 0, 'Le Saint-Sulpice', '1680 Saint-Denis', '45.550000', '-73.540000', '-1.00', 0, '', ''),
(49, 1, 1, 0, 0, 'Radio Lounge', '3553 Saint-Laurent', '45.510000', '-73.570000', '-1.00', 0, '', ''),
(50, 1, 1, 0, 0, 'Radio Lounge', '3553 Saint-Laurent', '37.780000', '-122.400000', '-1.00', 0, '', ''),
(51, 1, 2, 0, 0, 'Über', '1011, Fleury Est', '45.557803', '-73.659327', '-1.00', 0, '', '');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `userId` bigint(20) NOT NULL AUTO_INCREMENT,
  `fid` bigint(20) NOT NULL,
  `email` text NOT NULL,
  `cityId` int(11) NOT NULL,
  `username` varchar(16) NOT NULL,
  `profilePicsUrls` text,
  `sex` tinyint(4) NOT NULL,
  `orientation` tinyint(4) NOT NULL,
  `ageGroup` tinyint(4) NOT NULL,
  `birthday` varchar(10) NOT NULL,
  `tonight` text,
  `hometown` text,
  `work` text,
  `education` text,
  `music` text,
  `movies` text,
  `books` text,
  `television` text,
  `other` text,
  `languages` text,
  `aboutMe` text,
  `quotations` text,
  `wristBlue` tinyint(4) NOT NULL DEFAULT '0',
  `wristRed` tinyint(4) NOT NULL DEFAULT '0',
  `wristYellow` tinyint(4) NOT NULL DEFAULT '0',
  `wristGreen` tinyint(4) NOT NULL DEFAULT '0',
  `w_ageGroup` int(11) DEFAULT '0',
  `w_wristBlue` tinyint(4) NOT NULL DEFAULT '0',
  `w_wristRed` tinyint(4) NOT NULL DEFAULT '0',
  `w_wristYellow` tinyint(4) NOT NULL DEFAULT '0',
  `w_wristGreen` tinyint(4) NOT NULL DEFAULT '0',
  `online` tinyint(4) DEFAULT '1',
  `superuser` tinyint(4) DEFAULT '0',
  `end_superuser` datetime NOT NULL,
  `last_active` datetime NOT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `fid` (`fid`),
  KEY `fk_user_city` (`cityId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=35 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userId`, `fid`, `email`, `cityId`, `username`, `profilePicsUrls`, `sex`, `orientation`, `ageGroup`, `birthday`, `tonight`, `hometown`, `work`, `education`, `music`, `movies`, `books`, `television`, `other`, `languages`, `aboutMe`, `quotations`, `wristBlue`, `wristRed`, `wristYellow`, `wristGreen`, `w_ageGroup`, `w_wristBlue`, `w_wristRed`, `w_wristYellow`, `w_wristGreen`, `online`, `superuser`, `end_superuser`, `last_active`) VALUES
(7, 860270640, '', 1, 'Audrey', '["http:\\/\\/sphotos-a.ak.fbcdn.net\\/hphotos-ak-prn1\\/s720x720\\/60147_10152178141205641_1362021603_n.jpg","http:\\/\\/sphotos-f.ak.fbcdn.net\\/hphotos-ak-ash4\\/s720x720\\/308057_10152117049585641_194156976_n.jpg","http:\\/\\/sphotos-b.ak.fbcdn.net\\/hphotos-ak-ash4\\/423888_10152095782195641_1439324769_n.jpg","http:\\/\\/sphotos-e.ak.fbcdn.net\\/hphotos-ak-ash4\\/426669_10152064409715641_625117829_n.jpg","http:\\/\\/sphotos-f.ak.fbcdn.net\\/hphotos-ak-snc6\\/207208_10152064308825641_350069739_n.jpg","http:\\/\\/sphotos-f.ak.fbcdn.net\\/hphotos-ak-ash3\\/532360_10151868639070641_1094770781_n.jpg","http:\\/\\/sphotos-g.ak.fbcdn.net\\/hphotos-ak-ash3\\/581068_10151846638585641_895794853_n.jpg","http:\\/\\/sphotos-a.ak.fbcdn.net\\/hphotos-ak-ash4\\/318267_10151829594075641_1635344885_n.jpg","http:\\/\\/sphotos-g.ak.fbcdn.net\\/hphotos-ak-snc7\\/581111_10151731403020641_1657648824_n.jpg","http:\\/\\/sphotos-c.ak.fbcdn.net\\/hphotos-ak-snc7\\/484518_10151413176795641_393344212_n.jpg","http:\\/\\/sphotos-h.ak.fbcdn.net\\/hphotos-ak-snc7\\/s720x720\\/421277_10151413175410641_1286879706_n.jpg","http:\\/\\/sphotos-c.ak.fbcdn.net\\/hphotos-ak-snc7\\/405263_10151197426275641_458130302_n.jpg","http:\\/\\/sphotos-c.ak.fbcdn.net\\/hphotos-ak-ash4\\/405709_10151079802135641_1477627799_n.jpg","http:\\/\\/sphotos-a.ak.fbcdn.net\\/hphotos-ak-ash4\\/s720x720\\/374747_10151058446705641_801828079_n.jpg","http:\\/\\/sphotos-d.ak.fbcdn.net\\/hphotos-ak-ash4\\/309686_10150889951720641_1042674954_n.jpg","http:\\/\\/sphotos-h.ak.fbcdn.net\\/hphotos-ak-ash4\\/313286_10150848300125641_2091180493_n.jpg","http:\\/\\/sphotos-g.ak.fbcdn.net\\/hphotos-ak-snc7\\/307893_10150798893975641_1478385132_n.jpg","http:\\/\\/sphotos-f.ak.fbcdn.net\\/hphotos-ak-ash4\\/298875_10150772964250641_2931761_n.jpg","http:\\/\\/sphotos-d.ak.fbcdn.net\\/hphotos-ak-snc6\\/262874_10150747946840641_5789082_n.jpg","http:\\/\\/sphotos-f.ak.fbcdn.net\\/hphotos-ak-snc6\\/285207_10150741061540641_289036_n.jpg","http:\\/\\/sphotos-c.ak.fbcdn.net\\/hphotos-ak-ash4\\/281970_10150736590765641_121588_n.jpg","http:\\/\\/sphotos-h.ak.fbcdn.net\\/hphotos-ak-prn1\\/253359_10150728540910641_1081614_n.jpg","http:\\/\\/sphotos-d.ak.fbcdn.net\\/hphotos-ak-snc6\\/284223_10150723029270641_896159_n.jpg","http:\\/\\/sphotos-h.ak.fbcdn.net\\/hphotos-ak-snc6\\/261650_10150710709740641_144291_n.jpg","http:\\/\\/sphotos-b.ak.fbcdn.net\\/hphotos-ak-snc6\\/264990_10150676840795641_3775103_n.jpg"]', 2, 1, 2, '08/24/1985', 'Going out late tonight!', 'Montreal, Quebec', '[{"wo_e":"Centre hospitalier universitaire Sainte-Justine"}]', '[{"ed_c":"Doc. Neuropsy recherche et intervention","ed_sn":"Universit\\u00e9 de Montr\\u00e9al","ed_t":"College"}]', '[]', '[]', '[]', '[]', '["Premi\\u00e8re Moisson","Le Programme (CISM 89.3FM)","Cr\\u00e9ations Zestine","\\u00c9lectroma Podcast","Martinho da Vila - Canta Canta, Minha Gente","Cr\\u00e9ations SoNan","Studios Vert Prana"]', '[]', '', '', 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, '2012-09-12 00:51:28', '2012-09-12 00:51:28'),
(29, 604443798, '', 1, 'Mikaël', '["http:\\/\\/sphotos-d.ak.fbcdn.net\\/hphotos-ak-ash4\\/249212_10150322760828799_786239_n.jpg","http:\\/\\/sphotos-c.ak.fbcdn.net\\/hphotos-ak-prn1\\/34338_440597673798_4225824_n.jpg","http:\\/\\/sphotos-b.xx.fbcdn.net\\/hphotos-ash4\\/317_34041768798_2055_n.jpg","http:\\/\\/sphotos-a.xx.fbcdn.net\\/hphotos-snc7\\/299_30877293798_405_n.jpg","http:\\/\\/sphotos-c.ak.fbcdn.net\\/hphotos-ak-snc7\\/299_25842548798_7492_n.jpg","http:\\/\\/sphotos-e.ak.fbcdn.net\\/hphotos-ak-snc6\\/222897_5980503798_8571_n.jpg"]', 1, 1, 1, '12/31/1987', 'im yours', '', '[]', '[{"ed_c":"International Business Management","ed_sn":"McGill University","ed_t":"Graduate School"},{"ed_c":"Industrial Engineering","ed_sn":"\\u00c9cole Polytechnique de Montr\\u00e9al","ed_t":"College","ed_y":"2011"}]', '', '', '', '', '', '[]', 'If you can dream it, you can achieve it !\r\n\r\n"Stay Hungry. Stay Foolish." - Steve Jobs', '', 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, '2012-10-04 15:29:32', '2012-10-04 15:29:32'),
(30, 100003677733153, '', 1, 'Cassandra', '["http:\\/\\/sphotos-e.ak.fbcdn.net\\/hphotos-ak-ash4\\/s720x720\\/304569_186002781532312_1067115975_n.jpg","http:\\/\\/sphotos-h.ak.fbcdn.net\\/hphotos-ak-snc6\\/182124_185981231534467_929230469_n.jpg","http:\\/\\/sphotos-h.ak.fbcdn.net\\/hphotos-ak-snc7\\/s720x720\\/391406_185970418202215_594639172_n.jpg","http:\\/\\/sphotos-h.ak.fbcdn.net\\/hphotos-ak-prn1\\/s720x720\\/543287_101389713326953_799047047_n.jpg"]', 2, 1, 2, '06/26/1984', 'I''m partying!', '', '[]', '[{"ed_sn":"\\u00c9cole Polytechnique de Montr\\u00e9al","ed_t":"College","ed_y":"2010"}]', '', '', '', '', '', '[]', '', '', 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, '2012-10-12 01:37:07', '2012-10-12 01:37:07'),
(31, 100003683793267, '', 1, 'Stéphanie', '["http:\\/\\/sphotos-g.ak.fbcdn.net\\/hphotos-ak-ash3\\/555099_101426949990098_23199233_n.jpg"]', 2, 1, 2, '08/05/1985', '', '', '[]', '[{"ed_sn":"Universit\\u00e9 de Montr\\u00e9al","ed_t":"College","ed_y":"2013"}]', '', '', '', '', '', '[]', '', '', 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, '2012-10-12 02:00:07', '2012-10-12 02:00:07'),
(32, 100003667473274, 'michael.eilers.smith@gmail.com', 1, 'Mariève', '["http:\\/\\/sphotos-c.ak.fbcdn.net\\/hphotos-ak-ash3\\/543299_101450639987185_719098604_n.jpg"]', 2, 1, 2, '01/15/1985', 'fufcvujjh', '', '[]', '[{"ed_sn":"McGill University","ed_t":"College","ed_y":"2011"}]', '[]', '[]', '[]', '[]', '[]', '[]', '', '', 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, '2012-10-12 02:03:59', '2012-11-06 05:05:45'),
(34, 580385965, 'omegatai@hotmail.com', 1, 'Michael', '["https:\\/\\/fbcdn-sphotos-g-a.akamaihd.net\\/hphotos-ak-snc7\\/s720x720\\/396051_10151081631345966_705568855_n.jpg","https:\\/\\/fbcdn-sphotos-a-a.akamaihd.net\\/hphotos-ak-snc7\\/580019_10151077285775966_1711408193_n.jpg","https:\\/\\/fbcdn-sphotos-g-a.akamaihd.net\\/hphotos-ak-ash3\\/s720x720\\/553812_10150974701805966_977613468_n.jpg","https:\\/\\/fbcdn-sphotos-g-a.akamaihd.net\\/hphotos-ak-snc7\\/483974_10150944594950966_522305855_n.jpg","https:\\/\\/fbcdn-sphotos-d-a.akamaihd.net\\/hphotos-ak-ash3\\/s720x720\\/539630_10150942473835966_2133087639_n.jpg","https:\\/\\/fbcdn-sphotos-g-a.akamaihd.net\\/hphotos-ak-ash3\\/543681_10150907901085966_1612633449_n.jpg","https:\\/\\/fbcdn-sphotos-h-a.akamaihd.net\\/hphotos-ak-ash3\\/577078_10150903508915966_938962507_n.jpg","https:\\/\\/fbcdn-sphotos-a-a.akamaihd.net\\/hphotos-ak-snc6\\/s720x720\\/217932_10150892275930966_100628613_n.jpg","https:\\/\\/fbcdn-sphotos-e-a.akamaihd.net\\/hphotos-ak-ash4\\/429739_10150572527060966_1705568239_n.jpg","https:\\/\\/fbcdn-sphotos-f-a.akamaihd.net\\/hphotos-ak-ash4\\/s720x720\\/429591_10150568136315966_306645830_n.jpg","https:\\/\\/fbcdn-sphotos-c-a.akamaihd.net\\/hphotos-ak-ash4\\/s720x720\\/421081_10150489662365966_1624065190_n.jpg","https:\\/\\/fbcdn-sphotos-d-a.akamaihd.net\\/hphotos-ak-ash4\\/s720x720\\/390529_10150360170195966_375159027_n.jpg","https:\\/\\/fbcdn-sphotos-c-a.akamaihd.net\\/hphotos-ak-snc7\\/s720x720\\/376889_10150342067470966_271158823_n.jpg","https:\\/\\/fbcdn-sphotos-a-a.akamaihd.net\\/hphotos-ak-ash4\\/s720x720\\/310375_10150319967735966_257712580_n.jpg","https:\\/\\/fbcdn-sphotos-b-a.akamaihd.net\\/hphotos-ak-ash4\\/s720x720\\/320599_10150286945460966_1297474861_n.jpg","https:\\/\\/fbcdn-sphotos-h-a.akamaihd.net\\/hphotos-ak-ash4\\/296541_10150255626135966_1148863_n.jpg","https:\\/\\/fbcdn-sphotos-e-a.akamaihd.net\\/hphotos-ak-ash4\\/298385_10150253667610966_3377236_n.jpg","https:\\/\\/fbcdn-sphotos-a-a.akamaihd.net\\/hphotos-ak-snc6\\/248296_10150189960295966_2892972_n.jpg","https:\\/\\/fbcdn-sphotos-b-a.akamaihd.net\\/hphotos-ak-snc6\\/255083_10150189270850966_5876655_n.jpg","https:\\/\\/fbcdn-sphotos-h-a.akamaihd.net\\/hphotos-ak-snc6\\/227860_10150170466150966_1589691_n.jpg","https:\\/\\/fbcdn-sphotos-c-a.akamaihd.net\\/hphotos-ak-ash4\\/225890_10150161829270966_3778995_n.jpg","https:\\/\\/fbcdn-sphotos-a-a.akamaihd.net\\/hphotos-ak-snc6\\/221850_10150154173435966_6370594_n.jpg","https:\\/\\/fbcdn-sphotos-f-a.akamaihd.net\\/hphotos-ak-ash4\\/205150_10150140956905966_5310820_n.jpg","https:\\/\\/fbcdn-sphotos-h-a.akamaihd.net\\/hphotos-ak-snc6\\/200159_10150121747865966_1544503_n.jpg","https:\\/\\/fbcdn-sphotos-h-a.akamaihd.net\\/hphotos-ak-snc6\\/200217_10150106009575966_143426_n.jpg"]', 1, 1, 2, '03/17/1983', '', 'Jaboticabal', '[{"wo_sd_y":"2010","wo_p":"Research","wo_e":"\\u00c9cole Polytechnique de Montr\\u00e9al","wo_sd_m":"1","wo_l":"Montreal, Quebec"},{"wo_sd_y":"2010","wo_p":"Animateur","wo_e":"Montreal Science Centre","wo_sd_m":"1","wo_l":"Montreal, Quebec"}]', '[{"ed_c":"G\\u00e9nie Informatique (Computer Eng.)","ed_sn":"\\u00c9cole Polytechnique de Montr\\u00e9al","ed_y":"2012","ed_t":"Graduate School","ed_d":"MSCA"},{"ed_c":"G\\u00e9nie Logiciel (Software Eng.)","ed_sn":"\\u00c9cole Polytechnique de Montr\\u00e9al","ed_y":"2011","ed_t":"Graduate School","ed_d":"B.Ing"},{"ed_c":"Physique (Physics)","ed_sn":"Universit\\u00e9 de Montr\\u00e9al","ed_y":"2007","ed_t":"Graduate School","ed_d":"BSc"},{"ed_sn":"Hong Kong Polytechnic University","ed_t":"College"},{"ed_sn":"College de Bois-de-Boulogne","ed_t":"High School"},{"ed_sn":"Polyvalente Hyacinthe-Delorme","ed_t":"High School","ed_y":"2000"}]', '["My Bloody Valentine","Fleet Foxes","Explosions in the Sky","Modest Mouse","Neutral Milk Hotel","Yeasayer","The Black Keys","MGMT","Elliott Smith","Iron & Wine","Dave Brubeck","Mad Caddies","The Beatles","Sarah Bourdon","The Smashing Pumpkins","Radiohead","Sigur R\\u00f3s"]', '["Sideways"]', '["Foundation series"]', '["Breaking Bad"]', '["Centre d''entrepreneurship HEC-POLY-UdeM","Michel Houellebecq","Oktopuce","Pr\\u00eat-\\u00e0-reporter","Gabriela Smith Singer-actress","\\u00c9lectroma Podcast","Nightlife.ca","Project Syndicate","Trust Me, I''m an ''Engineer''","PolyFinances","Jack Layton","Renaud-Bray","Sorstu.ca","IEEEXtreme","\\u00c9cole Polytechnique - D\\u00e9partement de g\\u00e9nie informatique et g\\u00e9nie logiciel","Fondation CHU Sainte-Justine","Assommoir Notre-Dame","PolyU International","\\u00c9cole Polytechnique de Montr\\u00e9al","Luc Brodeur Jourdain"]', '["English","Brazilian Portuguese"]', '安世高', 'Pi is an infinite, nonrepeating decimal - meaning that every possible number combination exists somewhere in Pi. Converted into ASCII text, somewhere in that infinite string of digits is the name of every person you will ever love, the date, time, and manner of your death, and the answers to all the great questions of the universe.', 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, '2012-10-21 02:39:41', '2012-11-13 17:55:14');

-- --------------------------------------------------------

--
-- Table structure for table `users_blocks`
--

CREATE TABLE IF NOT EXISTS `users_blocks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `userBlockedId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_blocks_user` (`userId`),
  KEY `fk_users_blocks_userLiked` (`userBlockedId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `users_likes`
--

CREATE TABLE IF NOT EXISTS `users_likes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `userLikedId` bigint(20) NOT NULL,
  `placeId` bigint(20) NOT NULL,
  `likeTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_likes_user` (`userId`),
  KEY `fk_users_likes_userLiked` (`userLikedId`),
  KEY `fk_users_likes_place` (`placeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=93 ;

--
-- Dumping data for table `users_likes`
--

INSERT INTO `users_likes` (`id`, `userId`, `userLikedId`, `placeId`, `likeTime`) VALUES
(1, 31, 34, 22, '2012-10-22 22:50:32'),
(2, 30, 34, 12, '2012-10-21 22:50:57'),
(3, 30, 34, 10, '2012-10-24 03:17:21'),
(4, 34, 31, 3, '2012-10-23 15:34:01'),
(50, 34, 32, 1, '2012-10-25 19:31:34'),
(51, 34, 31, 1, '2012-10-25 19:33:50'),
(52, 34, 30, 1, '2012-10-25 19:36:42'),
(55, 34, 7, 1, '2012-10-26 16:46:36'),
(88, 7, 34, 1, '2012-11-05 06:41:54'),
(89, 7, 34, 1, '2012-11-06 00:56:21'),
(90, 32, 34, 1, '2012-11-06 01:10:42'),
(91, 34, 7, 1, '2012-11-07 15:50:34'),
(92, 34, 7, 1, '2012-11-13 17:57:20');

-- --------------------------------------------------------

--
-- Table structure for table `users_messages`
--

CREATE TABLE IF NOT EXISTS `users_messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `userMessagedId` bigint(20) NOT NULL,
  `placeId` bigint(20) NOT NULL,
  `messageType` smallint(6) NOT NULL,
  `messageTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_messages_user` (`userId`),
  KEY `fk_users_messages_userMessaged` (`userMessagedId`),
  KEY `fk_users_messages_place` (`placeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=18 ;

--
-- Dumping data for table `users_messages`
--

INSERT INTO `users_messages` (`id`, `userId`, `userMessagedId`, `placeId`, `messageType`, `messageTime`) VALUES
(1, 31, 34, 3, 0, '2012-10-23 03:12:32'),
(2, 32, 34, 6, 3, '2012-10-23 04:17:33'),
(14, 7, 31, 1, 1, '2012-10-26 16:52:25'),
(17, 7, 34, 1, 1, '2012-11-03 20:33:12');

-- --------------------------------------------------------

--
-- Table structure for table `users_pers_messages`
--

CREATE TABLE IF NOT EXISTS `users_pers_messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `userPersMessagedId` bigint(20) NOT NULL,
  `placeId` bigint(20) NOT NULL,
  `message` varchar(140) NOT NULL,
  `messageTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_pers_messages_user` (`userId`),
  KEY `fk_users_pers_messages_userPersMessaged` (`userPersMessagedId`),
  KEY `fk_users_pers_messages_place` (`placeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `users_pers_messages`
--

INSERT INTO `users_pers_messages` (`id`, `userId`, `userPersMessagedId`, `placeId`, `message`, `messageTime`) VALUES
(1, 34, 31, 17, 'vuvucucu', '2012-10-23 03:19:10');

-- --------------------------------------------------------

--
-- Table structure for table `users_places`
--

CREATE TABLE IF NOT EXISTS `users_places` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `placeId` int(11) NOT NULL,
  `enterTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_user` (`userId`),
  KEY `fk_place` (`placeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=97 ;

--
-- Dumping data for table `users_places`
--

INSERT INTO `users_places` (`id`, `userId`, `placeId`, `enterTime`) VALUES
(3, 29, 1, '2012-11-13 17:34:38'),
(4, 30, 1, '2012-11-13 06:34:44'),
(5, 31, 1, '2012-11-13 07:33:50'),
(25, 7, 1, '2012-11-13 17:14:04'),
(54, 32, 1, '2012-11-13 12:16:53'),
(96, 34, 1, '2012-11-13 17:55:24');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `places`
--
ALTER TABLE `places`
  ADD CONSTRAINT `fk_place_city` FOREIGN KEY (`cityId`) REFERENCES `cities` (`cityId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `fk_user_city` FOREIGN KEY (`cityId`) REFERENCES `cities` (`cityId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `users_places`
--
ALTER TABLE `users_places`
  ADD CONSTRAINT `fk_place` FOREIGN KEY (`placeId`) REFERENCES `places` (`placeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
