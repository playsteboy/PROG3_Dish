package org.example;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Integer id;
    private String reference;
    private Instant creationDatetime;
    private List<DishOrder> dishOrders;
    private Command_type type;
    private  Command_stat stat;

    public Order(int id, String reference, Instant creationDatetime,Command_type type, Command_stat stat) {
        this.id = id;
        this.reference = reference;
        this.creationDatetime = creationDatetime;
        this.type = type;
        this.stat = stat;
    }

    public Order() {
    }

    public Double getTotalAmountWithoutVAt() {
        if (dishOrders == null || dishOrders.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (DishOrder d : dishOrders) {
            if (d.getDish() != null && d.getDish().getPrice() != null) {
                total += d.getDish().getPrice() * d.getQuantity();
            }
        }
        return total;
    }

    public Double getTotalAmountWithVAt() {
        Double brut = getTotalAmountWithoutVAt();
        return brut != null ? brut * 1.20 : 0.0;
    }

    public Command_type getType() {
        return type;
    }

    public Command_stat getStat() {
        return stat;
    }

    public void setDishOrders(List<DishOrder> dishOrders) {
        this.dishOrders = dishOrders;
    }

    public void setType(Command_type type) {
        this.type = type;
    }

    public void setStat(Command_stat stat) {
        this.stat = stat;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public List<DishOrder> getDishOrders() {
        return dishOrders;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public void setDishOrderList(List<DishOrder> dishOrders) {
        this.dishOrders = dishOrders;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", creationDatetime=" + creationDatetime +
                ", dishOrders=" + dishOrders +
                ", type=" + type +
                ", stat=" + stat +
                '}';
    }
}
