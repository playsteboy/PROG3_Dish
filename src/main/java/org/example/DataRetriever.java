package org.example;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try(Connection connection = DBConnection.getDBConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id,name from Product_category ")){
            while (resultSet.next()) {
                Category c = new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
                categories.add(c);
            }

        }
        catch(SQLException e){
            throw e;
        }

        return categories;
    }
    public List<Product> getProductList (int page, int size) throws SQLException {
        int offset = (page - 1) * size;
        List<Product> products = new ArrayList<>();
        try(Connection connection = DBConnection.getDBConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select Product_category.id as pc_id,Product_category.name as pc_name,Product.id as id,Product.name as name,Product.creation_datetime as creation_datetime  from Product_category inner join Product on Product_category.product_id=Product.id order by Product.id limit ? offset ? "
        )){
            statement.setInt(1,size);
            statement.setInt(2,offset);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Category c = new Category(
                        resultSet.getInt("pc_id"),
                        resultSet.getString("pc_name")
                );
                Product p = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getTimestamp("creation_datetime").toInstant(),
                        c

                );
                products.add(p);
            }
        }
        catch(SQLException e){
            throw e;
        }
        return products;
    }
    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax) throws SQLException {
        List<Product> products = new ArrayList<>();
        String req = "select distinct Product_category.id as pc_id,Product_category.name as pc_name,Product.id as p_id,Product.name as p_name,Product.creation_datetime as p_creation_datetime  from Product_category inner join Product on Product_category.product_id=Product.id where 1=1 ";
        if(productName != null && !productName.isEmpty()){
            req = req+" and Product.name ilike'%"+productName+"%'";
        }
        if(categoryName != null && !categoryName.isEmpty()){
            req = req+" and Product_category.name ilike'%"+categoryName+"%'";
        }
        if(creationMin != null){
            req = req+" and Product.creation_datetime >='"+creationMin.toString()+"' ";
        }
        if(creationMax != null){
            req = req+" and Product.creation_datetime <='"+creationMax.toString()+"' ";
        }
        try(Connection connection = DBConnection.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(req);
            ResultSet resultSet = statement.executeQuery()){
            while(resultSet.next()){
                Category c = new Category(
                        resultSet.getInt("pc_id"),
                        resultSet.getString("pc_name")
                );
                Product p = new Product(
                        resultSet.getInt("p_id"),
                        resultSet.getString("p_name"),
                        resultSet.getTimestamp("p_creation_datetime").toInstant(),
                        c

                );
                products.add(p);
            }
        }
        catch(SQLException e){
            throw e;
        }
        return products;
    }
    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax, int page, int size) throws SQLException {
        int offset = (page - 1) * size;
        List<Product> products = new ArrayList<>();
        String req = "select distinct Product_category.id as pc_id,Product_category.name as pc_name,Product.id as p_id,Product.name as p_name,Product.creation_datetime as p_creation_datetime  from Product_category inner join Product on Product_category.product_id=Product.id where 1=1 ";
        if(productName != null && !productName.isEmpty()){
            req = req+" and Product.name ilike'%"+productName+"%'";
        }
        if(categoryName != null && !categoryName.isEmpty()){
            req = req+" and Product_category.name ilike'%"+categoryName+"%'";
        }
        if(creationMin != null){
            req = req+" and Product.creation_datetime >='"+creationMin.toString()+"' ";
        }
        if(creationMax != null){
            req = req+" and Product.creation_datetime <='"+creationMax.toString()+"' ";
        }
        req = req+" order by Product.id limit ? offset ? ";
        try(Connection connection = DBConnection.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(req);
            ){

            statement.setInt(1,size);
            statement.setInt(2,offset);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Category c = new Category(
                        resultSet.getInt("pc_id"),
                        resultSet.getString("pc_name")
                );
                Product p = new Product(
                        resultSet.getInt("p_id"),
                        resultSet.getString("p_name"),
                        resultSet.getTimestamp("p_creation_datetime").toInstant(),
                        c

                );
                products.add(p);
            }
        }
        catch(SQLException e){
            throw e;
        }
        return products;
    }
}
