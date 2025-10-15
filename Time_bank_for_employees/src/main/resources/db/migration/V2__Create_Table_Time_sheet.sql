CREATE TABLE IF NOT EXISTS `time_sheet` (
                                            `over_time` double DEFAULT NULL,
                                            `date_time` datetime(6) DEFAULT NULL,
                                            `employee_id` bigint DEFAULT NULL,
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `image_storage` varchar(255) DEFAULT NULL,
                                            PRIMARY KEY (`id`),
                                            KEY `FKs3xh9hr73dr02loe8emw5nb8p` (`employee_id`),
                                            CONSTRAINT `FKs3xh9hr73dr02loe8emw5nb8p` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;