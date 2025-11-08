CREATE TABLE IF NOT EXISTS `time_bank` (
                                           `total_value` INT DEFAULT NULL,
                                           `employee_id` bigint DEFAULT NULL,
                                           `id` bigint NOT NULL AUTO_INCREMENT,
                                           `last_update` datetime(6) DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           UNIQUE KEY `UK183n1tvsjpyykqf989yfsc8a5` (`employee_id`),
                                           CONSTRAINT `FK6knopp5vgios9x06o5tfksn0g` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;