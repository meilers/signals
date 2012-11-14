




CREATE TABLE IF NOT EXISTS `users_places`
(
    `id` INT AUTO_INCREMENT,
    `userId` BIGINT NOT NULL,
    `placeId` INT NOT NULL,
    `enterTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_user`
        FOREIGN KEY (`userId`) 
        REFERENCES `users`(`userId`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT `fk_place`
        FOREIGN KEY (`placeId`) 
        REFERENCES `places`(`placeId`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
)
ENGINE = InnoDB DEFAULT CHARSET=utf8;

