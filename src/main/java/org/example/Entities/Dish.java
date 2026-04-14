package org.example.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.Data.DishTypeEnum;

import java.util.List;

public class Dish {
    private Integer id;
    private Double sellingPrice;
    private String name;
    private DishTypeEnum dishType;
    @JsonIgnore
    private List<DishIngredient> dishIngredients;

    public Double getSellingPrice() {
        return sellingPrice;
    }


    public void setSellingPrice(Double price) {
        this.sellingPrice = price;
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
                ", price=" + sellingPrice +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", dishIngredients=" + dishIngredients +
                '}';
    }

    @JsonIgnore
    public Double getGrossMargin() {
        if (sellingPrice == null) {
            throw new RuntimeException("Price is null");
        }
        return sellingPrice - getDishCost();
    }
}