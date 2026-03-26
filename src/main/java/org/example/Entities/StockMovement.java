package org.example.Entities;

import jakarta.persistence.*;
import org.example.Data.MovementTypeEnum;

import java.time.Instant;
@Entity
@Table(name = "stockmovement")
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    private StockValue value;
    @Enumerated(EnumType.STRING)
    private MovementTypeEnum type;
    private Instant creationDatetime;

    public StockMovement(){}
    public StockMovement(Integer id, StockValue value, MovementTypeEnum type, Instant creationDatetime) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.creationDatetime = creationDatetime;
    }

    public Integer getId() {
        return id;
    }

    public StockValue getValue() {
        return value;
    }

    public MovementTypeEnum getType() {
        return type;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", value=" + value +
                ", type=" + type +
                ", creationDatetime=" + creationDatetime +
                '}';
    }
}
