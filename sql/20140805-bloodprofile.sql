CREATE TABLE `redhelp_db`.`user_blood_profile` (
  `B_P_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `BLOOD_GROUP` INT,
  `BIRTH_DATE` DATETIME,
  `LAST_KNOWN_LOCATION_LAT` DOUBLE,
  `LAST_KNOWN_LOCATION_LONG` DOUBLE,
  `LAST_KNONW_LOCATION_DATE_TIME` DATETIME,
  `CITY` VARCHAR(50),
  `GENDER` INT,
  PRIMARY KEY (`B_P_ID`)
)
CHARACTER SET utf8
COMMENT = 'Contains blood profile related users information';


ALTER TABLE `redhelp_db`.`user_blood_profile` ADD COLUMN `U_ID` BIGINT(20) NOT NULL AFTER `GENDER`;

ALTER TABLE `redhelp_db`.`user_blood_profile` CHANGE COLUMN `LAST_KNONW_LOCATION_DATE_TIME` `LAST_KNOWN_LOCATION_DATE_TIME` DATETIME DEFAULT NULL;


ALTER TABLE `redhelp_db`.`user_blood_profile` ADD CONSTRAINT `blood_profile_user_account_fk` FOREIGN KEY `blood_profile_user_account_fk` (`U_ID`)
    REFERENCES `user_account` (`U_ID`);
