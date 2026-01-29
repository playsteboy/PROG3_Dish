create type unit_type as enum ('PCS', 'KG', 'L');

create table DishIngredient(
    id       serial primary key,
    id_dish int references dish(id),
    id_ingredient int references ingredient(id),
    quantity_required numeric (10, 2),
    unit unit_type
);

alter table ingredient drop column id_dish;

alter table dish drop column price;

alter table dish add column selling_price numeric (10,2);

alter table ingredient drop column required_quantity;

create type movement_type as enum ('IN','OUT');
create table StockMovement(
    id       serial primary key,
    id_ingredient int references ingredient(id),
    quantity numeric (10, 2),
    type movement_type,
    unit unit_type,
    creation_datetime timestamp
);

create table if not exists "order"
(
    id                serial primary key,
    reference         varchar(255),
    creation_datetime timestamp without time zone
    );

create table if not exists dish_order
(
    id       serial primary key,
    id_order int references "order" (id),
    id_dish  int references dish (id),
    quantity int
    );

create type command_type as enum ('EAT_IN','TAKE_AWAY');
create type command_stat as enum ('CREATED','READY','DELIVERED');

alter table "order" add column "type" command_type;
alter table "order" add column "stat" command_stat;

