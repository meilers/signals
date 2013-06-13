


CREATE TABLE IF NOT EXISTS `cities`
(
    `cityId` INT AUTO_INCREMENT,
    `city` varchar(40) NOT NULL,
    `state` varchar(40) NOT NULL,
    `country` varchar(40) NOT NULL,
    `hasPlaces` TINYINT DEFAULT 0,
    PRIMARY KEY (`cityId`)
)
ENGINE = InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `cities` (`city`, `state`, `country`) VALUES('Montreal','QC', 'Canada');

