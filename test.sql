-- create table `account`
# DROP TABLE `test` IF EXISTS
CREATE TABLE `test` (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(20) NOT NULL,
  money double DEFAULT NULL,
  project_code varchar(500)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
INSERT INTO `test` VALUES ('1', 'aaa', '1000');
INSERT INTO `test` VALUES ('2', 'bbb', '1000');
INSERT INTO `test` VALUES ('3', 'ccc', '1000');