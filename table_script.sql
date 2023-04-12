CREATE TABLE `user` (
  `userId` int NOT NULL AUTO_INCREMENT,
  `createDate` date DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `isAdmin` tinyint DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ;