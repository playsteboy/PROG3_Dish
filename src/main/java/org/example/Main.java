package org.example;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();
        try {
            for(int i = 0; i<dr.getAllCategories().size();i++){
                System.out.println(dr.getAllCategories().get(i).toString());
            }
            for(int i = 0; i<dr.getProductList (1,10 ) .size();i++){
                System.out.println(dr.getProductList (1,10 ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductList (1,5 ) .size();i++){
                System.out.println(dr.getProductList (1,5 ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductList (1,3 ) .size();i++){
                System.out.println(dr.getProductList (1,3 ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductList (2,2 ) .size();i++){
                System.out.println(dr.getProductList (2,2 ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria("Dell",null ,null ,null ) .size();i++){
                System.out.println(dr.getProductsByCriteria("Dell",null ,null ,null ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria(null,"info" ,null ,null ) .size();i++){
                System.out.println(dr.getProductsByCriteria(null,"info" ,null ,null ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria("iphone","mobile" ,null ,null ) .size();i++){
                System.out.println(dr.getProductsByCriteria("iphone","mobile" ,null ,null ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria(null,null , Instant.parse("2024-02-01"+"T00:00:00Z") ,Instant.parse("2024-03-01"+"T00:00:00Z") ) .size(); i++){
                System.out.println(dr.getProductsByCriteria(null,null , Instant.parse("2024-02-01"+"T00:00:00Z") ,Instant.parse("2024-03-01"+"T00:00:00Z")).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria("Samsung","bureau" , null ,null ) .size(); i++){
                System.out.println(dr.getProductsByCriteria("Samsung","bureau" , null ,null ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria("Sony","informatique" , null ,null ) .size(); i++){
                System.out.println(dr.getProductsByCriteria("Sony","informatique" , null ,null ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria(null,"audio" , Instant.parse("2024-01-01"+"T00:00:00Z") ,Instant.parse("2024-12-01"+"T00:00:00Z") ) .size(); i++){
                System.out.println(dr.getProductsByCriteria(null,"audio" , Instant.parse("2024-01-01"+"T00:00:00Z") ,Instant.parse("2024-12-01"+"T00:00:00Z") ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria(null,null ,null ,null ) .size();i++){
                System.out.println(dr.getProductsByCriteria(null,null ,null ,null ).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria(null,null ,null ,null ,1,10) .size();i++){
                System.out.println(dr.getProductsByCriteria(null,null ,null ,null ,1,10).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria("Dell",null ,null ,null ,1,5) .size();i++){
                System.out.println(dr.getProductsByCriteria("Dell",null ,null ,null ,1,5).get(i).toString());
            }
            for(int i = 0; i<dr.getProductsByCriteria(null,"informatique" ,null ,null ,1,10) .size();i++){
                System.out.println(dr.getProductsByCriteria(null,"informatique" ,null ,null ,1,10).get(i).toString());
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
}