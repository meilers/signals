


CREATE TABLE IF NOT EXISTS `cities_requested`
(
    `cityRequestedId` INT AUTO_INCREMENT,
    `userId` BIGINT NOT NULL,
    `city` varchar(40) NOT NULL,
    `state` varchar(40) NOT NULL,
    `country` varchar(40) NOT NULL,
    
    PRIMARY KEY (`cityRequestedId`),
    CONSTRAINT `fk_requested_user`
        FOREIGN KEY (`userId`) 
        REFERENCES `users`(`userId`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
)
ENGINE = InnoDB DEFAULT CHARSET=utf8;



