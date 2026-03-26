package org.example.Entities;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.example.Data.Unit;

public class StockValue {
    private Double quantity;
    @Enumerated(EnumType.STRING)
    private Unit unit;
    public StockValue(Double quantity, Unit unit) {
        this.quantity = quantity;
        this.unit = unit;
    }
    public StockValue(){

    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }


    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "StockValue{" +
                "quantity=" + quantity +
                ", unit=" + unit +
                '}';
    }
}
