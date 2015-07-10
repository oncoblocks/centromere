

-- -----------------------------------------------------
-- Table `subjects`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `subjects` ;

CREATE TABLE IF NOT EXISTS `subjects` (
  `subject_id` INT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `gender` VARCHAR(1) NULL,
  `species` VARCHAR(64) NOT NULL,
  `notes` VARCHAR(1024) NULL,
  PRIMARY KEY (`subject_id`));


-- -----------------------------------------------------
-- Table `subject_attributes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `subject_attributes` ;

CREATE TABLE IF NOT EXISTS `subject_attributes` (
  `subject_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `value` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`subject_id`, `name`));


-- -----------------------------------------------------
-- Table `subject_aliases`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `subject_aliases` ;

CREATE TABLE IF NOT EXISTS `subject_aliases` (
  `subject_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `source` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`subject_id`, `source`, `name`));
