

-- -----------------------------------------------------
-- Table `entrez_gene`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `entrez_gene` ;

CREATE TABLE IF NOT EXISTS `entrez_gene` (
  `entrez_gene_id` INT NOT NULL,
  `primary_gene_symbol` VARCHAR(32) NOT NULL,
  `tax_id` INT NOT NULL,
  `locus_tag` VARCHAR(128) NULL,
  `chromosome` VARCHAR(2) NULL,
  `chromosome_location` VARCHAR(128) NULL,
  `description` VARCHAR(1024) NULL,
  `gene_type` VARCHAR(32) NULL,
  PRIMARY KEY (`entrez_gene_id`));


-- -----------------------------------------------------
-- Table `gene_attributes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gene_attributes` ;

CREATE TABLE IF NOT EXISTS `gene_attributes` (
  `entrez_gene_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `value` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`entrez_gene_id`, `name`));


-- -----------------------------------------------------
-- Table `gene_aliases`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gene_aliases` ;

CREATE TABLE IF NOT EXISTS `gene_aliases` (
  `entrez_gene_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`entrez_gene_id`, `name`));
