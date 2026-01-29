package org.example;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Log before changes
        DataRetriever dataRetriever = new DataRetriever();
//        Dish dish = dataRetriever.findDishById(1
//
//        );
//        System.out.println(dish.getDishCost());

//        Ingredient ingredient = new Ingredient();
//        ingredient.setId(2);
//        List<DishIngredient> dishIngredients = new ArrayList<DishIngredient>();
//        Dish dish1 = new Dish(
//                6,
//                "soupe legume",
//                DishTypeEnum.MAIN,
//                new ArrayList<>()
//        );
//        dish1.setPrice(2000.0);
//        DishIngredient dishIngredient = new DishIngredient(
//                6,
//                dish1,
//                ingredient,
//                0.50,
//                Unit.KG
//        );
//        dishIngredients.add(dishIngredient);
//        System.out.println(dataRetriever.saveDish(dish1));
//        Ingredient ingredient2 = new Ingredient(
//                6,
//                "Carrotte",
//                CategoryEnum.VEGETABLE,
//                500.0
//        );
//        StockValue stockValue = new StockValue(
//                8.00,
//                Unit.KG
//        );
//        StockMovement stockMovement = new StockMovement(
//                11,
//                stockValue,
//                MovementTypeEnum.IN,
//                Instant.parse(Instant.now().toString())
//        );
//        List<StockMovement> stockMovements = new ArrayList<>();
//        stockMovements.add(stockMovement);
//        ingredient2.setStockMovementList(stockMovements);
//        System.out.println(dataRetriever.saveIngredient(ingredient2));

//        Ingredient ingredient3 = new Ingredient(
//                1,
//                "Laitue",
//                CategoryEnum.VEGETABLE,
//                800.00
//        );
//        List<StockMovement> stockMovements = new ArrayList<>();
//        dataRetriever.findStockMovementsByIngredientId(ingredient3.getId()).forEach(stockMovements::add);
//        ingredient3.setStockMovementList(stockMovements);
//        System.out.println(dataRetriever.saveIngredient(ingredient3).toString());
//        System.out.println(ingredient3.getStockValueAt(Instant.parse("2024-01-06T12:00:00Z")));
        List<DishIngredient> dishIngredients2 = new ArrayList<>();
        Dish dish2 = new Dish(
                7,
                "soupe legume",
                DishTypeEnum.MAIN,
                dishIngredients2
        );
        dish2.setPrice(2000.0);

        Ingredient ingredient4 = dataRetriever.findIngredientById(5);
        DishIngredient dishIngredient2 = new DishIngredient(
                7,
                dish2,
                ingredient4,
                0.6,
                Unit.KG
        );
        dishIngredients2.add(dishIngredient2);
        Order order = new Order(
                1,
                "ORD00001",
                Instant.now(),
                Command_type.EAT_IN,
                Command_stat.CREATED

        );
        DishOrder dishOrder = new DishOrder(
                1,
                dish2,
                10
        );
        dataRetriever.saveDish(dish2);
        List<DishOrder> dishOrders = new ArrayList<>();
        dishOrders.add(dishOrder);
        order.setDishOrderList(dishOrders);
        System.out.println(dataRetriever.saveOrder(order));
        // Log after changes
//        dish.setIngredients(List.of(new Ingredient(1), new Ingredient(2)));
//        Dish newDish = dataRetriever.saveDish(dish);
//        System.out.println(newDish);

        // Ingredient creations
        //List<Ingredient> createdIngredients = dataRetriever.createIngredients(List.of(new Ingredient(null, "Fromage", CategoryEnum.DAIRY, 1200.0)));
        //System.out.println(createdIngredients);
    }
}