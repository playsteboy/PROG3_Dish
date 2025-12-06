package org.example;

import java.time.Instant;

public class Product {
    private int id;
    private String name;
    private Instant creationDatetime;
    private Category category;
    public Product(int id, String name, Instant creationDatetime, Category category) {
        this.id = id;
        this.name = name;
        this.creationDatetime = creationDatetime;
        this.category = category;
    }
    public String getCategoryName(){
        return category.getCategoryName();
    };
    @Override
    public String toString() {
        return "id: "+id+" name: "+name+" creationDatetime: "+creationDatetime+" category: "+category.getCategoryName();
    }
}
