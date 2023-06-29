CREATE TABLE `policy_search`.`Untitled`  (
    `id` int NOT NULL,
    `telphone` varchar(40) NOT NULL DEFAULT '',
    `password` varchar(200) NOT NULL DEFAULT '',
    `nick_name` varchar(40) NOT NULL DEFAULT '',
    `gender` int NOT NULL DEFAULT 0,
    `longitude` decimal(10, 6) NOT NULL DEFAULT 0.000000,
    `latitude` decimal(10, 6) NOT NULL DEFAULT 0.000000,
    `created_at` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
    `updated_at` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
    PRIMARY KEY (`id`)
);