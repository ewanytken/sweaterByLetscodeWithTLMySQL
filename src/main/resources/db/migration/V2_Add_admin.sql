INSERT INTO `sweaterdb`.`user_table` (`id`, `user_active`, `user_name`, `user_password`)
VALUES (1, true, 'admin', 'qqq');

INSERT INTO `sweaterdb`.`user_role` (`user_id`, `roles`) VALUES (1, 'ADMIN');
