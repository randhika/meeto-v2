SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema meeto
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `meeto`;

-- -----------------------------------------------------
-- Schema meeto
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `meeto`
  DEFAULT CHARACTER SET utf8;
USE `meeto`;

-- -----------------------------------------------------
-- Table `meeto`.`member`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meeto`.`member`;

CREATE TABLE IF NOT EXISTS `meeto`.`member` (
  `idmember` INT(11)      NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16)  NOT NULL,
  `email`    VARCHAR(127) NULL DEFAULT NULL,
  `password` VARCHAR(32)  NOT NULL,
  PRIMARY KEY (`idmember`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `meeto`.`meeting`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meeto`.`meeting`;

CREATE TABLE IF NOT EXISTS `meeto`.`meeting` (
  `idmeeting` INT(11)      NOT NULL AUTO_INCREMENT,
  `title`     VARCHAR(45)  NULL DEFAULT NULL
  COMMENT '		',
  `objective` VARCHAR(255) NULL DEFAULT NULL,
  `date`      DATE         NOT NULL,
  `time`      TIME         NOT NULL,
  `location`  VARCHAR(45)  NOT NULL,
  PRIMARY KEY (`idmeeting`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `meeto`.`action`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meeto`.`action`;

CREATE TABLE IF NOT EXISTS `meeto`.`action` (
  `idaction`    INT(11)     NOT NULL AUTO_INCREMENT,
  `idmeeting`   INT(11)     NOT NULL,
  `idmember`    INT(11)     NOT NULL,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  `completed`   TINYINT(1)  NULL DEFAULT '0',
  PRIMARY KEY (`idaction`),
  INDEX `fk_action_member1_idx` (`idmember` ASC),
  INDEX `fk_action_meeting1_idx` (`idmeeting` ASC),
  CONSTRAINT `fk_action_member1`
  FOREIGN KEY (`idmember`)
  REFERENCES `meeto`.`member` (`idmember`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_action_meeting1`
  FOREIGN KEY (`idmeeting`)
  REFERENCES `meeto`.`meeting` (`idmeeting`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `meeto`.`agenda`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meeto`.`agenda`;

CREATE TABLE IF NOT EXISTS `meeto`.`agenda` (
  `idagenda`  INT(11) NOT NULL AUTO_INCREMENT,
  `idmeeting` INT(11) NOT NULL,
  PRIMARY KEY (`idagenda`),
  INDEX `fk_agenda_meeting1_idx` (`idmeeting` ASC),
  CONSTRAINT `fk_agenda_meeting1`
  FOREIGN KEY (`idmeeting`)
  REFERENCES `meeto`.`meeting` (`idmeeting`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `meeto`.`item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meeto`.`item`;

CREATE TABLE IF NOT EXISTS `meeto`.`item` (
  `iditem`      INT(11)      NOT NULL AUTO_INCREMENT,
  `idagenda`    INT(11)      NOT NULL,
  `name`        VARCHAR(127) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `keydecision` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`iditem`),
  INDEX `fk_item_agenda1_idx` (`idagenda` ASC),
  CONSTRAINT `fk_item_agenda1`
  FOREIGN KEY (`idagenda`)
  REFERENCES `meeto`.`agenda` (`idagenda`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `meeto`.`log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meeto`.`log`;

CREATE TABLE IF NOT EXISTS `meeto`.`log` (
  `idlog`    INT(11)  NOT NULL AUTO_INCREMENT,
  `iditem`   INT(11)  NOT NULL,
  `idmember` INT(11)  NOT NULL,
  `line`     LONGTEXT NULL DEFAULT NULL,
  PRIMARY KEY (`idlog`),
  INDEX `fk_log_item1_idx` (`iditem` ASC),
  INDEX `fk_log_member1_idx` (`idmember` ASC),
  CONSTRAINT `fk_log_item1`
  FOREIGN KEY (`iditem`)
  REFERENCES `meeto`.`item` (`iditem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_log_member1`
  FOREIGN KEY (`idmember`)
  REFERENCES `meeto`.`member` (`idmember`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `meeto`.`meeting_member`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meeto`.`meeting_member`;

CREATE TABLE IF NOT EXISTS `meeto`.`meeting_member` (
  `idmeeting` INT(11)    NOT NULL,
  `idmember`  INT(11)    NOT NULL,
  `accepted`  TINYINT(1) NOT NULL DEFAULT '0',
  INDEX `fk_meeting_members_meeting_idx` (`idmeeting` ASC),
  PRIMARY KEY (`idmeeting`, `idmember`),
  INDEX `fk_meeting_members_member1_idx` (`idmember` ASC),
  CONSTRAINT `fk_meeting_members_meeting`
  FOREIGN KEY (`idmeeting`)
  REFERENCES `meeto`.`meeting` (`idmeeting`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_meeting_members_member1`
  FOREIGN KEY (`idmember`)
  REFERENCES `meeto`.`member` (`idmember`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
