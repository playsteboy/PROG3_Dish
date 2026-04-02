package org.example.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.Data.DishTypeEnum;

import java.util.List;

public class Dish {
    private Integer id;
    private Double selling_price;
    private String name;
    private DishTypeEnum dishType;
    @JsonIgnore
    private List<DishIngredient> dishIngredients;

    public Double getSelling_price() {
        return selling_price;
    }


    public void setSelling_price(Double price) {
        this.selling_price = price;
    }

    @JsonIgnore
    public Double getDishCost() {
        double totalPrice = 0;
        for (DishIngredient di: dishIngredients) {
            if(di.getQuantity_required() == null) {
                throw new RuntimeException("Some ingredients have undefined quantity");

            }
            totalPrice += di.getIngredient().getPrice() * di.getQuantity_required();
        }
        return totalPrice;
    }
    public Dish() {
    }

    public Dish(Integer id, String name, DishTypeEnum dishType) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }



    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", price=" + selling_price +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", dishIngredients=" + dishIngredients +
                '}';
    }

    @JsonIgnore
    public Double getGrossMargin() {
        if (selling_price == null) {
            throw new RuntimeException("Price is null");
        }
        return selling_price - getDishCost();
    }
}