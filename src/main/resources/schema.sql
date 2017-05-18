CREATE TABLE IF NOT EXISTS `config` (
  `id`    INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `key`   VARCHAR(255)              DEFAULT NULL,
  `value` LONGTEXT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_option_key` (`key`) USING HASH
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '设置';

CREATE TABLE IF NOT EXISTS `sort` (
  `id`     INT(11) UNSIGNED           NOT NULL AUTO_INCREMENT,
  `name`   VARCHAR(255)               NOT NULL,
  `parent` INT(11) UNSIGNED                    DEFAULT '0',
  `type`   ENUM ('ARTICLE', 'NORMAL') NOT NULL DEFAULT 'NORMAL',
  `taxis`  INT(11) UNSIGNED                    DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index_sort_parent` (`parent`) USING HASH
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '文章分类';

CREATE TABLE IF NOT EXISTS `user` (
  `id`       INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `email`    VARCHAR(255)     NOT NULL,
  `password` VARCHAR(255)     NOT NULL,
  `nick`     VARCHAR(255)              DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_user_email` (`email`) USING HASH
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '用户表';

CREATE TABLE IF NOT EXISTS `login_log` (
  `id`   INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user` INT(11) UNSIGNED NOT NULL,
  `ip`   VARCHAR(128)     NOT NULL,
  `time` DATETIME                  DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_login_log_user_id` (`user`),
  CONSTRAINT `fk_login_log_user_id` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '登录记录';

CREATE TABLE IF NOT EXISTS `article` (
  `id`          INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`       VARCHAR(255)     NOT NULL,
  `tag`         VARCHAR(255)              DEFAULT NULL,
  `image`       VARCHAR(255)              DEFAULT NULL,
  `content`     LONGTEXT         NOT NULL,
  `sort`        INT(11) UNSIGNED          DEFAULT '0',
  `author`      INT(11) UNSIGNED NOT NULL,
  `create_time` DATETIME                  DEFAULT CURRENT_TIMESTAMP,
  `views`       INT(11) UNSIGNED          DEFAULT '0',
  `publish`     TINYINT(1)                DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_article_sort` (`sort`) USING HASH,
  KEY `index_article_author` (`author`) USING HASH,
  CONSTRAINT `fk_article_author` FOREIGN KEY (`author`) REFERENCES `user` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '文章';
