create table Product(id int primary key,name varchar(100),price numeric,creation_datetime timestamp);
create table Product_category(id int primary key,name varchar(100),product_id int,constraint fk_Product_category
foreign key (product_id) references Product(id));