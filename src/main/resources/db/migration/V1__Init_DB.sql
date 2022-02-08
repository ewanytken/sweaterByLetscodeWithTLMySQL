 create table `hibernate_sequence` (
                                       next_val INTEGER NOT NULL
                                   );
 create table `sweaterdb`.`message` (
                                        id bigint not null,
                                        filename varchar(255),
                                        tag varchar(255),
                                        text varchar(255),
                                        user_id bigint,
                                        primary key (id));

 create table `sweaterdb`.`user_role` (
                                          user_id bigint not null,
                                          roles varchar(255));

 create table `sweaterdb`.`user_table` (
                                           id bigint not null,
                                           activation_code varchar(255),
                                           user_active bit,
                                           email varchar(255),
                                           user_password varchar(255),
                                           user_name varchar(255),
                                           primary key (id));

 alter table  message
     add constraint message_user_fk
         foreign key (user_id) references user_table (id);
 alter table user_role
     add constraint user_role_user_fk
         foreign key (user_id) references user_table (id);