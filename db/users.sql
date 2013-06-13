




CREATE TABLE IF NOT EXISTS `users`
(
    `userId` BIGINT AUTO_INCREMENT,
    `fid` BIGINT NOT NULL,
    
    `cityId` INT NOT NULL,
    
    
    `username` VARCHAR(16) NOT NULL,
    `picPosX` float DEFAULT 0.0,
    `picPosY` float DEFAULT 0.0,
    `profilePicsUrls` text,

    `sex` TINYINT NOT NULL,
    `orientation` TINYINT NOT NULL,
    `ageGroup` TINYINT NOT NULL,
    `birthday` VARCHAR(10) NOT NULL,
       
    `tonight` text,   
    `hometown` text,
    `work` text,
    `education` text,
    
    -- Likes
    `music` text,
    `movies` text,
    `books` text,
    `television` text,
    `other` text,
    
    `languages` text,
    `aboutMe` text,
    `quotations` text,
                                                
    `wristBlue` TINYINT DEFAULT 0,
    `wristRed` TINYINT DEFAULT 0,
    `wristYellow` TINYINT DEFAULT 0,
    `wristGreen` TINYINT DEFAULT 0,
    
    
    -- preferences
    `w_sex` INT DEFAULT 0,
    `w_orientation` INT DEFAULT 0,
    `w_ageGroup` INT DEFAULT 0,
    `w_wristBlue` TINYINT DEFAULT 0,
    `w_wristRed` TINYINT DEFAULT 0,
    `w_wristYellow` TINYINT DEFAULT 0,
    `w_wristGreen` TINYINT DEFAULT 0,

    
    `online` TINYINT DEFAULT 1,
    `superuser` TINYINT DEFAULT 0,
    `end_superuser` DATETIME NOT NULL,
    `last_active` DATETIME NOT NULL,


    UNIQUE(`fid`),
    PRIMARY KEY (`userId`),
    CONSTRAINT `fk_user_city`
        FOREIGN KEY (`cityId`) 
        REFERENCES `cities`(`cityId`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
)
ENGINE = InnoDB DEFAULT CHARSET=utf8;


-- pour afficher en chinois
SET NAMES utf8;
