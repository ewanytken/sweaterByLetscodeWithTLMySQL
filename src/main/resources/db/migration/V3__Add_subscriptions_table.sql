create table `sweaterdb`.`user_subscriptions`(
    channel_id bigint not null,
    subscriber_id bigint not null,
    primary key (channel_id, subscriber_id)
);

alter table  `sweaterdb`.`user_subscriptions`
    add constraint channel_id
        foreign key (channel_id) references user_table (id);
alter table  `sweaterdb`.`user_subscriptions`
    add constraint subscriber_id
        foreign key (subscriber_id) references user_table (id);