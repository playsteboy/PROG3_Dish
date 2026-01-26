package org.example;

public class DishIngredient {
    private Integer id;
    private  Dish dish;
    private  Ingredient ingredient;
    private Double quantity;
    Unit unit;

    public DishIngredient(Integer id, Dish dish, Ingredient ingredient, Double quantity, Unit unit) {
        this.id = id;
        this.dish = dish;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Integer getId() {
        return id;
    }

    public Dish getDish() {
        return dish;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", dish=" + dish +
                ", ingredient=" + ingredient +
                ", quantity=" + quantity +
                ", unit=" + unit +
                '}';
    }
}
