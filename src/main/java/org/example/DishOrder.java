package org.example;

public class DishOrder {
    private Integer id;
    private Dish dish;
    private Integer quantity;

    public DishOrder(Integer id, Dish dish, Integer quantity) {
        this.id = id;
        this.dish = dish;
        this.quantity = quantity;
    }

    public DishOrder() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public Dish getDish() {
        return dish;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "DishOrder{" +
                "id=" + id +
                ", dish=" + dish +
                ", quantity=" + quantity +
                '}';
    }
}
