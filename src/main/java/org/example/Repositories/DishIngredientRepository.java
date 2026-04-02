package org.example.Repositories;


import org.example.DTO.DishIngredientRequest;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DishIngredientRepository {
    Connection connection;

    DishIngredientRepository(Connection connection) {
        this.connection = connection;
    }

    public void updateDishIngredients(int dishId, List<DishIngredientRequest> requests) {
        try(PreparedStatement stmtDelete = connection.prepareStatement(
                """
delete from dishingredient where id_dish =?
"""
        )){
            stmtDelete.setInt(1,dishId);
            stmtDelete.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO dishingredient(id_dish, id_ingredient, quantity_required, unit) VALUES (?, ?, ?, ?::unit_type)")) {
            connection.setAutoCommit(false);
            for (DishIngredientRequest req : requests) {
                stmt.setInt(1, dishId);
                stmt.setInt(2, req.getIdIngredient());
                stmt.setDouble(3, req.getQuantity());
                stmt.setString(4, req.getUnit().name());

                        stmt.executeUpdate();
                    }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
