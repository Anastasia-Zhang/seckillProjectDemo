create database User;
create database miaosha;
use miaosha;

create table user_info
(
id int primary  key auto_increment,
name varchar(20) not null default ' ',
gender tinyint not null default 0,-- 0boy 1 girl
age int default 0 not null,
telphone varchar(20) not null default ' ',
register_mode varchar(20) not null default ' ',-- byphone,bywechat,byalipay
third_party_id varchar(20) not null default ' '
);

create table user_password
(
id int primary key auto_increment,
encrpt_password varchar(30) not null default ' ',
user_id int not null default 0,
foreign key(user_id) references user_info(id)
);
ALTER TABLE `miaosha`.`user_info` 
ADD UNIQUE INDEX `telphone_unique_index` (`telphone` ASC) VISIBLE;
;

CREATE TABLE `miaosha`.`item` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(64) NOT NULL DEFAULT ' ',
  `price` DECIMAL NOT NULL DEFAULT 0,
  `description` VARCHAR(500) NOT NULL DEFAULT ' ',
  `sales` INT NOT NULL DEFAULT 0,
  `img_url` VARCHAR(500) NOT NULL DEFAULT ' ',
  PRIMARY KEY (`id`));
  
  CREATE TABLE `miaosha`.`item_stock` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `stock` INT NOT NULL DEFAULT 0,
  `item_id` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`));
  
  ALTER TABLE `miaosha`.`item` 
CHANGE COLUMN `price` `price` DOUBLE NULL DEFAULT '0' ;

CREATE TABLE `miaosha`.`order_info` (
  `id` VARCHAR(32) NOT NULL DEFAULT ' ',
  `user_id` INT NOT NULL DEFAULT 0,
  item_id int not null default 0,
  `item_price` DOUBLE NOT NULL DEFAULT 0,
  `amount` INT NOT NULL DEFAULT 0,
  `order_price`DOUBLE NOT NULL DEFAULT 0,
  promo_id int not null default 0,  
  PRIMARY KEY (`id`));
  
  CREATE TABLE `miaosha`.`sequence_info` (
  `name` VARCHAR(45) NOT NULL DEFAULT ' ',
  `current_value` INT NOT NULL DEFAULT 0,
  `step` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`name`));
  
  CREATE TABLE `miaosha`.`promo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `promo_name` VARCHAR(200) NOT NULL DEFAULT ' ',
  `start_time` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00',
  `end_date` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00',
  `item_id` INT NOT NULL DEFAULT 0,
  `promo_item_price` DOUBLE NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`));