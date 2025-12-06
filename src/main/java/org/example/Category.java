package org.example;

public class Category {
    private int id;
    private String name;
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getCategoryName() {
        return name;
    }

    @Override
    public String toString() {
        return "id:"+id+" name: "+name;
    }
}
