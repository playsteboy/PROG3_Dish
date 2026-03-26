package org.example.Entities;

import jakarta.persistence.*;
import org.example.Data.Unit;

@Entity
@Table(name = "dishingredient")
public class DishIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_dish")
    private Dish dish;
    @ManyToOne
    @JoinColumn(name = "id_ingredient")
    private Ingredient ingredient;
    @Transient
    private Double quantity_required;
    @Transient
    @Enumerated(EnumType.STRING)
    Unit unit;

    public DishIngredient(Integer id, Dish dish, Ingredient ingredient, Double quantity_required, Unit unit) {
        this.id = id;
        this.dish = dish;
        this.ingredient = ingredient;
        this.quantity_required = quantity_required;
        this.unit = unit;
    }

    public DishIngredient() {

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

    public Double getQuantity_required() {
        return quantity_required;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantity_required(Double quantity_required) {
        this.quantity_required = quantity_required;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", dish=" + dish +
                ", ingredient=" + ingredient +
                ", quantity=" + quantity_required +
                ", unit=" + unit +
                '}';
    }
}
