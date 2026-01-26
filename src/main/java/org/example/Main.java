package org.example;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Log before changes
        DataRetriever dataRetriever = new DataRetriever();
        Dish dish = dataRetriever.findDishById(1

        );
        System.out.println(dish.getDishCost());

//        Ingredient ingredient = new Ingredient();
//        ingredient.setId(2);
//        List<DishIngredient> dishIngredients = new ArrayList<DishIngredient>();
//        Dish dish1 = new Dish(
//                6,
//                "soupe legume",
//                DishTypeEnum.MAIN,
//                new ArrayList<>()
//        );
//        DishIngredient dishIngredient = new DishIngredient(
//                6,
//                dish1,
//                ingredient,
//                0.50,
//                Unit.KG
//        );
//        dishIngredients.add(dishIngredient);
//        dataRetriever.saveDish(dish1);
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
//                MovementTypeEnum.OUT,
//                Instant.parse(Instant.now().toString())
//        );
//        ingredient2.setStockMovementList(new ArrayList<>());
//        dataRetriever.saveIngredient(ingredient2);

//        Ingredient ingredient3 = new Ingredient(
//                5,
//                "Beurre",
//                CategoryEnum.DAIRY,
//                2500.00
//        );
//        List<StockMovement> stockMovements = new ArrayList<>();
//        dataRetriever.findStockMovementsByIngredientId(ingredient3.getId()).forEach(stockMovements::add);
//        ingredient3.setStockMovementList(stockMovements);
//        dataRetriever.saveIngredient(ingredient3);
//        System.out.println(ingredient3.getStockValueAt(Instant.parse("2024-01-06T12:00:00Z")));
        // Log after changes
//        dish.setIngredients(List.of(new Ingredient(1), new Ingredient(2)));
//        Dish newDish = dataRetriever.saveDish(dish);
//        System.out.println(newDish);

        // Ingredient creations
        //List<Ingredient> createdIngredients = dataRetriever.createIngredients(List.of(new Ingredient(null, "Fromage", CategoryEnum.DAIRY, 1200.0)));
        //System.out.println(createdIngredients);
    }
}