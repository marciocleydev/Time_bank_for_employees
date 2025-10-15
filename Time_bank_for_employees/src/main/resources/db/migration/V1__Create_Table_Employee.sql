CREATE TABLE IF NOT EXISTS `employee` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `name` varchar(255) DEFAULT NULL,
                                          `pis` varchar(255) NOT NULL,
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `UKe1u4no7rmmc16xoh9ppemknrc` (`pis`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

