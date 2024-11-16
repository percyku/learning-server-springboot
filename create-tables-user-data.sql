use `learningsys`;


--
-- Dumping data for table `user`
--
-- NOTE: The passwords are encrypted using BCrypt
--
--
-- Default passwords here are: fun123
--

INSERT INTO `user` (`username`,`password`,`enabled`, `first_name`, `last_name`, `email`)
VALUES 
('studnet1','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'student1', 'test', 'studnet1@gmail.com'),
('instructor1','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'instructor1', 'test', 'instructor1@gmail.com')



--
-- Dumping data for table `roles`
--

INSERT INTO `role` (name)
VALUES 
('ROLE_STUDENT'),('ROLE_INSTRUCTOR');

SET FOREIGN_KEY_CHECKS = 0;



--
-- Dumping data for table `users_roles`
--

INSERT INTO `users_roles` (user_id,role_id)
VALUES 
(1, 1),
(2, 2),