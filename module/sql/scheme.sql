CREATE TABLE `job` (
  `job_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `code` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `time_expression` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `reg_time` datetime NOT NULL,
  `mod_time` datetime NOT NULL,
  `state` varchar(8) COLLATE utf8_unicode_ci NOT NULL COMMENT 'USE',
  `is_running` char(1) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Y',
  `last_tick_time` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `job_history` (
  `job_history_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `job_id` int(10) unsigned NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `finish_time` datetime DEFAULT NULL,
  `state` varchar(16) COLLATE utf8_unicode_ci NOT NULL COMMENT 'new',
  `result` text COLLATE utf8_unicode_ci,
  `run_ip` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`job_history_id`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
