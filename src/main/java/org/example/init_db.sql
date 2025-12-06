create database product_management_db;
create user product_manager_user with password '123456';
grant connect on database product_management_db to product_manager_user;
grant usage,create on schema public to product_manager_user;
