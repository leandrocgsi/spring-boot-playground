CREATE TABLE IF NOT EXISTS `permission` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;