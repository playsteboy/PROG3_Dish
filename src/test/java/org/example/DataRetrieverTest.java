package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Instant;

public class DataRetrieverTest {
    private DataRetriever dataRetriever;
    @BeforeEach
    public void setUp(){
        dataRetriever = new DataRetriever();
    }
    @Test
    public void testGetAllCategories() throws SQLException
    {
        Assertions.assertEquals(7,dataRetriever.getAllCategories().size());
    }
    @Test
    public void testGetProductList() throws SQLException
    {
        Assertions.assertEquals(7,dataRetriever.getProductList(1,10).size());
        Assertions.assertEquals(5,dataRetriever.getProductList(1,5).size());
        Assertions.assertEquals(3,dataRetriever.getProductList(1,3).size());
        Assertions.assertEquals(2,dataRetriever.getProductList(2,2).size());
    }
    @Test
    public void testGetProductsByCriteriaDell() throws SQLException
    {
        Assertions.assertTrue(dataRetriever.getProductsByCriteria("Dell",null ,null ,null ).getFirst().toString().contains("Laptop Dell XPS"));

    }
    @Test
    public void  testGetProductsByCriteria() throws SQLException
    {
        Assertions.assertTrue(dataRetriever.getProductsByCriteria("Dell",null ,null ,null ).getFirst().toString().contains("Laptop Dell XPS"));
        Assertions.assertEquals(2,dataRetriever.getProductsByCriteria(null,"info" ,null ,null ).size());
        Assertions.assertTrue(dataRetriever.getProductsByCriteria("iphone","mobile" ,null ,null ).getFirst().toString().contains("iPhone 13"));
        Assertions.assertEquals(3,dataRetriever.getProductsByCriteria(null,null , Instant.parse("2024-02-01"+"T00:00:00Z") ,Instant.parse("2024-03-01"+"T00:00:00Z") ).size());
        Assertions.assertTrue(dataRetriever.getProductsByCriteria("Samsung","bureau" , null ,null ).getFirst().toString().contains("Ecran Samsung 27"));
        Assertions.assertEquals(0,dataRetriever.getProductsByCriteria("Sony","informatique" , null ,null ).size());
        Assertions.assertEquals(1,dataRetriever.getProductsByCriteria(null,"audio" , Instant.parse("2024-01-01"+"T00:00:00Z") ,Instant.parse("2024-12-01"+"T00:00:00Z") ).size());
        Assertions.assertEquals(7,dataRetriever.getProductsByCriteria(null,null ,null ,null ).size());
    }
    @Test
    public void testGetProductsByCriteriaBook() throws SQLException{
        Assertions.assertEquals(7,dataRetriever.getProductsByCriteria(null,null ,null ,null ,1,10).size());
        Assertions.assertTrue(dataRetriever.getProductsByCriteria("Dell",null ,null ,null ,1,5).getFirst().toString().contains("Laptop Dell XPS"));
        Assertions.assertEquals(2,dataRetriever.getProductsByCriteria(null,"informatique" ,null ,null ,1,10).size());
    }

}
