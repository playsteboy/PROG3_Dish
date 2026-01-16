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