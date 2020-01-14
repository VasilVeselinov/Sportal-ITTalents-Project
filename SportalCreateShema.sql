-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema SportalProjectSchema
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema SportalProjectSchema
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `SportalProjectSchema` DEFAULT CHARACTER SET utf8 ;
USE `SportalProjectSchema` ;

-- -----------------------------------------------------
-- Table `SportalProjectSchema`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SportalProjectSchema`.`users` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(20) NOT NULL,
  `user_password` VARCHAR(100) NOT NULL,
  `user_email` VARCHAR(40) NOT NULL,
  `is_admin` TINYINT NULL,
  PRIMARY KEY (`id`),
  INDEX `u_name_index` (`user_name` ASC) VISIBLE,
  INDEX `u_email_index` (`user_email` ASC) VISIBLE,
  UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC) VISIBLE,
  UNIQUE INDEX `user_email_UNIQUE` (`user_email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SportalProjectSchema`.`articles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SportalProjectSchema`.`articles` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `full_text` LONGTEXT NOT NULL,
  `date_published` DATETIME NOT NULL,
  `views` INT UNSIGNED NULL,
  `author_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `user_author_id_FK`
    FOREIGN KEY (`author_id`)
    REFERENCES `SportalProjectSchema`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SportalProjectSchema`.`pictures`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SportalProjectSchema`.`pictures` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `picture_url` VARCHAR(100) NOT NULL,
  `article_id` INT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `picture_url_UNIQUE` (`picture_url` ASC) VISIBLE,
  CONSTRAINT `pikture_article_id_FK`
    FOREIGN KEY (`article_id`)
    REFERENCES `SportalProjectSchema`.`articles` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SportalProjectSchema`.`comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SportalProjectSchema`.`comments` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `full_comment_text` LONGTEXT NOT NULL,
  `date_published` DATETIME NOT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  `article_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `user_comment_id_FK`
    FOREIGN KEY (`user_id`)
    REFERENCES `SportalProjectSchema`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `comment_article_id_FK`
    FOREIGN KEY (`article_id`)
    REFERENCES `SportalProjectSchema`.`articles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SportalProjectSchema`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SportalProjectSchema`.`categories` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `category_name_UNIQUE` (`category_name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SportalProjectSchema`.`articles_categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SportalProjectSchema`.`articles_categories` (
  `article_id` INT UNSIGNED NOT NULL,
  `category_id` INT UNSIGNED NOT NULL,
  UNIQUE INDEX `PK` (`article_id` ASC, `category_id` ASC) VISIBLE,
  CONSTRAINT `article_id_FK`
    FOREIGN KEY (`article_id`)
    REFERENCES `SportalProjectSchema`.`articles` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `category_id_FK`
    FOREIGN KEY (`category_id`)
    REFERENCES `SportalProjectSchema`.`categories` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SportalProjectSchema`.`users_like_articles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SportalProjectSchema`.`users_like_articles` (
  `article_id` INT UNSIGNED NOT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  UNIQUE INDEX `PK` (`article_id` ASC, `user_id` ASC) VISIBLE,
  CONSTRAINT `like_article_id_FK`
    FOREIGN KEY (`article_id`)
    REFERENCES `SportalProjectSchema`.`articles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `like_user_id_FK`
    FOREIGN KEY (`user_id`)
    REFERENCES `SportalProjectSchema`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SportalProjectSchema`.`users_like_comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SportalProjectSchema`.`users_like_comments` (
  `comment_id` INT UNSIGNED NOT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  UNIQUE INDEX `PK` (`comment_id` ASC, `user_id` ASC) VISIBLE,
  CONSTRAINT `like_comment_id_FK`
    FOREIGN KEY (`comment_id`)
    REFERENCES `SportalProjectSchema`.`comments` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_like_id_FK`
    FOREIGN KEY (`user_id`)
    REFERENCES `SportalProjectSchema`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SportalProjectSchema`.`users_disliked_comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SportalProjectSchema`.`users_disliked_comments` (
  `comment_id` INT UNSIGNED NOT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  UNIQUE INDEX `PK` (`comment_id` ASC, `user_id` ASC) VISIBLE,
  CONSTRAINT `user_d_comment_FK`
    FOREIGN KEY (`user_id`)
    REFERENCES `SportalProjectSchema`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `comment_d_user_FK`
    FOREIGN KEY (`comment_id`)
    REFERENCES `SportalProjectSchema`.`comments` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
