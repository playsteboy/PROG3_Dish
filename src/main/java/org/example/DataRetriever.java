package org.example;

import java.sql.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataRetriever {
    Dish findDishById(Integer id) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        try {
            PreparedStatement psDish = connection.prepareStatement(
                    "SELECT id, name, dish_type, selling_price FROM dish WHERE id = ?"
            );
            psDish.setInt(1, id);
            ResultSet rsDish = psDish.executeQuery();

            if (rsDish.next()) {
                Dish dish = new Dish();
                dish.setId(rsDish.getInt("id"));
                dish.setName(rsDish.getString("name"));
                dish.setDishType(DishTypeEnum.valueOf(rsDish.getString("dish_type")));
                double pricePrimitive = rsDish.getDouble("selling_price");
                if (rsDish.wasNull()) {
                    dish.setPrice(null);
                } else {
                    dish.setPrice(pricePrimitive);
                }
                List<DishIngredient> ingredients = findDishIngredientByDishId(id);
                dish.setDishIngredients(ingredients);

                dbConnection.closeConnection(connection);
                return dish;
            }

            dbConnection.closeConnection(connection);
            throw new RuntimeException("Dish not found " + id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Dish saveDish(Dish toSave) {
        Connection conn = new DBConnection().getConnection();
        String upsertDishSql = """
                    INSERT INTO dish (id, selling_price, name, dish_type)
                    VALUES (?, ?, ?, ?::dish_type)
                    ON CONFLICT (id) DO UPDATE
                    SET name = EXCLUDED.name,
                        dish_type = EXCLUDED.dish_type,
                        selling_price = EXCLUDED.selling_price
                    RETURNING id
                """;

        try {
            conn.setAutoCommit(false);
            Integer dishId;
            try (PreparedStatement ps = conn.prepareStatement(upsertDishSql)) {
                if (toSave.getId() != null) {
                    ps.setInt(1, toSave.getId());
                } else {
                    ps.setInt(1, getNextSerialValue(conn, "dish", "id"));
                }
                if (toSave.getPrice() != null) {
                    ps.setDouble(2, toSave.getPrice());
                } else {
                    ps.setNull(2, Types.DOUBLE);
                }
                ps.setString(3, toSave.getName());
                ps.setString(4, toSave.getDishType().name());
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    dishId = rs.getInt(1);
                }
            }
            if(toSave.getDishIngredients() != null) {
                if(toSave.getDishIngredients().isEmpty()){
                    PreparedStatement psDelete = conn.prepareStatement("Delete from dishingredient WHERE id_dish = ?");
                    psDelete.setInt(1, dishId);
                    psDelete.executeUpdate();
                }
                else{
                    PreparedStatement psDI = conn.prepareStatement("""
                INSERT INTO DishIngredient(id,id_dish, id_ingredient, quantity_required, unit) values (?,?, ?, ?, ?::unit_type)
                on conflict (id) do update set quantity_required=excluded.quantity_required, unit=excluded.unit
                """);
                    for(int i = 0; i < toSave.getDishIngredients().size(); i++) {
                        psDI.setInt(1, toSave.getDishIngredients().get(i).getId());
                        psDI.setInt(2, toSave.getDishIngredients().get(i).getDish().getId());
                        psDI.setInt(3, toSave.getDishIngredients().get(i).getIngredient().getId());
                        psDI.setDouble(4, toSave.getDishIngredients().get(i).getQuantity());
                        psDI.setString(5, toSave.getDishIngredients().get(i).getUnit().name());
                        psDI.addBatch();
                    }
                    int[] result = psDI.executeBatch();
                }
            }
            conn.commit();
            conn.close();
            return findDishById(dishId);
        } catch (SQLException e) {
            if(conn!=null){
                try {
                    conn.close();
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
        if (newIngredients == null || newIngredients.isEmpty()) {
            return List.of();
        }
        List<Ingredient> savedIngredients = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            String insertSql = """
                        INSERT INTO ingredient (id, name, category, price)
                        VALUES (?, ?, ?::ingredient_category, ?)
                        RETURNING id
                    """;
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                for (Ingredient ingredient : newIngredients) {
                    if (ingredient.getId() != null) {
                        ps.setInt(1, ingredient.getId());
                    } else {
                        ps.setInt(1, getNextSerialValue(conn, "ingredient", "id"));
                    }
                    ps.setString(2, ingredient.getName());
                    ps.setString(3, ingredient.getCategory().name());
                    ps.setDouble(4, ingredient.getPrice());
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        int generatedId = rs.getInt(1);
                        ingredient.setId(generatedId);
                        savedIngredients.add(ingredient);
                    }
                }
                conn.commit();
                conn.close();
                return savedIngredients;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    private List<DishIngredient> findDishIngredientByDishId(Integer idDish) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        List<DishIngredient> dishIngredients = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
    SELECT DishIngredient.id as di_id,DishIngredient.quantity_required as di_qr, DishIngredient.unit as di_unit,
           Ingredient.id as i_id,Ingredient.name as i_name,Ingredient.category as i_c,Ingredient.price as i_price,
           Dish.id as d_id, Dish.name as d_name, Dish.dish_type as d_dt, Dish.selling_price as d_sp from DishIngredient 
        JOIN ingredient ON dishingredient.id_ingredient = ingredient.id
        JOIN dish ON dishingredient.id_dish = dish.id              
    where DishIngredient.id_dish = ?
    """);
            preparedStatement.setInt(1, idDish);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(resultSet.getString("d_dt"));
                CategoryEnum categoryEnum = CategoryEnum.valueOf(resultSet.getString("i_c"));

                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("i_id"));
                ingredient.setName(resultSet.getString("i_name"));
                ingredient.setCategory(categoryEnum);
                double pricePrimitive = resultSet.getDouble("i_price");
                if (resultSet.wasNull()) {
                    ingredient.setPrice(null);
                } else {
                    ingredient.setPrice(pricePrimitive);
                }
                ingredient.setStockMovementList(findStockMovementsByIngredientId(ingredient.getId()));
                Dish dish = new Dish(
                        resultSet.getInt("d_id"),
                        resultSet.getString("d_name"),
                        dishTypeEnum,
                        new ArrayList<>()
                );
                double sellingPricePrimitive = resultSet.getDouble("d_sp");
                if (resultSet.wasNull()) {
                    dish.setPrice(null);
                } else {
                    dish.setPrice(sellingPricePrimitive);
                }
                Unit unit = Unit.valueOf(resultSet.getString("di_unit"));
                DishIngredient dishIngredient = new DishIngredient(
                        resultSet.getInt("di_id"),
                        dish,
                        ingredient,
                        resultSet.getDouble("di_qr"),
                        unit
                );
                dishIngredients.add(dishIngredient);
            }
            dbConnection.closeConnection(connection);
            return dishIngredients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private String getSerialSequenceName(Connection conn, String tableName, String columnName)
            throws SQLException {

        String sql = "SELECT pg_get_serial_sequence(?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    private int getNextSerialValue(Connection conn, String tableName, String columnName)
            throws SQLException {

        String sequenceName = getSerialSequenceName(conn, tableName, columnName);
        if (sequenceName == null) {
            throw new IllegalArgumentException(
                    "Any sequence found for " + tableName + "." + columnName
            );
        }
        updateSequenceNextValue(conn, tableName, columnName, sequenceName);

        String nextValSql = "SELECT nextval(?)";

        try (PreparedStatement ps = conn.prepareStatement(nextValSql)) {
            ps.setString(1, sequenceName);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private void updateSequenceNextValue(Connection conn, String tableName, String columnName, String sequenceName) throws SQLException {
        String setValSql = String.format(
                "SELECT setval('%s', (SELECT COALESCE(MAX(%s), 0) FROM %s))",
                sequenceName, columnName, tableName
        );

        try (PreparedStatement ps = conn.prepareStatement(setValSql)) {
            ps.executeQuery();
        }
    }

    public Ingredient  saveIngredient(Ingredient toSave){
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        try{
            connection.setAutoCommit(false);
            String sql = """
INSERT INTO ingredient (id, name, price, category) 
VALUES (?, ?, ?, ?::ingredient_category)
ON CONFLICT (id) DO UPDATE SET 
    name = EXCLUDED.name, 
    price = EXCLUDED.price, 
    category = EXCLUDED.category
    """;
            PreparedStatement psIngredient = connection.prepareStatement(sql);
            psIngredient.setInt(1, toSave.getId());
            psIngredient.setString(2, toSave.getName());
            psIngredient.setDouble(3, toSave.getPrice());
            psIngredient.setString(4, toSave.getCategory().toString());
            psIngredient.executeUpdate();
            if(toSave.getStockMovementList()!=null){
                    PreparedStatement psStock = connection.prepareStatement(
                            "insert into stockmovement (id, id_ingredient, quantity, type, unit, creation_datetime) " +
                                    "values (?,?,?,?::movement_type,?::unit_type,?) on conflict(id) do nothing");
                    for (StockMovement sm : toSave.getStockMovementList()) {
                        psStock.setInt(1, sm.getId());
                        psStock.setInt(2, toSave.getId());
                        psStock.setDouble(3, sm.getValue().getQuantity());
                        psStock.setString(4, sm.getType().toString());
                        psStock.setString(5, sm.getValue().getUnit().toString());
                        psStock.setTimestamp(6, Timestamp.from(sm.getCreationDatetime()));
                        psStock.addBatch();
                    }
                    int[] result = psStock.executeBatch();
            }
            connection.commit();
            connection.close();
            return findIngredientById(toSave.getId());
        }catch (SQLException e){
            if(connection != null){
                try{
                    connection.close();
                    connection.rollback();
                }catch(SQLException e1){
                    throw new  RuntimeException(e1);
                }
            }
            throw new RuntimeException(e);
        }
    }
    public Ingredient findIngredientById(Integer id) {
        DBConnection dbConnection = new DBConnection();
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select id, name, price, category from ingredient where id = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idIngredient = resultSet.getInt("id");
                String name = resultSet.getString("name");
                CategoryEnum category = CategoryEnum.valueOf(resultSet.getString("category"));
                Double price = resultSet.getDouble("price");
                Ingredient ingredient = new Ingredient(idIngredient, name, category, price);
                ingredient.setStockMovementList(findStockMovementsByIngredientId(idIngredient));
                return ingredient;
            }
            throw new RuntimeException("Ingredient not found " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StockMovement> findStockMovementsByIngredientId(int id_ingredient){
        try{
            List<StockMovement> list = new ArrayList<>();
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("""
select id,id_ingredient,quantity,type,unit,creation_datetime
from stockmovement where id_ingredient=?
    """);
            ps.setInt(1, id_ingredient);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                StockValue stockValue = new StockValue();
                double quantityPrimitive = rs.getDouble("quantity");
                if (rs.wasNull()) {
                    stockValue.setQuantity(null);
                } else {
                    stockValue.setQuantity(quantityPrimitive);
                }
                stockValue.setUnit(Unit.valueOf(rs.getString("unit")));
                StockMovement stockMovement = new StockMovement(
                        rs.getInt("id"),
                        stockValue,
                        MovementTypeEnum.valueOf(rs.getString("type")),
                        rs.getTimestamp("creation_datetime").toInstant()
                );
                list.add(stockMovement);
            }
            connection.close();
            return list;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    Order findOrderByReference(String reference) {
        DBConnection dbConnection = new DBConnection();
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    select id, reference, creation_datetime,"type",stat from "order" where reference like ?""");
            preparedStatement.setString(1, reference);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Order order = new Order();
                Integer idOrder = resultSet.getInt("id");
                order.setId(idOrder);
                order.setReference(resultSet.getString("reference"));
                order.setCreationDatetime(resultSet.getTimestamp("creation_datetime").toInstant());
                order.setDishOrderList(findDishOrderByIdOrder(idOrder));
                String statStr = resultSet.getString("stat");
                if (statStr != null) {
                    order.setStat(Command_stat.valueOf(statStr));
                }

                String typeStr = resultSet.getString("type");
                if (typeStr != null) {
                    order.setType(Command_type.valueOf(typeStr));
                }
                return order;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DishOrder> findDishOrderByIdOrder(Integer idOrder) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        List<DishOrder> dishOrders = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            select id, id_dish, quantity from dish_order where dish_order.id_order = ?
                            """);
            preparedStatement.setInt(1, idOrder);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Dish dish = findDishById(resultSet.getInt("id_dish"));
                DishOrder dishOrder = new DishOrder();
                dishOrder.setId(resultSet.getInt("id"));
                dishOrder.setQuantity(resultSet.getInt("quantity"));
                dishOrder.setDish(dish);
                dishOrders.add(dishOrder);
            }
            dbConnection.closeConnection(connection);
            return dishOrders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order saveOrder(Order orderToSave) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        try{
            connection.setAutoCommit(false);
            Order orderToFind = findOrderByReference(orderToSave.getReference());
            if (orderToFind != null && Command_stat.DELIVERED.equals(orderToFind.getStat())){
                connection.rollback();
                connection.close();
                throw new RuntimeException("Delivered order can t be updated");
            }
            else{
                PreparedStatement ps = connection.prepareStatement(
                        """
     INSERT INTO "order" (id, reference, creation_datetime,"type",stat) VALUES (?, ?, ?,?::Command_type,?::Command_stat)
                        ON CONFLICT (id) DO UPDATE
                        SET reference = EXCLUDED.reference,
                            creation_datetime = EXCLUDED.creation_datetime,
                            "type" = EXCLUDED.type,
                            stat = EXCLUDED.stat
                            
    """
                );
                ps.setInt(1,orderToSave.getId());
                ps.setString(2,orderToSave.getReference());
                ps.setTimestamp(3,Timestamp.from(orderToSave.getCreationDatetime()));
                ps.setString(4,orderToSave.getType().toString());
                ps.setString(5,orderToSave.getStat().toString());
                ps.executeUpdate();
                if(orderToSave.getDishOrders()!=null){
                    if(orderToSave.getDishOrders().isEmpty()){
                        PreparedStatement psDelete = connection.prepareStatement("delete from dish_order where id_order = ? ");
                        psDelete.setInt(1, orderToSave.getId());
                        psDelete.executeUpdate();
                        connection.commit();
                        connection.close();
                        throw new RuntimeException("No dish found in order");
                    }
                    else{
                        PreparedStatement psOrder = connection.prepareStatement(
                                "insert into dish_order (id, id_order,id_dish,quantity) " +
                                        "values (?,?,?,?) on conflict(id) do nothing");
                        for (DishOrder dishOrder : orderToSave.getDishOrders()) {
                            psOrder.setInt(1, dishOrder.getId());
                            psOrder.setInt(2,orderToSave.getId());
                            psOrder.setInt(3,dishOrder.getDish().getId());
                            psOrder.setInt(4,dishOrder.getQuantity());
                            psOrder.addBatch();
                            for(DishIngredient dishIngredient: findDishById(dishOrder.getDish().getId()).getDishIngredients()){
                                Ingredient ingredient = findIngredientById(dishIngredient.getIngredient().getId());
                                Double quantityRequired = dishIngredient.getQuantity()*dishOrder.getQuantity();
                                Double quantityActual = ingredient.getStockValueAt(orderToSave.getCreationDatetime()).getQuantity();
                                if(quantityRequired>quantityActual){
                                    connection.rollback();
                                    connection.close();
                                    throw new RuntimeException(ingredient.getName()+" quantity is not sufficient");
                                }
                            }
                        }

                        int[] result = psOrder.executeBatch();
                    }
                }
                connection.commit();
                connection.close();
                return findOrderByReference(orderToSave.getReference());
            }


        }catch (SQLException e){
            if(connection != null){
                try{
                    connection.close();
                    connection.rollback();
                }catch(SQLException e1){
                    throw new  RuntimeException(e1);
                }
            }
            throw new RuntimeException(e);
        }
    }
}