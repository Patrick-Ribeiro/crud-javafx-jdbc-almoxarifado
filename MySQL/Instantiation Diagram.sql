-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema almox
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `almox` ;

-- -----------------------------------------------------
-- Schema almox
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `almox` DEFAULT CHARACTER SET utf8 ;
USE `almox` ;

-- -----------------------------------------------------
-- Table `almox`.`user_groups`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`user_groups` ;

CREATE TABLE IF NOT EXISTS `almox`.`user_groups` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`users` ;

CREATE TABLE IF NOT EXISTS `almox`.`users` (
  `code_erp` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(12) NULL,
  `email` VARCHAR(100) NULL,
  `telephone` VARCHAR(14) NULL,
  `group_id` INT NOT NULL,
  `active` TINYINT NOT NULL,
  PRIMARY KEY (`code_erp`),
  INDEX `fk_usuario_grupo_usuario1_idx` (`group_id` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_grupo_usuario1`
    FOREIGN KEY (`group_id`)
    REFERENCES `almox`.`user_groups` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`expenses`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`expenses` ;

CREATE TABLE IF NOT EXISTS `almox`.`expenses` (
  `debit` INT NOT NULL,
  `description` VARCHAR(100) NULL,
  `type` ENUM('SELLING', 'CONSUMPTION') NOT NULL,
  PRIMARY KEY (`debit`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`product_categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`product_categories` ;

CREATE TABLE IF NOT EXISTS `almox`.`product_categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`departaments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`departaments` ;

CREATE TABLE IF NOT EXISTS `almox`.`departaments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`product_groups`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`product_groups` ;

CREATE TABLE IF NOT EXISTS `almox`.`product_groups` (
  `id_erp` INT NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `expense_debit` INT NOT NULL,
  PRIMARY KEY (`id_erp`),
  INDEX `fk_grupo_produto_despesa1_idx` (`expense_debit` ASC) VISIBLE,
  CONSTRAINT `fk_grupo_produto_despesa1`
    FOREIGN KEY (`expense_debit`)
    REFERENCES `almox`.`expenses` (`debit`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`packings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`packings` ;

CREATE TABLE IF NOT EXISTS `almox`.`packings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  `abbreviation` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`products`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`products` ;

CREATE TABLE IF NOT EXISTS `almox`.`products` (
  `internal_code` INT NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `description_erp` VARCHAR(45) NOT NULL,
  `category_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  `packing_id` INT NOT NULL,
  `quantity_packing` DOUBLE NOT NULL,
  `buyer_user_id` INT NOT NULL,
  `active` TINYINT NOT NULL,
  INDEX `fk_produto_categoria1_idx` (`category_id` ASC) VISIBLE,
  INDEX `fk_produto_grupo_produto1_idx` (`group_id` ASC) VISIBLE,
  INDEX `fk_products_users1_idx` (`buyer_user_id` ASC) VISIBLE,
  INDEX `fk_products_packings1_idx` (`packing_id` ASC) VISIBLE,
  PRIMARY KEY (`internal_code`),
  CONSTRAINT `fk_produto_categoria1`
    FOREIGN KEY (`category_id`)
    REFERENCES `almox`.`product_categories` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_produto_grupo_produto1`
    FOREIGN KEY (`group_id`)
    REFERENCES `almox`.`product_groups` (`id_erp`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_users1`
    FOREIGN KEY (`buyer_user_id`)
    REFERENCES `almox`.`users` (`code_erp`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_packings1`
    FOREIGN KEY (`packing_id`)
    REFERENCES `almox`.`packings` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`departament_products`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`departament_products` ;

CREATE TABLE IF NOT EXISTS `almox`.`departament_products` (
  `departament_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  `throw` TINYINT NOT NULL,
  PRIMARY KEY (`departament_id`, `product_id`),
  INDEX `fk_departamento_has_produto_produto1_idx` (`product_id` ASC) VISIBLE,
  INDEX `fk_departamento_has_produto_departamento1_idx` (`departament_id` ASC) VISIBLE,
  CONSTRAINT `fk_departamento_has_produto_departamento1`
    FOREIGN KEY (`departament_id`)
    REFERENCES `almox`.`departaments` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_departamento_has_produto_produto1`
    FOREIGN KEY (`product_id`)
    REFERENCES `almox`.`products` (`internal_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`products_movements`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`products_movements` ;

CREATE TABLE IF NOT EXISTS `almox`.`products_movements` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `type` ENUM('IN', 'OU') NOT NULL,
  `quantity` DOUBLE NOT NULL,
  `previous_inventory` DOUBLE NULL,
  `final_inventory` DOUBLE NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_products_movement_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_products_movement_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `almox`.`products` (`internal_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`product_inventory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`product_inventory` ;

CREATE TABLE IF NOT EXISTS `almox`.`product_inventory` (
  `product_id` INT NOT NULL,
  `minimum_inventory` DOUBLE NOT NULL,
  `current_invetory` DOUBLE NOT NULL,
  `maximum_inventory` DOUBLE NOT NULL,
  INDEX `fk_estoques_produto1_idx` (`product_id` ASC) VISIBLE,
  PRIMARY KEY (`product_id`),
  CONSTRAINT `fk_estoques_produto1`
    FOREIGN KEY (`product_id`)
    REFERENCES `almox`.`products` (`internal_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`orders` ;

CREATE TABLE IF NOT EXISTS `almox`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `user_requester` INT NOT NULL,
  `status` ENUM('PENDING', 'INCLUDED', 'IN_PROGRESS', 'COMPLETED', 'CANCELED') NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_orders_users1_idx` (`user_requester` ASC) VISIBLE,
  CONSTRAINT `fk_orders_users1`
    FOREIGN KEY (`user_requester`)
    REFERENCES `almox`.`users` (`code_erp`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`order_products`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `almox`.`order_products` ;

CREATE TABLE IF NOT EXISTS `almox`.`order_products` (
  `order_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  `product_quantity` DOUBLE NOT NULL,
  `product_included` TINYINT NOT NULL,
  PRIMARY KEY (`order_id`, `product_id`),
  INDEX `fk_orders_has_products_products1_idx` (`product_id` ASC) VISIBLE,
  INDEX `fk_orders_has_products_orders1_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `fk_orders_has_products_orders1`
    FOREIGN KEY (`order_id`)
    REFERENCES `almox`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_has_products_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `almox`.`products` (`internal_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
