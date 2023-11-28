# DROP DATABASE `SPRINGDB`;
# SHOW CREATE DATABASE `SPRINGDB`;
# DROP USER `SPRINGUSER`@'YOUR_IP_RESP_TO_MYSQL_SERVER';
# FLUSH PRIVILEGES;
# CREATE USER `SPRINGUSER`@`YOUR_IP_RESP_TO_MYSQL_SERVER` IDENTIFIED BY 'password';
# GRANT ALL PRIVILEGES ON `SPRINGDB`.* TO `SPRINGUSER`@`YOUR_IP_RESP_TO_MYSQL_SERVER`;

# create table cash_card_seq (next_val bigint) engine=InnoDB;
# insert into cash_card_seq values ( 1 );
# create table cash_card (id bigint not null, amount float(53), owner varchar(255), primary key (id)) engine=InnoDB;
# create table event (id bigint not null, name varchar(255), owner bigint, start_time datetime(6), primary key (id)) engine=InnoDB;
# create table event_seq (next_val bigint) engine=InnoDB;
# insert into event_seq values ( 1 );
# create table user (id bigint not null, hashword varchar(255), name varchar(255), username varchar(255), primary key (id)) engine=InnoDB;
# create table user_seq (next_val bigint) engine=InnoDB;
# insert into user_seq values ( 1 );
