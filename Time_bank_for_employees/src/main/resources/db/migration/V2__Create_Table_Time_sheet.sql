CREATE TABLE IF NOT EXISTS `time_sheet` (
                                            `date` DATE DEFAULT NULL,
                                            `created_at` datetime(6) DEFAULT NULL,
                                            `expected_work_time` TIME DEFAULT NULL,
                                            `actual_sheet_time` TIME DEFAULT NULL,
                                            `difference_in_minutes` INT DEFAULT NULL,
                                            `employee_id` bigint DEFAULT NULL,
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `photo_url` varchar(255) DEFAULT NULL,
                                            PRIMARY KEY (`id`),
                                            KEY `FKs3xh9hr73dr02loe8emw5nb8p` (`employee_id`),
                                            CONSTRAINT `FKs3xh9hr73dr02loe8emw5nb8p` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;