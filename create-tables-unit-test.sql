use `learningsys_unit_test`;
--
-- Table structure for table `user`
--
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` char(80) NOT NULL,
  `enabled` tinyint NOT NULL,  
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) NOT NULL,
  `email` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Table structure for table `role`
--
DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Table structure for table `users_roles`
--
DROP TABLE IF EXISTS `users_roles`;

CREATE TABLE `users_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  
  PRIMARY KEY (`user_id`,`role_id`),
  
  KEY `FK_ROLE_idx` (`role_id`),
  
  CONSTRAINT `FK_USER_05` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) 
  REFERENCES `role` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;


--
-- Table structure for table `course`
--
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(128) DEFAULT NULL,
  `price` int DEFAULT 0,
  `description` varchar(256) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  
  PRIMARY KEY (`id`),
  
  UNIQUE KEY `TITLE_UNIQUE` (`title`),
  
  KEY `FK_USER_idx` (`user_id`),
  
  CONSTRAINT `FK_USER` 
  FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;


--
-- Table structure for table `review`
--
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `comment` varchar(256) DEFAULT NULL,
  `course_id` int DEFAULT NULL,
	`user_id` int DEFAULT NULL,

  PRIMARY KEY (`id`),

  KEY `FK_COURSE_ID_idx` (`course_id`),

  CONSTRAINT `FK_COURSE` 
  FOREIGN KEY (`course_id`) 
  REFERENCES `course` (`id`), 

	KEY `FK_USER_02_idx` (`user_id`),
  
  CONSTRAINT `FK_USER_02` 
  FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 

  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Table structure for table `course_user`
--
DROP TABLE IF EXISTS `course_user`;
CREATE TABLE `course_user` (
  `course_id` int NOT NULL,
  `user_id` int NOT NULL,
  
  PRIMARY KEY (`course_id`,`user_id`),
  
  KEY `FK_USER_01_idx` (`user_id`),
  
  CONSTRAINT `FK_COURSE_05` FOREIGN KEY (`course_id`) 
  REFERENCES `course` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  CONSTRAINT `FK_USER_01` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


SET FOREIGN_KEY_CHECKS = 1;




use `learningsys_unit_test`;

-- Dumping data for table `roles`
--

INSERT INTO `role` (name)
VALUES 
('ROLE_STUDENT'),('ROLE_INSTRUCTOR');

SET FOREIGN_KEY_CHECKS = 0;