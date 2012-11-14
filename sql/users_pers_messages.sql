-- phpMyAdmin SQL Dump
-- version 3.5.0-beta1
-- http://www.phpmyadmin.net
--
-- Host: mysql-shared-02.phpfog.com
-- Generation Time: Aug 10, 2012 at 08:38 PM
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
-- Table structure for table `users_messages`
--

CREATE TABLE IF NOT EXISTS `users_pers_messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `userPersMessagedId` bigint(20) NOT NULL,
  `placeId` bigint(20) NOT NULL,
  `message` VARCHAR(140) NOT NULL,
  `messageTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_pers_messages_user` (`userId`),
  KEY `fk_users_pers_messages_userPersMessaged` (`userPersMessagedId`),
  KEY `fk_users_pers_messages_place` (`placeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;
