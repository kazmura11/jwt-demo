CREATE DATABASE demo;

USE `demo`;

CREATE TABLE IF NOT EXISTS `users` (
    `user_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ユーザーID',
    `email_address` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'メールアドレス',
    `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'ユーザー名',
    `password` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'メールアドレス',
    `admin_flag` TINYINT(1) NOT NULL DEFAULT false COMMENT '管理者フラグ',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '削除フラグ',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY (`user_id`)
) COLLATE='utf8mb4_general_ci' ENGINE=InnoDB
;

INSERT INTO `users` 
    (`email_address`, `name`, `password`, `admin_flag`, `deleted`, `created_at`, `updated_at`) VALUES
    ('demo@example.com', 'demo', '$2a$10$v5AgiYxe0voqQ22/OVgro.cORv9C9bDdU2i/ZlUgkKXtKZ74l7Mri', '1', '0', NOW(), NOW())
;
