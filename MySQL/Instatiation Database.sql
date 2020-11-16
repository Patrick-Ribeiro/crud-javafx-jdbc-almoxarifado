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
CREATE TABLE IF NOT EXISTS `almox`.`user_groups` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`users`
-- -----------------------------------------------------
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
CREATE TABLE IF NOT EXISTS `almox`.`expenses` (
  `debit` INT NOT NULL,
  `description` VARCHAR(100) NULL,
  `type` ENUM('S', 'C') NOT NULL,
  PRIMARY KEY (`debit`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`product_categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `almox`.`product_categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`departaments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `almox`.`departaments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`product_groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `almox`.`product_groups` (
  `id_erp` INT NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `expense_id` INT NOT NULL,
  PRIMARY KEY (`id_erp`),
  INDEX `fk_grupo_produto_despesa1_idx` (`expense_id` ASC) VISIBLE,
  CONSTRAINT `fk_grupo_produto_despesa1`
    FOREIGN KEY (`expense_id`)
    REFERENCES `almox`.`expenses` (`debit`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`packings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `almox`.`packings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  `abbreviation` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `almox`.`products` (
  `internalCode` INT NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `description_erp` VARCHAR(45) NOT NULL,
  `category_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  `packing_id` INT NOT NULL,
  `quantity_packing` INT NOT NULL,
  `buyer_user_id` INT NOT NULL,
  `active` TINYINT NOT NULL,
  INDEX `fk_produto_categoria1_idx` (`category_id` ASC) VISIBLE,
  INDEX `fk_produto_grupo_produto1_idx` (`group_id` ASC) VISIBLE,
  INDEX `fk_products_users1_idx` (`buyer_user_id` ASC) VISIBLE,
  INDEX `fk_products_packings1_idx` (`packing_id` ASC) VISIBLE,
  PRIMARY KEY (`internalCode`),
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
    REFERENCES `almox`.`products` (`internalCode`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`products_movement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `almox`.`products_movement` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `type` ENUM('IN', 'OU') NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_products_movement_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_products_movement_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `almox`.`products` (`internalCode`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `almox`.`product_inventory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `almox`.`product_inventory` (
  `product_id` INT NOT NULL,
  `minimum_inventory` INT NOT NULL,
  `current_invetory` INT NOT NULL,
  `maximum_inventory` INT NOT NULL,
  INDEX `fk_estoques_produto1_idx` (`product_id` ASC) VISIBLE,
  PRIMARY KEY (`product_id`),
  CONSTRAINT `fk_estoques_produto1`
    FOREIGN KEY (`product_id`)
    REFERENCES `almox`.`products` (`internalCode`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


USE almox;

INSERT INTO user_groups (description) VALUES
('Almoxarife'),
('Comprador'), 
('Gerente'),
('Suporte');

INSERT INTO product_categories (description) VALUES
('Bandejas'),
('Bobina/Filme'),
('E.P.I.'),
('Embalagens Rotisseria'),
('Etiqueta de balança'),
('Lanches e refeições'),
('Material de escritório'),
('Material de limpeza'),
('Material de uso e consumo'),
('Rapid bag/Speed roll'),
('Sacolas');

INSERT INTO expenses (debit, description, type) VALUES
(331201, 'Uniformes e Equipamentos de Proteção Individual', 'C'),
(331410, 'Gás', 'C'),
(332501, 'Impressos', 'C'),
(332502, 'Material de Escritório', 'C'),
(332503, 'Material de Limpeza', 'C'),
(332504, 'Suprimentos de Informática', 'C'),
(332505, 'Despesas com Duplicação e Encardenação', 'C'),
(332507, 'Material de Uso e Consumo', 'C'),
(332706, 'Lanches e Refeições', 'C'),
(332601, 'Sacolas', 'S'),
(332602, 'Embalagens Açougue', 'S'),
(332603, 'Embalagens Rotisseria', 'S'),
(332604, 'Bobina Resinite/Filme', 'S'),
(332605, 'Rapid Bag / Spreed Roll', 'S'),
(332606, 'Bandejas', 'S'),
(332607, 'Etiquetas para Balança', 'S'),
(332608, 'Bobina Termoscripty', 'S'),
(332609, 'Suprimentos para o PDV/bobinas', 'S'),
(332612, 'Sacolas Hortifruti', 'S');

INSERT INTO departaments (name) VALUES
('Açougue'),
('Cartão'),
('Contabilidade'),
('Depósito'),
('Fatiados'),
('Financeiro'),
('Frente de caixa'),
('Hortifruti'),
('Marketing'),
('Mercearia'),
('Padaria'),
('R.H.');